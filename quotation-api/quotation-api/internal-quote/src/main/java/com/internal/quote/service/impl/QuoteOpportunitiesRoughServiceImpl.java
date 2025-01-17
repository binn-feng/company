package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.internal.quote.dto.QuoteOpportunitiesRoughSaveDTO;
import com.internal.quote.mapper.*;
import com.internal.quote.service.IQuoteOpportunitiesService;
import com.internal.quote.service.IQuoteRadioService;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.quote.vo.QuoteOpportunitiesRoughVO;
import com.internal.system.domain.SysPost;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.internal.quote.service.IQuoteOpportunitiesRoughService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机粗略报价Service业务层处理
 *
 * @author internal
 * @date 2024-10-17
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesRoughServiceImpl extends ServiceImpl<QuoteOpportunitiesRoughMapper, QuoteOpportunitiesRough> implements IQuoteOpportunitiesRoughService {

    private final QuoteOpportunitiesRoughMapper quoteOpportunitiesRoughMapper;
    private final QuoteOpportunitiesMapper quoteOpportunitiesMapper;
    private final QuoteOpportunitiesRoughDetailMapper quoteOpportunitiesRoughDetailMapper;
    private final IQuoteOpportunitiesService quoteService;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemConfig systemConfig;
    private final SysUserMapper sysUserMapper;
    private final SysDeptMapper sysDeptMapper;
    private final QuoteMailUtil quoteMailUtil;
    private final IQuoteRadioService radioService;
    /**
     * 查询报价系统-商机粗略报价
     *
     * @param id 报价系统-商机粗略报价主键
     * @return 报价系统-商机粗略报价
     */
    @Override
    public QuoteOpportunitiesRough selectQuoteOpportunitiesRoughById(Long id) {
        return quoteOpportunitiesRoughMapper.selectQuoteOpportunitiesRoughById(id);
    }

    /**
     * 查询报价系统-商机粗略报价列表
     *
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 报价系统-商机粗略报价
     */
    @Override
    public List<QuoteOpportunitiesRough> selectQuoteOpportunitiesRoughList(QuoteOpportunitiesRough quoteOpportunitiesRough) {
        return quoteOpportunitiesRoughMapper.selectQuoteOpportunitiesRoughList(quoteOpportunitiesRough);
    }

    /**
     * 新增报价系统-商机粗略报价
     *
     * @param dto 报价系统-商机粗略报价
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesRough(QuoteOpportunitiesRoughSaveDTO dto) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }

        /*if(dto.getRadioList().stream()
                .map(QuoteRadioDTO::getRadio).filter(ObjectUtil::isNotNull).reduce(BigDecimal.ZERO,BigDecimal::add).compareTo(BigDecimal.valueOf(100)) != 0){
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

        dto.setCreateTime(DateUtils.getNowDate());
        dto.setStatus(VersionStatus.INACTIVE.getCode());
        QuoteOpportunitiesRough opportunitiesRough = BeanUtil.toBean(dto, QuoteOpportunitiesRough.class);

        // 获取最新记录的version
        String currentVersion = quoteService.getCurrentVersion(quote.getSupportCrmId());
        opportunitiesRough.setValuationVersion(VersionUtil.incrementMajor(currentVersion));
        //修改三个表里所有OpportunitiesId为该id的状态为 未生效
        quoteService.setAllVersionStatusByCrm(quote.getSupportCrmId(), VersionStatus.INACTIVE);

        opportunitiesRough.setStatus(VersionStatus.ACTIVE.getCode());
        quoteOpportunitiesRoughMapper.insert(opportunitiesRough);
        if (CollUtil.isNotEmpty(dto.getQuoteOpportunitiesRoughDetails())) {
            dto.getQuoteOpportunitiesRoughDetails().forEach(item -> {
                item.setOpportunitiesId(opportunitiesRough.getOpportunitiesId());
                item.setRoughId(opportunitiesRough.getId());
                quoteOpportunitiesRoughDetailMapper.insert(item);
            });
        }
        //如果processCategory是2，就把状态改成 报价单
        LambdaUpdateWrapper<QuoteOpportunities> quoteUpdateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getAmount, dto.getAmount())
                .set(QuoteOpportunities::getType, QuoteType.ROUGH.getCode())
                .set(QuoteOpportunities::getUpdateTime, DateTime.now())
                .eq(QuoteOpportunities::getId, dto.getOpportunitiesId());
        //如果报价人为空，就把报价人设成自己
        if(ObjectUtil.isEmpty(quote.getQuoters())){
            quoteUpdateWrapper.set(QuoteOpportunities::getQuoters,currentUser.getCrmUserId());
        }
        if(QuoteProcessCategory.UPDATE_REQUIRED.getCode().equals(quote.getProcessCategory())){
            quoteUpdateWrapper.set(QuoteOpportunities::getProcessCategory,QuoteProcessCategory.UPDATED.getCode());
            quoteUpdateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.SALE_QUOTE.getCode());
        }else {
            quoteUpdateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.WAIT_SALE_QUOTE.getCode());
        }

        quoteOpportunitiesMapper.update(null, quoteUpdateWrapper);

        // 占比
        if(CollUtil.isNotEmpty(dto.getRadioList()) && dto.getRadioList().size()>1){
            dto.getRadioList().forEach(r ->{
                r.setOpportunitiesId(quote.getId());
                r.setCostId(opportunitiesRough.getId());
                r.setSupportCrmId(quote.getSupportCrmId());
            });
            List<QuoteRadio> radios = BeanUtil.copyToList(dto.getRadioList(),QuoteRadio.class);
            radioService.saveBatch(radios);
        }

        ThreadUtil.runInThread(() -> {
            // 发送邮件
            log.info("----------开始发送粗略报价邮件----------");
            SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
            EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
            EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
            List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
            // 设置主题信息
            QuoteOpportunities quoteOpportunities = quoteOpportunitiesMapper.selectById(opportunitiesRough.getOpportunitiesId());
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

                emailSubjectInfoConvertDTO.setQuoteType(QuoteType.ROUGH.getInfo());
                emailSubjectInfoConvertDTO.setVersion(opportunitiesRough.getValuationVersion());
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
            log.info("----------粗略报价邮件发送完毕----------");
        });
        return 1;
    }

    /**
     * 修改报价系统-商机粗略报价
     *
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesRough(QuoteOpportunitiesRough quoteOpportunitiesRough) {
        quoteOpportunitiesRough.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesRoughMapper.updateById(quoteOpportunitiesRough);
    }

    /**
     * 批量删除报价系统-商机粗略报价
     *
     * @param ids 需要删除的报价系统-商机粗略报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesRoughByIds(Long[] ids) {
        return quoteOpportunitiesRoughMapper.deleteQuoteOpportunitiesRoughByIds(ids);
    }

    /**
     * 删除报价系统-商机粗略报价信息
     *
     * @param id 报价系统-商机粗略报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesRoughById(Long id) {
        return quoteOpportunitiesRoughMapper.deleteQuoteOpportunitiesRoughById(id);
    }

    @Override
    public QuoteOpportunitiesRoughVO getRoughInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        LambdaQueryWrapper<QuoteOpportunitiesRough> queryChainWrapper = new LambdaQueryWrapper<QuoteOpportunitiesRough>();
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getId()), QuoteOpportunitiesRough::getId, quoteOpportunitiesDetailQuery.getId());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getOpportunitiesId()), QuoteOpportunitiesRough::getOpportunitiesId, quoteOpportunitiesDetailQuery.getOpportunitiesId());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getStatus()), QuoteOpportunitiesRough::getStatus, quoteOpportunitiesDetailQuery.getStatus());
        QuoteOpportunitiesRough rough = baseMapper.selectOne(queryChainWrapper);
        if (ObjectUtil.isEmpty(rough)) {
            return null;
        }
        QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail = quoteOpportunitiesRoughDetailMapper.selectOne(Wrappers.<QuoteOpportunitiesRoughDetail>lambdaQuery()
                .eq(QuoteOpportunitiesRoughDetail::getRoughId, rough.getId()));
        rough.setQuoteOpportunitiesRoughDetails(Collections.singletonList(quoteOpportunitiesRoughDetail));
        //数据库存了，无需再算，不然可能会错误覆盖
            /*
            opportunitiesRough.setAmount(quoteOpportunitiesRoughDetail.getCustomAmount().add(quoteOpportunitiesRoughDetail.getSelfAmount())
                    .add(quoteOpportunitiesRoughDetail.getOtherAmount()).add(quoteOpportunitiesRoughDetail.getProductAmount())
                    .add(quoteOpportunitiesRoughDetail.getExternalAmount()).add(quoteOpportunitiesRoughDetail.getImplementAmount())
                    .add(quoteOpportunitiesRoughDetail.getSupportAmount()));
            */
        QuoteOpportunitiesRoughVO vo = BeanUtil.toBean(rough,QuoteOpportunitiesRoughVO.class);
        vo.setVersion(rough.getValuationVersion());
        vo.setValuationVersion(QuoteType.ROUGH.getInfo() + rough.getValuationVersion());
        //占比

        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(vo.getOpportunitiesId());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        vo.setRadioList(radioService.getRadioList(vo.getId()));

        return vo;
    }

}
