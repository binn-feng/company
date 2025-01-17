package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.*;
import com.internal.common.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.quote.domain.*;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesDetailSaveDTO;
import com.internal.quote.dto.QuoteRadioDTO;
import com.internal.quote.mapper.QuoteOpportunitiesMapper;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.mapper.QuoteRadioMapper;
import com.internal.quote.service.*;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.quote.vo.*;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesDetailMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机详细报价Service业务层处理
 *
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class QuoteOpportunitiesDetailServiceImpl extends ServiceImpl<QuoteOpportunitiesDetailMapper, QuoteOpportunitiesDetail> implements IQuoteOpportunitiesDetailService {

    private final QuoteOpportunitiesDetailMapper quoteOpportunitiesDetailMapper;
    private final IQuoteOpportunitiesSupportService quoteOpportunitiesSupportService;
    private final IQuoteOpportunitiesCustomizableService quoteOpportunitiesCustomerService;
    private final IQuoteOpportunitiesProductService quoteOpportunitiesProductService;
    private final IQuoteOpportunitiesSelfService quoteOpportunitiesSelfService;
    private final IQuoteOpportunitiesExternalService quoteOpportunitiesExternalService;
    private final IQuoteOpportunitiesImplementService quoteOpportunitiesImportService;
    private final IQuoteOpportunitiesOtherService quoteOpportunitiesOtherServices;
    private final QuoteOpportunitiesMapper quoteOpportunitiesMapper;
    private final IQuoteOpportunitiesService quoteService;
    private final SysUserMapper sysUserMapper;
    private final QuoteMailUtil quoteMailUtil;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemConfig systemConfig;
    private final SysDeptMapper sysDeptMapper;
    private final IQuoteRadioService radioService;



    /**
     * 查询报价系统-商机详细报价
     *
     * @param quoteOpportunitiesDetailQuery 报价系统-商机详细报价主键
     * @return 报价系统-商机详细报价
     */
    @Override
    public QuoteOpportunitiesDetail selectQuoteOpportunitiesDetailById(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        return quoteOpportunitiesDetailMapper.selectQuoteOpportunitiesDetailById(quoteOpportunitiesDetailQuery);
    }

    /**
     * 查询报价系统-商机详细报价列表
     *
     * @param quoteOpportunitiesDetail 报价系统-商机详细报价
     * @return 报价系统-商机详细报价
     */
    @Override
    public List<QuoteOpportunitiesDetail> selectQuoteOpportunitiesDetailList(QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        return quoteOpportunitiesDetailMapper.selectQuoteOpportunitiesDetailList(quoteOpportunitiesDetail);
    }

    /**
     * 新增报价系统-商机详细报价
     *
     * @param dto 报价系统-商机详细报价
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesDetail(QuoteOpportunitiesDetailSaveDTO dto) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }

        /*if(!dto.getStatus().equals(VersionStatus.TEMPORARY.getCode()) && (dto.getRadioList().stream()
                .map(QuoteRadioDTO::getRadio).filter(ObjectUtil::isNotNull).reduce(BigDecimal.ZERO,BigDecimal::add).compareTo(BigDecimal.valueOf(100)) != 0)){
            throw new RuntimeException("报价部门占比数据错误，请重新检查!");
        }*/

        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(dto.getOpportunitiesId());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        //使用当前最新quote的id
        dto.setOpportunitiesId(quote.getId());

        // 清除暂存版本
        Set<Long> quoteIdList = aboutQuoteList.stream().map(QuoteOpportunities::getId).collect(Collectors.toSet());
        clearTemporaryInfo(quoteIdList);

        QuoteOpportunitiesDetail quoteOpportunitiesDetail = BeanUtil.toBean(dto, QuoteOpportunitiesDetail.class);

        LambdaUpdateWrapper<QuoteOpportunities> quoteUpdateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                .eq(QuoteOpportunities::getId, quote.getId())
                .set(QuoteOpportunities::getUpdateTime, DateTime.now());
        //如果报价人为空，就把报价人设成自己
        if(ObjectUtil.isEmpty(quote.getQuoters())){
            quoteUpdateWrapper.set(QuoteOpportunities::getQuoters,currentUser.getCrmUserId());
        }
        if (quoteOpportunitiesDetail.getStatus().equals(VersionStatus.TEMPORARY.getCode())) {
            quoteOpportunitiesDetail.setValuationVersion("无");
            //详细报价,如果暂存,则把状态改为"待更新成本报价",(即使是已成本报价，出现暂存后也要打回到"待更新成本报价"状态)
            quoteUpdateWrapper.set(QuoteOpportunities::getStatus, QuoteStatusEnum.UPDATE_PENDING.getCode());
        }
        else
        {
            // 获取最新记录的version
            String currentVersion = quoteService.getCurrentVersion(quote.getSupportCrmId());
            quoteOpportunitiesDetail.setValuationVersion(VersionUtil.incrementMajor(currentVersion));
            // 已完成详细报价
            //如果processCategory是2，就把状态改成 报价单
            quoteUpdateWrapper.set(QuoteOpportunities::getType, QuoteType.COST.getCode())
                    .set(QuoteOpportunities::getAmount,dto.getAmount());
            if(QuoteProcessCategory.UPDATE_REQUIRED.getCode().equals(quote.getProcessCategory())){
                quoteUpdateWrapper.set(QuoteOpportunities::getProcessCategory,QuoteProcessCategory.UPDATED.getCode());
                quoteUpdateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.SALE_QUOTE.getCode());
            } else {
                quoteUpdateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.WAIT_SALE_QUOTE.getCode());
            }
        }
        log.info("==========开始更新报价单:{}信息==========",JSON.toJSONString(quoteUpdateWrapper.getSqlSet()));
        quoteOpportunitiesMapper.update(null, quoteUpdateWrapper);
        if (ObjectUtil.isNotEmpty(dto.getStatus()) && "1".equals(dto.getStatus())) {
            //修改三个表里所有OpportunitiesId为该id的状态为 未生效
            quoteService.setAllVersionStatusByCrm(quote.getSupportCrmId(), VersionStatus.INACTIVE);

            quoteOpportunitiesDetail.setStatus(VersionStatus.ACTIVE.getCode());
        }
        baseMapper.insert(quoteOpportunitiesDetail);
        // 售前支持
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesSupportList())) {
            quoteOpportunitiesSupportService.saveBatchDetail(dto.getQuoteOpportunitiesSupportList(), quoteOpportunitiesDetail);
        }
        // 定制开发
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesCustomizableList())) {
            quoteOpportunitiesCustomerService.saveBatchDetail(dto.getQuoteOpportunitiesCustomizableList(), quoteOpportunitiesDetail);
        }
        // 产品平台小记
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesProductList())) {
            quoteOpportunitiesProductService.saveBatchDetail(dto.getQuoteOpportunitiesProductList(), quoteOpportunitiesDetail);
        }
        // 自研硬件小记
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesSelfList())) {
            quoteOpportunitiesSelfService.saveBatchDetail(dto.getQuoteOpportunitiesSelfList(), quoteOpportunitiesDetail);
        }
        // 外采硬件小记
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesExternalList())) {
            quoteOpportunitiesExternalService.saveBatchDetail(dto.getQuoteOpportunitiesExternalList(), quoteOpportunitiesDetail);
        }
        // 实施小记
        if (ObjectUtil.isNotEmpty(dto.getQuoteOpportunitiesImplementList())) {
            quoteOpportunitiesImportService.saveBatchDetail(Lists.newArrayList(dto.getQuoteOpportunitiesImplementList()), quoteOpportunitiesDetail);
        }
        // 其他
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesOtherList())) {
            quoteOpportunitiesOtherServices.saveBatchDetail(dto.getQuoteOpportunitiesOtherList(), quoteOpportunitiesDetail);
        }
        // 占比
        if(CollUtil.isNotEmpty(dto.getRadioList()) && dto.getRadioList().size()>1){
            dto.getRadioList().forEach(r ->{
                r.setOpportunitiesId(quote.getId());
                r.setCostId(quoteOpportunitiesDetail.getId());
                r.setSupportCrmId(quote.getSupportCrmId());
            });
            List<QuoteRadio> radios = BeanUtil.copyToList(dto.getRadioList(),QuoteRadio.class);
            radioService.saveBatch(radios);
        }
        ThreadUtil.runInThread(() -> {
            // 发送邮件
            log.info("----------开始发送详细报价邮件----------");
            SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
            EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
            EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
            List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
            // 设置主题信息
            QuoteOpportunities quoteOpportunities = quoteOpportunitiesMapper.selectById(dto.getOpportunitiesId());
            if (ObjectUtil.isNotEmpty(quoteOpportunities)) {
                List<SysUser> userList = sysUserList.stream()
                        .filter(sysUser -> ObjectUtil.isNotEmpty(sysUser.getCrmUserId()) && quoteOpportunities.getSupportPerson().contains(sysUser.getCrmUserId()))
                        .collect(Collectors.toList());
                if(CollUtil.isNotEmpty(userList)){
                    List<Long> deptIdList = userList.stream().map(SysUser::getDeptId).collect(Collectors.toList());
                    List<SysDept> sysDeptList = sysDeptMapper.selectList(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIdList));
                    String deptName = sysDeptList.stream().map(SysDept::getDeptName).collect(Collectors.joining(","));
                    emailSubjectInfoConvertDTO.setPreSaleDepartment(deptName);
                    emailModelInfoConvertDTO.setPreSaleDepartment(deptName);
                }
                emailSubjectInfoConvertDTO.setQuoteType(QuoteType.COST.getInfo());
                emailSubjectInfoConvertDTO.setVersion(quoteOpportunitiesDetail.getValuationVersion());
                emailSubjectInfoConvertDTO.setCurrentDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
            }
            // 获取所属销售
            QuoteOpportunitiesParent quoteOpportunitiesParent = quoteOpportunitiesParentMapper.selectById(quoteOpportunities.getOpportunitiesParentId());
            if (ObjectUtil.isNotEmpty(quoteOpportunitiesParent)) {
                sysUserList.stream().filter(sysUser -> sysUser.getUserId().equals(quoteOpportunitiesParent.getSaleId())).findFirst().ifPresent(sendEmailInfoDTO::setSysUser);
                emailSubjectInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
                emailModelInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
            }

            // 设置模版信息
            emailModelInfoConvertDTO.setCostTotal(DecimalUtil.getStr(dto.getAmount(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setQuoteSystemLink(systemConfig.getDomainName());

            sendEmailInfoDTO.setEmailModelInfoConvertDTO(emailModelInfoConvertDTO);
            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(emailSubjectInfoConvertDTO);
            quoteMailUtil.sendEmail(Collections.singletonList(sendEmailInfoDTO), EmailTypeEnum.COMPLETE_COST_QUOTATION.getCode());
            log.info("----------详细报价邮件发送完毕----------");
        });
        return 1;
    }


    /**
     * 修改报价系统-商机详细报价
     *
     * @param quoteOpportunitiesDetail 报价系统-商机详细报价
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesDetail(QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        quoteOpportunitiesDetail.setUpdateTime(DateUtils.getNowDate());
        log.info("==========修改报价系统-商机详细报价-信息:{}==========",JSON.toJSONString(quoteOpportunitiesDetail));
        return quoteOpportunitiesDetailMapper.updateById(quoteOpportunitiesDetail);
    }

    /**
     * 批量删除报价系统-商机详细报价
     *
     * @param ids 需要删除的报价系统-商机详细报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesDetailByIds(Long[] ids) {
        return quoteOpportunitiesDetailMapper.deleteQuoteOpportunitiesDetailByIds(ids);
    }

    /**
     * 删除报价系统-商机详细报价信息
     *
     * @param id 报价系统-商机详细报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesDetailById(Long id) {
        return quoteOpportunitiesDetailMapper.deleteQuoteOpportunitiesDetailById(id);
    }

    @Override
    public QuoteOpportunitiesDetailVO getDetailInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        LambdaQueryWrapper<QuoteOpportunitiesDetail> queryChainWrapper = new LambdaQueryWrapper<QuoteOpportunitiesDetail>();
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getId()), QuoteOpportunitiesDetail::getId, quoteOpportunitiesDetailQuery.getId());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getOpportunitiesId()), QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getOpportunitiesId());
        queryChainWrapper.in(CollUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getQuoteIdList()), QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getQuoteIdList());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getStatus()), QuoteOpportunitiesDetail::getStatus, quoteOpportunitiesDetailQuery.getStatus());

        QuoteOpportunitiesDetail opportunitiesDetail = baseMapper.selectOne(queryChainWrapper);
        if (ObjectUtil.isNull(opportunitiesDetail)) {
            return null;
        }

        QuoteOpportunitiesDetailVO quoteOpportunitiesDetailVO = BeanUtil.toBean(opportunitiesDetail, QuoteOpportunitiesDetailVO.class);
        quoteOpportunitiesDetailVO.setVersion(opportunitiesDetail.getValuationVersion());
        quoteOpportunitiesDetailVO.setValuationVersion(QuoteType.COST.getInfo() + opportunitiesDetail.getValuationVersion());
        quoteOpportunitiesDetailVO.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(opportunitiesDetail.getCreateTime()));
        SysUser sysUser = sysUserMapper.selectUserById(Long.valueOf(opportunitiesDetail.getCreateBy()));
        if(ObjectUtil.isNotEmpty(sysUser)){
            quoteOpportunitiesDetailVO.setPerson(sysUser.getNickName());
        }
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesSupportVO(quoteOpportunitiesSupportService.getSupportInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesCustomizableVo(quoteOpportunitiesCustomerService.getCustomizableInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesProductVO(quoteOpportunitiesProductService.getProductInfo(opportunitiesDetail.getId()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesSelfVO(quoteOpportunitiesSelfService.getSelfInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesExternalVO(quoteOpportunitiesExternalService.getExternalInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesImplement(quoteOpportunitiesImportService.getImportInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        quoteOpportunitiesDetailVO.setQuoteOpportunitiesOtherVO(quoteOpportunitiesOtherServices.getOtherInfo(QuoteOpportunitiesDetailQuery
                .builder()
                .opportunitiesDetailId(opportunitiesDetail.getId())
                .build()));
        //占比
        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(quoteOpportunitiesDetailVO.getOpportunitiesId());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        quoteOpportunitiesDetailVO.setRadioList(radioService.getRadioList(quoteOpportunitiesDetailVO.getId()));
        BigDecimal amount = Optional.of(quoteOpportunitiesDetailVO)
                .map(vo -> Optional.ofNullable(vo.getQuoteOpportunitiesSupportVO()).map(QuoteOpportunitiesSupportVO::getTotalCost).orElse(BigDecimal.ZERO)
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesCustomizableVo()).map(QuoteOpportunitiesCustomizableVO::getTotalSoftwareCosts).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesProductVO()).map(QuoteOpportunitiesProductVO::getTotalCost).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesSelfVO()).map(QuoteOpportunitiesSelfVO::getTotalCost).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesExternalVO()).map(QuoteOpportunitiesExternalVO::getTotalEstimatedlCost).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesImplement()).map(QuoteOpportunitiesImplement::getTotalCost).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(vo.getQuoteOpportunitiesOtherVO()).map(QuoteOpportunitiesOtherVO::getTotalCost).orElse(BigDecimal.ZERO)))
                .orElse(BigDecimal.ZERO);

        quoteOpportunitiesDetailVO.setAmount(amount);
        return quoteOpportunitiesDetailVO;
    }

    /**
     * 根据详情ID清理暂存信息
     *
     * @param idList
     */
    private void clearTemporaryInfo(Set<Long> idList) {
        List<QuoteOpportunitiesDetail> quoteOpportunitiesDetailList = baseMapper.selectList(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                .in(QuoteOpportunitiesDetail::getOpportunitiesId, idList).eq(QuoteOpportunitiesDetail::getStatus, VersionStatus.TEMPORARY.getCode()));
        if (CollUtil.isNotEmpty(quoteOpportunitiesDetailList)) {
            List<Long> detailIdList = quoteOpportunitiesDetailList.stream().map(QuoteOpportunitiesDetail::getId).collect(Collectors.toList());
            quoteOpportunitiesSupportService.remove(Wrappers.<QuoteOpportunitiesSupport>lambdaQuery()
                    .in(QuoteOpportunitiesSupport::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesCustomerService.remove(Wrappers.<QuoteOpportunitiesCustomizable>lambdaQuery()
                    .in(QuoteOpportunitiesCustomizable::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesProductService.remove(Wrappers.<QuoteOpportunitiesProduct>lambdaQuery()
                    .in(QuoteOpportunitiesProduct::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesSelfService.remove(Wrappers.<QuoteOpportunitiesSelf>lambdaQuery()
                    .in(QuoteOpportunitiesSelf::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesExternalService.remove(Wrappers.<QuoteOpportunitiesExternal>lambdaQuery()
                    .in(QuoteOpportunitiesExternal::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesImportService.remove(Wrappers.<QuoteOpportunitiesImplement>lambdaQuery()
                    .in(QuoteOpportunitiesImplement::getOpportunitiesDetailId, detailIdList));
            quoteOpportunitiesOtherServices.remove(Wrappers.<QuoteOpportunitiesOther>lambdaQuery()
                    .in(QuoteOpportunitiesOther::getOpportunitiesDetailId, detailIdList));
            radioService.remove(Wrappers.<QuoteRadio>lambdaQuery()
                    .in(QuoteRadio::getCostId,detailIdList));
            baseMapper.delete(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery().in(QuoteOpportunitiesDetail::getId, detailIdList));
        }
    }
}
