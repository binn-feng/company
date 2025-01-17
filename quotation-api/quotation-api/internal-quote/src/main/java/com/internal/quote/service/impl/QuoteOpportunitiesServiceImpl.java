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
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.core.domain.BaseEntity;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysRole;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.core.text.Convert;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.*;
import com.internal.common.request.PageParams;
import com.internal.common.utils.*;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageHardwareSelf;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.domain.ManageQuote;
import com.internal.manager.mapper.ManageHardwareExtMapper;
import com.internal.manager.mapper.ManageHardwareSelfMapper;
import com.internal.manager.mapper.ManageProductMapper;
import com.internal.manager.mapper.ManageQuoteMapper;
import com.internal.quote.domain.*;
import com.internal.quote.dto.*;
import com.internal.quote.mapper.*;
import com.internal.quote.service.*;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.quote.util.QuoteUtil;
import com.internal.quote.vo.*;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import com.internal.system.mapper.SysUserRoleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机报价信息Service业务层处理
 * 
 * @author internal
 * @date 2024-10-14
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuoteOpportunitiesServiceImpl extends ServiceImpl<QuoteOpportunitiesMapper, QuoteOpportunities> implements IQuoteOpportunitiesService
{

    private final QuoteOpportunitiesMapper quoteOpportunitiesMapper;
    private final QuoteOpportunitiesParentMapper parentMapper;
    private final QuoteOpportunitiesDetailMapper detailMapper;
    private final QuoteOpportunitiesRoughMapper roughMapper;
    private final QuoteOpportunitiesRoughDetailMapper roughDetailMapper;
    private final QuoteOpportunitiesFileMapper fileMapper;
    private final QuotePresaleInfoMapper presaleInfoMapper;
    private final ManageQuoteMapper manageQuoteMapper;
    private final SysUserRoleMapper systemUserRoleMapper;
    private final QuoteSignInfoMapper signInfoMapper;
    private final QuoteOpportunitiesUnableMapper unableMapper;
    private final QuoteMailUtil quoteMailUtil;
    private final QuoteEmailSettingMapper quoteEmailSettingMapper;
    private final SysUserMapper sysUserMapper;
    private final SystemConfig systemConfig;
    private final String EmptyStr = "无";
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SyncCrmMapper syncCrmMapper;
    private final QuoteUtil quoteUtil;
    private final SysDeptMapper sysDeptMapper;
    private final IQuoteOpportunitiesSupportService quoteOpportunitiesSupportService;
    private final IQuoteOpportunitiesCustomizableService quoteOpportunitiesCustomerService;
    private final IQuoteOpportunitiesProductService quoteOpportunitiesProductService;
    private final IQuoteOpportunitiesSelfService quoteOpportunitiesSelfService;
    private final IQuoteOpportunitiesExternalService quoteOpportunitiesExternalService;
    private final IQuoteOpportunitiesImplementService quoteOpportunitiesImportService;
    private final IQuoteOpportunitiesOtherService quoteOpportunitiesOtherServices;
    private final ManageProductMapper productMapper;
    private final ManageHardwareSelfMapper hardwareSelfMapper;
    private final ManageHardwareExtMapper hardwareExtMapper;


    /**
     * 查询报价系统-商机报价信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 报价系统-商机报价信息
     */
    @Override
    public QuoteOpportunities selectQuoteOpportunitiesById(Long id)
    {
        QuoteOpportunities quoteOpportunities = quoteOpportunitiesMapper.selectById(id);
        quoteOpportunities.setQuoteOpportunitiesFileList(fileMapper
                .selectList(Wrappers.<QuoteOpportunitiesFile>lambdaQuery().eq(QuoteOpportunitiesFile::getOpportunitiesId, id)));
        return quoteOpportunities;
    }

    /**
     * 查询报价系统-商机报价信息列表
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 报价系统-商机报价信息
     */
    @Override
    public List<QuoteOpportunitiesVO> selectQuoteOpportunitiesList(QuoteOpportunitiesQuery quoteOpportunities)
    {
        List<QuoteOpportunitiesVO> resultList = quoteOpportunitiesMapper.selectQuoteOpportunitiesList(quoteOpportunities);
        return resultList;
    }

    @Override
    public Long selectQuoteOpportunitiesCount(QuoteOpportunitiesQuery quoteOpportunities) {
        return quoteOpportunitiesMapper.selectQuoteOpportunitiesCount(quoteOpportunities);
    }

    /**
     * 主查询
     * @param model 查询条件
     * @param selectDetail 是否查询明细
     * @return
     */
    @Override
    public List<QuoteOpportunitiesVO> getQuoteOpportunitiesList(QuoteOpportunitiesQuery model,Boolean selectDetail) {
        List<QuoteOpportunitiesVO> voList = quoteOpportunitiesMapper.selectQuoteOpportunitiesList(model);
        if(CollUtil.isEmpty(voList))
        {
            return new LinkedList<>();
        }

        if(selectDetail)
        {
            SysUser currentUser = SecurityUtils.getLoginUser().getUser();
            if(ObjectUtil.isEmpty(currentUser)){
                throw new RuntimeException("无效的用户");
            }
            Set<String> crmIdList = voList.stream().map(QuoteOpportunitiesVO::getSupportCrmId).collect(Collectors.toSet());
            //主表map,查询非废弃数据
            Map<String,List<QuoteOpportunities>> quoteMap = quoteOpportunitiesMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                    .in(QuoteOpportunities::getSupportCrmId,crmIdList))
                    .stream().collect(Collectors.groupingBy(QuoteOpportunities::getSupportCrmId));
            //全部全部的QuoteOpportunitiesId
            Set<Long> idList = quoteMap.values().stream().flatMap(List::stream).map(QuoteOpportunities::getId).collect(Collectors.toSet());

            List<QuoteOpportunitiesDetail> detailList =  detailMapper.selectList(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                            .in(QuoteOpportunitiesDetail::getOpportunitiesId,idList)
                            .eq(QuoteOpportunitiesDetail::getStatus,VersionStatus.ACTIVE.getCode()));
            List<QuoteOpportunitiesRough> roughList = roughMapper.selectList(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                            .in(QuoteOpportunitiesRough::getOpportunitiesId,idList)
                            .eq(QuoteOpportunitiesRough::getStatus,VersionStatus.ACTIVE.getCode()));
            List<QuoteOpportunitiesUnable> unableList = unableMapper.selectList(Wrappers.<QuoteOpportunitiesUnable>lambdaQuery()
                    .in(QuoteOpportunitiesUnable::getOpportunitiesId,idList)
                    .eq(QuoteOpportunitiesUnable::getStatus,VersionStatus.ACTIVE.getCode()));
            List<QuotePresaleInfo> presaleList = presaleInfoMapper.selectList(Wrappers.<QuotePresaleInfo>lambdaQuery()
                            .in(QuotePresaleInfo::getOpportunitiesId,idList)
                            .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));
            List<QuoteSignInfo> signInfoList = signInfoMapper.selectList(Wrappers.<QuoteSignInfo>lambdaQuery()
                            .in(QuoteSignInfo::getOpportunitiesId,idList)
                            .eq(QuoteSignInfo::getStatus,VersionStatus.ACTIVE.getCode()));

            Map<String,SysUser> userMap = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                    .isNotNull(SysUser::getCrmUserId)).stream().collect(Collectors.toMap(SysUser::getCrmUserId,x -> x));

            //详细报价map
            Map<String,QuoteOpportunitiesDetail> detailMap = new HashMap<>();
            //粗略报价map
            Map<String,QuoteOpportunitiesRough> roughMap = new HashMap<>();
            //无法报价map
            Map<String,QuoteOpportunitiesUnable> unableMap = new HashMap<>();
            //销售报价map
            Map<String,QuotePresaleInfo> presaleInfoMap = new HashMap<>();
            //签约map
            Map<String,QuoteSignInfo> signInfoMap = new HashMap<>();
            Map<String,ManageQuote> manageQuoteMap = manageQuoteMapper.selectList(Wrappers.<ManageQuote>lambdaQuery()).stream().collect(Collectors.toMap(x -> x.getType(),x -> x));

            quoteMap.forEach((key,val) -> {
                Set<Long> quoteIdList = val.stream().map(QuoteOpportunities::getId).collect(Collectors.toSet());
                detailMap.put(key,
                        detailList.stream().filter(x -> quoteIdList.contains(x.getOpportunitiesId())).findFirst().orElse(null)
                );
                roughMap.put(key,
                        roughList.stream().filter(x -> quoteIdList.contains(x.getOpportunitiesId())).findFirst().orElse(null)
                );
                unableMap.put(key,
                        unableList.stream().filter(x -> quoteIdList.contains(x.getOpportunitiesId())).findFirst().orElse(null)
                );
                presaleInfoMap.put(key,
                        presaleList.stream().filter(x -> quoteIdList.contains(x.getOpportunitiesId())).findFirst().orElse(null)
                );
                signInfoMap.put(key,
                        signInfoList.stream().filter(x -> quoteIdList.contains(x.getOpportunitiesId())).findFirst().orElse(null)
                );

            });

            List<String> saleStatusList = Arrays.asList(
                    QuoteStatusEnum.SALE_QUOTE.getCode(),QuoteStatusEnum.SALE_REJECTED.getCode(),QuoteStatusEnum.SALE_PENDING_APPROVAL.getCode()
            );
            List<String> signStatusList = Arrays.asList(
                    QuoteStatusEnum.SIGN_PENDING_APPROVAL.getCode(),QuoteStatusEnum.SIGN_REJECTED.getCode(),QuoteStatusEnum.WAIT_SIGN.getCode(),QuoteStatusEnum.SIGNED.getCode()
            );

            for (QuoteOpportunitiesVO vo: voList) {
                //报价版本
                if(QuoteStatusEnum.ABANDON.getCode().equals(vo.getStatus())){
                    continue;
                }
                QuoteOpportunitiesDetail detail = MapUtil.getMapValue(detailMap,vo.getSupportCrmId());
                QuoteOpportunitiesRough rough = MapUtil.getMapValue(roughMap,vo.getSupportCrmId());
                QuoteOpportunitiesUnable unable = MapUtil.getMapValue(unableMap,vo.getSupportCrmId());
                if(ObjectUtil.isNotNull(vo.getType())){
                    if(vo.getType().equals(QuoteType.COST.getCode())){
                        if(ObjectUtil.isNotEmpty(detail)){
                            vo.setCostQuoteVersion(detail.getValuationVersion());
                            vo.setCurrentVersion(QuoteType.COST.getInfo() + detail.getValuationVersion());
                        }
                    }else if(vo.getType().equals(QuoteType.ROUGH.getCode())) {
                        if(ObjectUtil.isNotEmpty(rough)){
                            vo.setCostQuoteVersion(rough.getValuationVersion());
                            vo.setCurrentVersion(QuoteType.ROUGH.getInfo() + rough.getValuationVersion());
                        }
                    }else {
                        if(ObjectUtil.isNotEmpty(unable)){
                            vo.setCostQuoteVersion(unable.getValuationVersion());
                            vo.setCurrentVersion(QuoteType.INCAPABLE.getInfo() + unable.getValuationVersion());
                            vo.setQuoteDesc(unable.getValuationDesc());
                        }
                    }
                }

                QuotePresaleInfo presaleInfo = MapUtil.getMapValue(presaleInfoMap,vo.getSupportCrmId());
                if(ObjectUtil.isNotEmpty(presaleInfo)){
                    if(saleStatusList.contains(vo.getStatus())){
                        if(vo.getType().equals(QuoteType.INCAPABLE.getCode())){
                            vo.setDescription(presaleInfo.getDescription());
                            vo.setProjExtQuoteTotal(presaleInfo.getUnableQuoteAmount());
                        }else {
                            //销售对外报价利润率需要重算
                            //没时间优化，只能这样写了，报价单页面可能需要几秒钟
                            if(QuoteProcessCategory.UPDATED.getCode().equals(vo.getProcessCategory()) && QuoteStatusEnum.SALE_QUOTE.getCode().equals(vo.getStatus())){
                                calcProfitRateExclToQuote(vo,detail,rough,presaleInfo,manageQuoteMap);
                            }else {
                                vo.setProjProfitRateExcl(presaleInfo.getProjProfitRateExcl());
                            }
                            vo.setProjExtQuoteTotal(DecimalUtil.addAll(
                                    presaleInfo.getPreExtQuote(),presaleInfo.getDevExtQuote(),presaleInfo.getProdExtQuote(),
                                    presaleInfo.getOtherExtQuote(),presaleInfo.getSelfExtQuote(),presaleInfo.getExtExtQuote(),
                                    presaleInfo.getImpExtQuote()
                            ));
                        }
                    }

                    //跟进理由
                    vo.setCloseNote(presaleInfo.getCloseNote());
                    vo.setSaleAuditDesc(presaleInfo.getSaleAuditDesc());

                    if(ObjectUtil.isEmpty(vo.getCloseNote())){
                        vo.setCloseNote("无");
                    }
                }
                QuoteSignInfo signInfo = MapUtil.getMapValue(signInfoMap,vo.getSupportCrmId());
                if(ObjectUtil.isNotEmpty(signInfo)){
                    vo.setSignQuoteTotal(signInfo.getNorthAmount());//总金额
                    vo.setSignProjProfitRateExcl(signInfo.getCostProfitRate());//利润率
                    vo.setSignAuditDesc(signInfo.getSignAuditDesc());
                    vo.setContractType(signInfo.getContractType());
                    vo.setTime(signInfo.getCreateTime());
                }

                //设置前端判断条件
                quoteUtil.setQuoteMethod(vo,currentUser,userMap);

                //判断是否是多部门
                 vo.setIsMultiDept(isMultiDept(userMap,vo.getSupportPerson()));
            }
        }


        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean incapable(QuoteOpportunitiesUpdateDTO dto) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }

        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = getAboutQuoteList(dto.getId());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        //使用当前最新quote的id
        dto.setId(quote.getId());

        LambdaUpdateWrapper<QuoteOpportunities> quoteUpdateWrapper =
                Wrappers.<QuoteOpportunities>lambdaUpdate()
                        .eq(QuoteOpportunities::getId, dto.getId())
                        .set(QuoteOpportunities::getAmount,BigDecimal.ZERO)
                        .set(QuoteOpportunities::getUpdateTime, DateTime.now())
                        .set(QuoteOpportunities::getStatus,QuoteStatusEnum.WAIT_SALE_QUOTE.getCode())
                        .set(QuoteOpportunities::getType,QuoteType.INCAPABLE.getCode());
        //如果报价人为空，就把报价人设成自己
        if(ObjectUtil.isEmpty(quote.getQuoters())){
            quoteUpdateWrapper.set(QuoteOpportunities::getQuoters,currentUser.getCrmUserId());
        }
        //修改报价状态
        baseMapper.update(null,quoteUpdateWrapper);
        //修改三个表里所有OpportunitiesId为该id的状态为 未生效
        setAllVersionStatusByCrm(quote.getSupportCrmId(), VersionStatus.INACTIVE);
        //新增一条生效的 无法报价
        QuoteOpportunitiesUnable unable = new QuoteOpportunitiesUnable();
        unable.setCreateTime(DateTime.now());
        unable.setUpdateTime(DateTime.now());
        unable.setOpportunitiesId(dto.getId());
        unable.setStatus(VersionStatus.ACTIVE.getCode());
        //TODO:set Version
        String currentVersion = getCurrentVersion(quote.getSupportCrmId());
        unable.setValuationVersion(VersionUtil.incrementMajor(currentVersion));
        unable.setValuationDesc(dto.getQuoteDesc());
        unableMapper.insert(unable);
        ThreadUtil.runInThread(() ->{
            // 发送邮件
            log.info("----------开始发送无法报价邮件----------");
            SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
            EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
            EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
            List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
            // 设置主题信息
            QuoteOpportunities quoteOpportunities = quoteOpportunitiesMapper.selectById(dto.getId());
            if(ObjectUtil.isNotEmpty(quoteOpportunities)){
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
                emailSubjectInfoConvertDTO.setQuoteType(QuoteType.INCAPABLE.getInfo());
                emailSubjectInfoConvertDTO.setVersion(unable.getValuationVersion());
                emailSubjectInfoConvertDTO.setCurrentDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
            }
            // 获取所属销售
            QuoteOpportunitiesParent quoteOpportunitiesParent = quoteOpportunitiesParentMapper.selectById(quoteOpportunities.getOpportunitiesParentId());
            if(ObjectUtil.isNotEmpty(quoteOpportunitiesParent)){
                sysUserList.stream().filter(sysUser -> sysUser.getUserId().equals(quoteOpportunitiesParent.getSaleId())).findFirst().ifPresent(sendEmailInfoDTO::setSysUser);
                emailSubjectInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
                emailModelInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
            }

            // 设置模版信息
            emailModelInfoConvertDTO.setCostTotal("无");
            emailModelInfoConvertDTO.setQuoteSystemLink(systemConfig.getDomainName());

            sendEmailInfoDTO.setEmailModelInfoConvertDTO(emailModelInfoConvertDTO);
            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(emailSubjectInfoConvertDTO);
            quoteMailUtil.sendEmail(Collections.singletonList(sendEmailInfoDTO), EmailTypeEnum.COMPLETE_COST_QUOTATION.getCode());
            log.info("----------无法报价邮件发送完毕----------");
        });

        return true;
    }

    /**
     * 新增报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunities(QuoteOpportunities quoteOpportunities)
    {
        quoteOpportunities.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesMapper.insert(quoteOpportunities);
    }

    /**
     * 修改报价系统-商机报价信息
     * 
     * @param quoteOpportunities 报价系统-商机报价信息
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunities(QuoteOpportunities quoteOpportunities)
    {
        quoteOpportunities.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesMapper.updateById(quoteOpportunities);
    }

    /**
     * 批量删除报价系统-商机报价信息
     * 
     * @param ids 需要删除的报价系统-商机报价信息主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesByIds(Long[] ids)
    {
        return quoteOpportunitiesMapper.deleteQuoteOpportunitiesByIds(ids);
    }

    /**
     * 删除报价系统-商机报价信息信息
     * 
     * @param id 报价系统-商机报价信息主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesById(Long id)
    {
        return quoteOpportunitiesMapper.deleteQuoteOpportunitiesById(id);
    }

    @Override
    public List<OpportunitiesDetailVersionVO> getVersionList(QuoteOpportunitiesRoughQuery query) {
        QuoteOpportunities current = baseMapper.selectById(query.getOpportunitiesId());
        if(ObjectUtil.isEmpty(current)){
            throw new RuntimeException("无效的报价信息");
        }
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }
        List<QuoteOpportunities> quoteList = baseMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getSupportCrmId,current.getSupportCrmId()));
        if(CollUtil.isEmpty(quoteList)){
            throw new RuntimeException("无效的报价信息");
        }
        Set<Long> quoteIdLIst = quoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        QuoteOpportunities nextQuote = quoteList.stream()
                .filter(x -> x.getCreateTime().compareTo(current.getCreateTime()) > 0)
                .sorted(Comparator.comparing(QuoteOpportunities::getCreateTime))
                .findFirst().orElse(null);

        List<OpportunitiesDetailVersionVO> resultVersionList = new LinkedList<>();
        LambdaQueryWrapper<QuoteOpportunitiesDetail> detailWrapper = Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                .in(QuoteOpportunitiesDetail::getOpportunitiesId, quoteIdLIst)
                .ne(QuoteOpportunitiesDetail::getStatus, VersionStatus.TEMPORARY.getCode());
        LambdaQueryWrapper<QuoteOpportunitiesRough> roughWrapper = Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                .in(QuoteOpportunitiesRough::getOpportunitiesId, quoteIdLIst)
                .ne(QuoteOpportunitiesRough::getStatus, VersionStatus.TEMPORARY.getCode());
        LambdaQueryWrapper<QuoteOpportunitiesUnable> unableWrapper = Wrappers.<QuoteOpportunitiesUnable>lambdaQuery()
                .in(QuoteOpportunitiesUnable::getOpportunitiesId,quoteIdLIst)
                .ne(QuoteOpportunitiesUnable::getStatus, VersionStatus.TEMPORARY.getCode());

        //数据隔离
        //如果是管理员不筛
        if(query.getIsolation() && !currentUser.isAdmin()){
            //如果没有下一个，说明这个就是最新的,就不用筛了
            if(ObjectUtil.isNotEmpty(nextQuote)){
                detailWrapper.lt(QuoteOpportunitiesDetail::getCreateTime,nextQuote.getCreateTime());
                roughWrapper.lt(QuoteOpportunitiesRough::getCreateTime,nextQuote.getCreateTime());
                unableWrapper.lt(QuoteOpportunitiesUnable::getCreateTime,nextQuote.getCreateTime());
            }
        }

        //获取最新（理论上三个里面只会剩下一条）
        if(query.getLatest()){
            detailWrapper.eq(QuoteOpportunitiesDetail::getStatus,VersionStatus.ACTIVE.getCode());
            roughWrapper.eq(QuoteOpportunitiesRough::getStatus,VersionStatus.ACTIVE.getCode());
            unableWrapper.eq(QuoteOpportunitiesUnable::getStatus,VersionStatus.ACTIVE.getCode());
        }

        //详细
        List<QuoteOpportunitiesDetail> quoteOpportunitiesDetailList = detailMapper
                .selectList(detailWrapper);
        if(CollUtil.isNotEmpty(quoteOpportunitiesDetailList)){
            resultVersionList.addAll(
            quoteOpportunitiesDetailList.stream()
                    .map(item -> OpportunitiesDetailVersionVO.builder()
                            .id(item.getId())
                            .opportunitiesId(item.getOpportunitiesId())
                            .versionType(QuoteType.COST.getCode())
                            .amount(item.getAmount())
                            .date(new SimpleDateFormat("yyyy-MM-dd").format(item.getCreateTime()))
                            .createTime(item.getCreateTime())
                            .createBy(item.getCreateBy())
                            .version(item.getValuationVersion())
                            .valuationVersion(QuoteType.COST.getInfo() + item.getValuationVersion())
                            .build())
                    .collect(Collectors.toList()));
        }

        //粗略
        List<QuoteOpportunitiesRough> quoteOpportunitiesRoughList = roughMapper
                .selectList(roughWrapper);
        if(CollUtil.isNotEmpty(quoteOpportunitiesRoughList)){
            resultVersionList.addAll(quoteOpportunitiesRoughList.stream()
                    .map(item -> OpportunitiesDetailVersionVO.builder()
                            .id(item.getId())
                            .opportunitiesId(item.getOpportunitiesId())
                            .versionType(QuoteType.ROUGH.getCode())
                            .amount(item.getAmount())
                            .date(new SimpleDateFormat("yyyy-MM-dd").format(item.getCreateTime()))
                            .createTime(item.getCreateTime())
                            .createBy(item.getCreateBy())
                            .version(item.getValuationVersion())
                            .valuationVersion(QuoteType.ROUGH.getInfo() + item.getValuationVersion())
                            .build())
                    .collect(Collectors.toList()));
        }

        //无法
        List<QuoteOpportunitiesUnable> unableList = unableMapper.selectList(unableWrapper);
        if(CollUtil.isNotEmpty(unableList)){
            resultVersionList.addAll(unableList.stream()
                    .map(item -> OpportunitiesDetailVersionVO.builder()
                            .id(item.getId())
                            .opportunitiesId(item.getOpportunitiesId())
                            .versionType(QuoteType.INCAPABLE.getCode())
                            .amount(BigDecimal.ZERO)
                            .date(new SimpleDateFormat("yyyy-MM-dd").format(item.getCreateTime()))
                            .createTime(item.getCreateTime())
                            .createBy(item.getCreateBy())
                            .version(item.getValuationVersion())
                            .valuationVersion(QuoteType.INCAPABLE.getInfo() + item.getValuationVersion())
                            .build())
                    .collect(Collectors.toList()));
        }

        Map<Long,SysUser> userMap = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                        .isNotNull(SysUser::getUserId))
                .stream().collect(Collectors.toMap(SysUser::getUserId,x -> x));
        //Map<Long,QuoteOpportunities> quoteMap = quoteList.stream().collect(Collectors.toMap(QuoteOpportunities::getId,x -> x));

        resultVersionList.forEach(v -> {
            SysUser user = MapUtil.getMapValue(userMap,Long.valueOf(v.getCreateBy()));
            if(ObjectUtil.isNotEmpty(user)){
                v.setPreSaleName(user.getNickName());
            }
        });

        return resultVersionList.stream()
                .sorted(Comparator.comparing(OpportunitiesDetailVersionVO::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<OpportunitiesSalesVersionVO> getSalesVersionList(QuoteOpportunitiesRoughQuery query) {
        List<OpportunitiesSalesVersionVO> list = new LinkedList<>();

        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }
        //当前quote
        QuoteOpportunities current = baseMapper.selectById(query.getOpportunitiesId());
        if(ObjectUtil.isEmpty(current)){
            throw new RuntimeException("无效的报价信息");
        }
        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = getAboutQuoteList(query.getOpportunitiesId());
        Set<Long> quoteIdLIst = aboutQuoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        QuoteOpportunities nextQuote = aboutQuoteList.stream()
                .filter(x -> x.getCreateTime().compareTo(current.getCreateTime()) > 0)
                .sorted(Comparator.comparing(QuoteOpportunities::getCreateTime))
                .findFirst().orElse(null);

        LambdaQueryWrapper<QuotePresaleInfo> wrapper = Wrappers.<QuotePresaleInfo>lambdaQuery()
                .in(QuotePresaleInfo::getOpportunitiesId,quoteIdLIst)
                .orderByDesc(QuotePresaleInfo::getCreateTime);

        if(Boolean.TRUE.equals(query.getLatest())){
            wrapper.eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode());
        }

        //数据隔离
        //如果是管理员不筛
        if(query.getIsolation() && !currentUser.isAdmin()){
            //如果没有下一个，说明这个就是最新的,就不用筛了
            if(ObjectUtil.isNotEmpty(nextQuote)){
                wrapper.lt(QuotePresaleInfo::getCreateTime,nextQuote.getCreateTime());
            }
        }

        List<QuotePresaleInfo> presaleInfoList = presaleInfoMapper.selectList(wrapper);
        for (QuotePresaleInfo info: presaleInfoList) {
            OpportunitiesSalesVersionVO vo = BeanUtil.toBean(info,OpportunitiesSalesVersionVO.class);

            vo.setDate(info.getCreateTime());

            switch (QuoteType.valueOf(info.getType())){
                case COST:
                case ROUGH:
                    //报价金额(不包含外采硬件)
                    vo.setAmountExcl(
                            DecimalUtil.addAll(
                                    info.getPreExtQuote(),info.getDevExtQuote(),info.getProdExtQuote(),
                                    info.getOtherExtQuote(), info.getSelfExtQuote(),info.getImpExtQuote()
                            )
                    );
                    //报价金额(包含外采硬件)
                    vo.setAmountIncl(
                            DecimalUtil.addAll(vo.getAmountExcl(),info.getExtExtQuote())
                    );
                    break;
                case INCAPABLE:
                    //报价金额(包含外采硬件)
                    vo.setAmountIncl(info.getUnableQuoteAmount());
                    break;
                default:
                    break;
            }

            list.add(vo);
        }
        return list;
    }

    @Override
    public List<OpportunitiesSignVersionVO> getSignVersionList(QuoteOpportunitiesRoughQuery query) {
        List<OpportunitiesSignVersionVO> list = new LinkedList<>();

        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }
        //当前quote
        QuoteOpportunities current = baseMapper.selectById(query.getOpportunitiesId());
        if(ObjectUtil.isEmpty(current)){
            throw new RuntimeException("无效的报价信息");
        }
        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = getAboutQuoteList(query.getOpportunitiesId());
        Set<Long> quoteIdLIst = aboutQuoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());

        LambdaQueryWrapper<QuoteSignInfo> wrapper = Wrappers.<QuoteSignInfo>lambdaQuery()
                .in(QuoteSignInfo::getOpportunitiesId,quoteIdLIst)
                .orderByDesc(QuoteSignInfo::getCreateTime);

        QuoteOpportunities nextQuote = aboutQuoteList.stream()
                .filter(x -> x.getCreateTime().compareTo(current.getCreateTime()) > 0)
                .sorted(Comparator.comparing(QuoteOpportunities::getCreateTime))
                .findFirst().orElse(null);
        //数据隔离
        //如果是管理员不筛
        if(query.getIsolation() && !currentUser.isAdmin()){
            //如果没有下一个，说明这个就是最新的,就不用筛了
            if(ObjectUtil.isNotEmpty(nextQuote)){
                wrapper.lt(QuoteSignInfo::getCreateTime,nextQuote.getCreateTime());
            }
        }

        if(query.getLatest()){
            wrapper.eq(QuoteSignInfo::getStatus,VersionStatus.ACTIVE.getCode());
        }

        List<QuoteSignInfo> quoteSignInfoList = signInfoMapper.selectList(wrapper);

        if (CollUtil.isEmpty(quoteSignInfoList)){
            return new LinkedList<>();
        }

        quoteSignInfoList.forEach(x ->
        {
            OpportunitiesSignVersionVO vo = BeanUtil.toBean(x,OpportunitiesSignVersionVO.class);
            vo.setDate(x.getCreateTime());
            //报价金额(不包含外采硬件)
            vo.setAmountExcl(
                    x.getNorthQuoteAmount()
            );
            //报价金额(包含外采硬件)
            vo.setAmountIncl(
                    x.getNorthAmount()
            );
            //两个利润率
            vo.setProjProfitRateExcl(x.getCostProfitRate());
            vo.setProjProfitRateIncl(x.getTotalCostProfitRate());

            list.add(vo);
        });

        return list;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean quoteApply(ReQuoteDTO dto) {
        QuoteOpportunities quote = baseMapper.selectById(dto.getId());
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价信息");
        }
        QuoteOpportunitiesParent parent = parentMapper.selectById(quote.getOpportunitiesParentId());
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();

        //是否换了人
        Boolean updatePerson = true;
        List<String> preCrmList = dto.getPreSaleIdList().stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        List<String> oldUserCrmList = StrUtil.splitList(quote.getSupportPerson(),",").stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        if(preCrmList.containsAll(oldUserCrmList) && oldUserCrmList.containsAll(preCrmList)){
            updatePerson = false;
        }

        //如果换人了，就把旧的改成废弃，造一个新的
        if(updatePerson){
            List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                    .in(SysUser::getCrmUserId,preCrmList));

            QuoteOpportunities newQuote = new QuoteOpportunities();
            newQuote.setOpportunitiesParentId(quote.getOpportunitiesParentId());
            newQuote.setSupportCrmId(quote.getSupportCrmId());
            newQuote.setSupportType(quote.getSupportType());
            newQuote.setSupportIdentification(quote.getSupportIdentification());
            newQuote.setStatus(QuoteStatusEnum.UPDATE_PENDING.getCode());
            newQuote.setSupportPerson(String.join(",",preCrmList));
            newQuote.setSupportName(String.join(",",users.stream().map(x -> x.getNickName()).collect(Collectors.toList())));
            newQuote.setCreateTime(DateTime.now());
            newQuote.setUpdateBy(currentUser.getCrmUserId());
            newQuote.setUpdateTime(DateTime.now());
            if(preCrmList.size() > 1){
                newQuote.setQuoteMethod(QuoteMethod.DEFAULT.getCode());
            }else {
                newQuote.setQuoteMethod(QuoteMethod.SINGLE.getCode());
                newQuote.setQuoters(preCrmList.get(0));
            }
            newQuote.setType(quote.getType());
            newQuote.setProcessCategory(QuoteProcessCategory.NORMAL.getCode());
            newQuote.setAmount(quote.getAmount());
            newQuote.setQuoteDesc(dto.getQuoteDesc());

            //把老数据状态改为废弃
            baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getSupportCrmId,quote.getSupportCrmId())
                    .set(QuoteOpportunities::getStatus,QuoteStatusEnum.ABANDON.getCode())
            );
            //修改上一条数据的更新时间，废弃原因
            String abandonDesc = "所属售前更换为 " + newQuote.getSupportName();
            baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getId,quote.getId())
                    .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                    .set(QuoteOpportunities::getAbandonDesc,abandonDesc)

            );
            baseMapper.insert(newQuote);
        }else {
            //如果没换人，就把状态改为为初始状态
            LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getId,quote.getId())
                    .set(QuoteOpportunities::getStatus,QuoteStatusEnum.UPDATE_PENDING.getCode())
                    .set(QuoteOpportunities::getSaleAuditLog,"")
                    .set(QuoteOpportunities::getSaleAuditTime,null)
                    .set(QuoteOpportunities::getSignAuditTime, null)
                    .set(QuoteOpportunities::getSignAuditLog, "")
                    .set(QuoteOpportunities::getNonQuoters, "")
                    .set(QuoteOpportunities::getQuoteDesc, dto.getQuoteDesc())
                    .set(QuoteOpportunities::getProcessCategory, QuoteProcessCategory.NORMAL.getCode())
                    .set(QuoteOpportunities::getCreateTime, DateTime.now())
                    .set(QuoteOpportunities::getCreateBy, parent.getSaleId())
                    .set(QuoteOpportunities::getUpdateTime, DateTime.now())
                    .set(QuoteOpportunities::getUpdateBy, currentUser.getUserId());

            if(preCrmList.size() > 1){
                updateWrapper.set(QuoteOpportunities::getQuoteMethod,QuoteMethod.DEFAULT.getCode());
                updateWrapper.set(QuoteOpportunities::getQuoters,"");
            }else {
                updateWrapper.set(QuoteOpportunities::getQuoteMethod,QuoteMethod.SINGLE.getCode());
                updateWrapper.set(QuoteOpportunities::getQuoters,preCrmList.get(0));
            }
            baseMapper.update(null,updateWrapper);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRequired(ReQuoteDTO dto) {
        QuoteOpportunities quote = baseMapper.selectById(dto.getId());
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价信息");
        }
        QuoteOpportunitiesParent parent = parentMapper.selectById(quote.getOpportunitiesParentId());
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();

        //是否换了人
        Boolean updatePerson = true;

        List<String> preCrmList = dto.getPreSaleIdList().stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        List<String> oldUserCrmList = StrUtil.splitList(quote.getSupportPerson(),",").stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        if(preCrmList.containsAll(oldUserCrmList) && oldUserCrmList.containsAll(preCrmList)){
            updatePerson = false;
        }

        //如果换人了，就把旧的改成废弃，造一个新的
        if(updatePerson){
            List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                    .in(SysUser::getCrmUserId,preCrmList));

            QuoteOpportunities newQuote = new QuoteOpportunities();
            newQuote.setOpportunitiesParentId(quote.getOpportunitiesParentId());
            newQuote.setSupportCrmId(quote.getSupportCrmId());
            newQuote.setSupportType(quote.getSupportType());
            newQuote.setSupportIdentification(quote.getSupportIdentification());
            newQuote.setStatus(QuoteStatusEnum.UPDATE_PENDING.getCode());
            newQuote.setSupportPerson(String.join(",",preCrmList));
            newQuote.setSupportName(String.join(",",users.stream().map(x -> x.getNickName()).collect(Collectors.toList())));
            newQuote.setCreateTime(DateTime.now());
            newQuote.setUpdateBy(currentUser.getCrmUserId());
            newQuote.setUpdateTime(DateTime.now());

            if(preCrmList.size() > 1){
                newQuote.setQuoteMethod(QuoteMethod.DEFAULT.getCode());
            }else {
                newQuote.setQuoteMethod(QuoteMethod.SINGLE.getCode());
                newQuote.setQuoters(preCrmList.get(0));
            }
            newQuote.setType(quote.getType());
            newQuote.setProcessCategory(QuoteProcessCategory.UPDATE_REQUIRED.getCode());
            newQuote.setAmount(quote.getAmount());

            //把老数据状态改为废弃
            baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getSupportCrmId,quote.getSupportCrmId())
                    .set(QuoteOpportunities::getStatus,QuoteStatusEnum.ABANDON.getCode())
            );
            //修改上一条数据的更新时间，废弃原因
            String abandonDesc = "所属售前更换为 " + newQuote.getSupportName();
            baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getId,quote.getId())
                    .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                    .set(QuoteOpportunities::getAbandonDesc,abandonDesc)

            );

            baseMapper.insert(newQuote);
        }else {
            //如果没换人，就把状态改为为初始状态
            LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getId,quote.getId())
                    .set(QuoteOpportunities::getStatus,QuoteStatusEnum.UPDATE_PENDING.getCode())
                    .set(QuoteOpportunities::getSaleAuditLog,"")
                    .set(QuoteOpportunities::getSaleAuditTime,null)
                    .set(QuoteOpportunities::getSignAuditTime, null)
                    .set(QuoteOpportunities::getSignAuditLog, "")
                    .set(QuoteOpportunities::getNonQuoters, "")
                    .set(QuoteOpportunities::getProcessCategory, QuoteProcessCategory.UPDATE_REQUIRED.getCode())
                    .set(QuoteOpportunities::getCreateTime, DateTime.now())
                    .set(QuoteOpportunities::getCreateBy, parent.getSaleId())
                    .set(QuoteOpportunities::getUpdateTime, DateTime.now())
                    .set(QuoteOpportunities::getUpdateBy, currentUser.getUserId());
            if(preCrmList.size() > 1){
                updateWrapper.set(QuoteOpportunities::getQuoteMethod,QuoteMethod.DEFAULT.getCode());
                updateWrapper.set(QuoteOpportunities::getQuoters,"");
            }else {
                updateWrapper.set(QuoteOpportunities::getQuoteMethod,QuoteMethod.SINGLE.getCode());
                updateWrapper.set(QuoteOpportunities::getQuoters,preCrmList.get(0));
            }
            baseMapper.update(null,updateWrapper);
        }
        return true;
    }

    @Override
    public Boolean quoteBySelf(QuoteOpportunitiesUpdateDTO dto)
    {
        QuoteOpportunities quote = baseMapper.selectById(dto.getOpportunitiesId());
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价信息");
        }
        //如果是多人报价，则无法更改
        if(QuoteMethod.MULTIPLE.getCode().equals(quote.getQuoteMethod())){
            throw new RuntimeException("当前是多人报价，无法更改为单人报价");
        }
        //本报价的所有售前CrmId
        List<String> personCrmIdList = StrUtil.splitList(quote.getSupportPerson(),",");
        if(CollUtil.isEmpty(personCrmIdList)){
            throw new RuntimeException("无效的售前信息");
        }
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }

        if(ObjectUtil.isNotEmpty(quote.getQuoters())){
            throw new RuntimeException("已有人报价");
        }

        //otherQuoterList 不可能为空
        List<String> otherQuoterList = personCrmIdList.stream().filter(x -> !x.equals(currentUser.getCrmUserId())).collect(Collectors.toList());

        //更新操作，报价方式设为单人报价；报价人设为当前人；把其他人都设成不报价的人
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getQuoteMethod,QuoteMethod.SINGLE.getCode())
                .set(QuoteOpportunities::getQuoters,currentUser.getCrmUserId())
                .set(QuoteOpportunities::getNonQuoters,String.join(",",otherQuoterList))
        );

        return true;
    }

    @Override
    public Boolean quoteByOthers(QuoteOpportunitiesUpdateDTO dto) {
        QuoteOpportunities quote = baseMapper.selectById(dto.getOpportunitiesId());
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价信息");
        }
        //如果是多人报价，则无法更改
        if(QuoteMethod.MULTIPLE.getCode().equals(quote.getQuoteMethod())){
            throw new RuntimeException("当前是多人报价，无法更改为单人报价");
        }
        //本报价的所有售前CrmId
        List<String> personIdList = StrUtil.splitList(quote.getSupportPerson(),",");
        if(CollUtil.isEmpty(personIdList)){
            throw new RuntimeException("无效的售前信息");
        }
        //personCrmIdList 必包含 当前登录人，不然就报错
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();

        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }

        /// 新的不报价人列表
        StringBuilder newNonQuoters = new StringBuilder();
        // 如果已有不报价人列表，添加到新列表中
        if (ObjectUtil.isNotEmpty(quote.getNonQuoters())) {
            newNonQuoters.append(quote.getNonQuoters()).append(",");
        }
        // 获取同部门用户的CrmUserId列表并加入不报价人列表
        String commentDeptCrmUserId = sysUserMapper.selectList(
                        Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, currentUser.getDeptId())
                ).stream()
                .map(SysUser::getCrmUserId)
                .collect(Collectors.joining(","));

        if (!commentDeptCrmUserId.isEmpty()) {
            newNonQuoters.append(commentDeptCrmUserId);
        }

        //更新操作，报价方式设为单人报价；把当然操作人塞进不报价列表里
        LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getQuoteMethod,QuoteMethod.SINGLE.getCode())
                .set(QuoteOpportunities::getNonQuoters,newNonQuoters.toString());

        if(currentUser.isAdmin()){
            //如果是管理员，则直接扔到不报价列表里，无事发生
        }else {
            //如果不是管理员，需要判断
            //if(quote.getSupportPerson().contains(currentUser.getCrmUserId())){
            //报价列表
            List<String> quoterList = StrUtil.splitList(quote.getQuoters(),",");
            //不报价列表
            List<String> nonQuoterList = StrUtil.splitList(newNonQuoters.toString(),",");
            //已选择过的人
            List<String> completeList = new LinkedList<>();
            completeList.addAll(quoterList);
            completeList.addAll(nonQuoterList);
            completeList.add(currentUser.getCrmUserId());
            //没选择过的人(已选择里没有的人)
            List<String> defaultList = personIdList.stream().filter(p -> !completeList.contains(p)).collect(Collectors.toList());
            //自己选完后只剩最后一人的情况
            if(defaultList.size() == 1){
                //如果还是没人报
                if(quoterList.size() == 0){
                    updateWrapper.set(QuoteOpportunities::getQuoters,defaultList.get(0));
                }
                //理论上不可能有人报，因为前面的代码里一旦有人报，别人就都进不报价列表里了
            }
            //}else {
                //如果这个人不是管理员，还不属于这几个售前，那没有权限(理论上没这种情况，因为前端已经堵死了)
                //throw new  RuntimeException("无权限");
            //}
        }
        //除了当前人外的所有人

        /*List<String> quoterList = StrUtil.splitList(quote.getQuoters(),",");
        List<String> nonQuoterList = StrUtil.splitList(quote.getNonQuoters(),",");*/
        //如果其他人都在不报价列表，且报价人为空，且当前登录人属于售前支持人员列表时，报错
        /*if(nonQuoterList.containsAll(otherQuoterList) && ObjectUtil.isEmpty(quote.getQuoters()) && personIdList.contains(currentUser.getCrmUserId())){
            throw new RuntimeException("其他售前没有报价！");
        }*/


        //List<String>

        baseMapper.update(null,updateWrapper);

        return true;
    }

    @Override
    public String selectQuoteSalePhone(Long opportunitiesId) {
        return baseMapper.selectQuoteSalePhone(opportunitiesId);
    }

    @Override
    @DSTransactional()
    public Boolean lose(QuoteOpportunitiesUpdateDTO dto) {
        QuoteOpportunities quote = baseMapper.selectById(dto.getId());
        if(ObjectUtil.isEmpty(quote)){
            return false;
        }

        LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                .eq(QuoteOpportunities::getId,quote.getId())
                .set(QuoteOpportunities::getStatus,QuoteStatusEnum.LOSE_PENDING_APPROVAL.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getLoseTime,DateTime.now())
                .set(QuoteOpportunities::getReasonDesc,dto.getReasonDesc());

        baseMapper.update(null,updateWrapper);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean salesApproval(ApprovalDTO dto) {
        //修改审核状态和审核时间
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.SALE_QUOTE.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getSaleAuditTime, DateTime.now())
                .set(QuoteOpportunities::getSaleAuditLog,"批准")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));
        //修改子表里的审核理由
        presaleInfoMapper.update(null,Wrappers.<QuotePresaleInfo>lambdaUpdate()
                .set(QuotePresaleInfo::getSaleAuditDesc,dto.getAuditDesc())
                .eq(QuotePresaleInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        //发送邮件
        ThreadUtil.runInThread(() -> {
            approvalEmail("销售报价审批已通过", dto, EmailTypeEnum.QUOTATION_APPROVAL_RESULT_NOTIFICATION.getCode());
        });

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean salesReject(ApprovalDTO dto) {
        //修改审核状态和审核时间
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.SALE_REJECTED.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getSaleAuditTime, DateTime.now())
                .set(QuoteOpportunities::getSaleAuditLog,"拒绝")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));
        //修改子表里的审核理由
        presaleInfoMapper.update(null,Wrappers.<QuotePresaleInfo>lambdaUpdate()
                .set(QuotePresaleInfo::getSaleAuditDesc,dto.getAuditDesc())
                .eq(QuotePresaleInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        //发送邮件
        ThreadUtil.runInThread(() -> {
            approvalEmail(QuoteStatusEnum.SALE_REJECTED.getInfo(), dto, EmailTypeEnum.QUOTATION_APPROVAL_RESULT_NOTIFICATION.getCode());
        });
        return true;
    }

    /**
     * 签约审批通过
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean signApproval(ApprovalDTO dto) {
        //修改审核状态和审核时间
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.WAIT_SIGN.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getSignAuditTime, DateTime.now())
                .set(QuoteOpportunities::getSignAuditLog,"批准")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));
        //修改子表里的审核理由
        signInfoMapper.update(null,Wrappers.<QuoteSignInfo>lambdaUpdate()
                .set(QuoteSignInfo::getSignAuditDesc,dto.getAuditDesc())
                .eq(QuoteSignInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuoteSignInfo::getStatus, VersionStatus.ACTIVE.getCode()));
        //发送邮件
        ThreadUtil.runInThread(() -> {
            approvalEmail("签约审批已通过", dto, EmailTypeEnum.SIGNING_APPROVAL_RESULT_NOTIFICATION.getCode());
        });

        return true;
    }

    /**
     * 签约审批驳回
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean signReject(ApprovalDTO dto) {
        //修改审核状态和审核时间
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.SIGN_REJECTED.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getSignAuditTime, DateTime.now())
                .set(QuoteOpportunities::getSignAuditLog,"拒绝")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));
        //修改子表里的审核理由
        signInfoMapper.update(null,Wrappers.<QuoteSignInfo>lambdaUpdate()
                .set(QuoteSignInfo::getSignAuditDesc,dto.getAuditDesc())
                .eq(QuoteSignInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuoteSignInfo::getStatus, VersionStatus.ACTIVE.getCode()));
        //发送邮件
        ThreadUtil.runInThread(() -> {
            approvalEmail("销售报价审批已通过", dto, EmailTypeEnum.SIGNING_APPROVAL_RESULT_NOTIFICATION.getCode());
        });


        return true;
    }

    /**
     * 获取当前最新的version
     * @param supportCrmId
     * @return
     */
    @Override
    public String getCurrentVersion(String supportCrmId) {
        List<QuoteOpportunities> quoteList = baseMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getSupportCrmId,supportCrmId));
        if(CollUtil.isEmpty(quoteList)){
            throw new RuntimeException("无效的报价信息");
        }
        Set<Long> quoteIdLIst = quoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        QuoteOpportunitiesDetail detail = detailMapper.selectList(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                        .in(QuoteOpportunitiesDetail::getOpportunitiesId,quoteIdLIst)
                        .isNotNull(QuoteOpportunitiesDetail::getValuationVersion)
                        .orderByDesc(QuoteOpportunitiesDetail::getCreateTime))
                .stream().findFirst().orElse(null);
        QuoteOpportunitiesRough rough = roughMapper.selectList(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                        .in(QuoteOpportunitiesRough::getOpportunitiesId,quoteIdLIst)
                        .isNotNull(QuoteOpportunitiesRough::getValuationVersion)
                        .orderByDesc(QuoteOpportunitiesRough::getCreateTime))
                .stream().findFirst().orElse(null);
        QuoteOpportunitiesUnable unable = unableMapper.selectList(Wrappers.<QuoteOpportunitiesUnable>lambdaQuery()
                        .in(QuoteOpportunitiesUnable::getOpportunitiesId,quoteIdLIst)
                        .isNotNull(QuoteOpportunitiesUnable::getValuationVersion)
                        .orderByDesc(QuoteOpportunitiesUnable::getCreateTime))
                .stream().findFirst().orElse(null);
        List<BaseEntity> list = Arrays.asList(detail,rough,unable)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        String currentVersion = "";

        if(CollUtil.isNotEmpty(list)){
            BaseEntity current = list.stream().sorted(Comparator.comparing(BaseEntity::getCreateTime).reversed())
                    .findFirst().get();
            if(current instanceof QuoteOpportunitiesDetail){
                currentVersion = ((QuoteOpportunitiesDetail)current).getValuationVersion();
            }
            else if (current instanceof QuoteOpportunitiesRough) {
                currentVersion = ((QuoteOpportunitiesRough)current).getValuationVersion();
            }else {
                currentVersion = ((QuoteOpportunitiesUnable)current).getValuationVersion();
            }
        }

        return currentVersion;
    }

    /**
     * 修改所有成本报价版本的状态
     * @param idList
     * @param status
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAllVersionStatus(List<Long> idList, VersionStatus status) {
        unableMapper.update(null,Wrappers.<QuoteOpportunitiesUnable>lambdaUpdate()
                .set(QuoteOpportunitiesUnable::getStatus,status.getCode())
                .in(QuoteOpportunitiesUnable::getOpportunitiesId,idList));
        roughMapper.update(null,Wrappers.<QuoteOpportunitiesRough>lambdaUpdate()
                .set(QuoteOpportunitiesRough::getStatus,status.getCode())
                .in(QuoteOpportunitiesRough::getOpportunitiesId,idList));
        //暂存的不修改状态，让它继续暂存
        detailMapper.update(null,Wrappers.<QuoteOpportunitiesDetail>lambdaUpdate()
                .set(QuoteOpportunitiesDetail::getStatus,status.getCode())
                .ne(QuoteOpportunitiesDetail::getStatus,VersionStatus.TEMPORARY.getCode())
                .in(QuoteOpportunitiesDetail::getOpportunitiesId,idList));
    }

    @Override
    public void setAllVersionStatusByParent(Long parentId, VersionStatus status)
    {
        List<QuoteOpportunities> aboutQuoteList = baseMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getOpportunitiesParentId,parentId));
        if(CollUtil.isNotEmpty(aboutQuoteList)){
            List<Long> idList = aboutQuoteList.stream().map(QuoteOpportunities::getId).collect(Collectors.toList());
            setAllVersionStatus(idList,status);
        }
    }
    @Override
    public void setAllVersionStatusByCrm(String crmId, VersionStatus status)
    {
        List<QuoteOpportunities> aboutQuoteList = baseMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getSupportCrmId,crmId));
        if(CollUtil.isNotEmpty(aboutQuoteList)){
            List<Long> idList = aboutQuoteList.stream().map(QuoteOpportunities::getId).collect(Collectors.toList());
            setAllVersionStatus(idList,status);
        }
    }


    private BigDecimal getValue(BigDecimal b){
        return b == null ? BigDecimal.ZERO : b;
    }
    private String getStr(Object o){
        return ObjectUtil.isEmpty(o) ? EmptyStr : o.toString();
    }


    /**
     * 审批邮件发送
     *
     * @param approvalResult 审批结果
     * @param approvalDTO    审批参数
     */
    private void approvalEmail(String approvalResult, ApprovalDTO approvalDTO, String approvalType) {

        // 发送邮件
        log.info("----------开始发送报价审批结果通知邮件----------");
        SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
        EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
        EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
        List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
        QuoteOpportunities quoteOpportunities = quoteOpportunitiesMapper.selectById(approvalDTO.getOpportunitiesId());
        if (ObjectUtil.isNotEmpty(quoteOpportunities)) {
            emailSubjectInfoConvertDTO.setSignApprovalResult(approvalResult);
            emailSubjectInfoConvertDTO.setApprovalResult(approvalResult);
            emailSubjectInfoConvertDTO.setCurrentDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
            // 获取所属销售
            QuoteOpportunitiesParent quoteOpportunitiesParent = quoteOpportunitiesParentMapper.selectById(quoteOpportunities.getOpportunitiesParentId());
            if (ObjectUtil.isNotEmpty(quoteOpportunitiesParent)) {
                sysUserList.stream().filter(sysUser -> sysUser.getUserId().equals(quoteOpportunitiesParent.getSaleId())).findFirst().ifPresent(sendEmailInfoDTO::setSysUser);
                // 设置商机主题
                emailSubjectInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
                emailModelInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
            }
            // 设置模版信息
            emailModelInfoConvertDTO.setCostTotal(DecimalUtil.getStr(approvalDTO.getTotalCost(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setSaleQuote(DecimalUtil.getStr(approvalDTO.getNorthAmount(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setProfitRate(DecimalUtil.getStr(approvalDTO.getCostProfitRate(),DecimalUtil.displayScale,true));
            emailModelInfoConvertDTO.setApprovalResult(approvalResult);
            emailModelInfoConvertDTO.setSignApprovalResult(approvalResult);
            emailModelInfoConvertDTO.setQuoteSystemLink(systemConfig.getDomainName());

            sendEmailInfoDTO.setEmailModelInfoConvertDTO(emailModelInfoConvertDTO);
            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(emailSubjectInfoConvertDTO);
            quoteMailUtil.sendEmail(Collections.singletonList(sendEmailInfoDTO), approvalType);
            log.info("----------报价审批结果通知邮件发送完毕----------");
        }else{
            log.error("----------商机不存在,邮件发送失败----------");
        }

    }

    @Override
    public void fillAuthInfo(SysUser currentUser, PageParams<QuoteOpportunitiesQuery> params) {
        /**
         * 1 全部数据权限
         * 2 自定数据权限
         * 3 部门数据权限
         * 4 部门及以下数据权限
         * 5 仅本人数据权限
         */
        String dataScopeAll = "1";
        String dataScopeCustom = "2";
        String dataScopeDept = "3";
        String dataScopeDeptAndChild = "4";
        String dataScopeSelf = "5";

        List<String> scopeCustomIds = new ArrayList<String>();
        currentUser.getRoles().forEach(role -> {
            if (dataScopeCustom.equals(role.getDataScope())) {
                scopeCustomIds.add(Convert.toStr(role.getRoleId()));
            }
        });
        List<SysRole> support = sysUserMapper.getRole(currentUser.getUserId());

        for (SysRole role : currentUser.getRoles()) {
            String dataScope = role.getDataScope();
            if (dataScopeAll.equals(dataScope)) {
                break;
            } else if (dataScopeCustom.equals(dataScope)) {
                if (scopeCustomIds.size() > 1) {
                    if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.PRESALE.getCode()))){
                        List<String> crmUserIdList = sysUserMapper.selectRoleUserBathListByDeptId(scopeCustomIds)
                                .stream().map(SysUser::getCrmUserId).collect(Collectors.toList());
                        params.getModel().getSupportPerson().addAll(crmUserIdList);
                    }else if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.SALE_SUPPORT.getCode()))){
                        List<String> userIdList = sysUserMapper.selectRoleUserBathListByDeptId(scopeCustomIds).stream()
                                .map(item -> String.valueOf(item.getUserId()))
                                .collect(Collectors.toList());
                        params.getModel().getSalePerson().addAll(userIdList);
                    }
                } else {
                    if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.PRESALE.getCode()))){
                        List<String> crmUserIdList = sysUserMapper.selectRoleUserBathListByDeptId(Collections.singletonList(String.valueOf(role.getRoleId())))
                                .stream().map(SysUser::getCrmUserId).collect(Collectors.toList());
                        params.getModel().getSupportPerson().addAll(crmUserIdList);
                    }else if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.SALE_SUPPORT.getCode()))){
                        List<String> userIdList = sysUserMapper.selectRoleUserBathListByDeptId(Collections.singletonList(String.valueOf(role.getRoleId()))).stream()
                                .map(item -> String.valueOf(item.getUserId()))
                                .collect(Collectors.toList());
                        params.getModel().getSalePerson().addAll(userIdList);
                    }
                }

            } else if (dataScopeDept.equals(dataScope)) {
                if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.PRESALE.getCode()))){
                    List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getDeptId, currentUser.getDeptId()));
                    if(CollUtil.isNotEmpty(sysUserList)){
                        List<String> crmUserIdList = sysUserList.stream().map(SysUser::getCrmUserId).collect(Collectors.toList());
                        params.getModel().getSupportPerson().addAll(crmUserIdList);
                    }
                }else if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.SALE_SUPPORT.getCode()))){
                    List<String> userIdList = sysUserMapper.selectList(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getDeptId, currentUser.getDeptId()))
                            .stream().map(item -> String.valueOf(item.getUserId()))
                            .collect(Collectors.toList());
                    params.getModel().getSalePerson().addAll((userIdList));
                }
            } else if (dataScopeDeptAndChild.equals(dataScope)) {
                List<SysUser> sysUserList = sysUserMapper.selectRoleUserBathListByDeptIdAndChild(currentUser.getDeptId(), currentUser.getDeptId());
                if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.PRESALE.getCode()))){
                    List<String> crmUserIdList = sysUserList.stream().map(SysUser::getCrmUserId).collect(Collectors.toList());
                    params.getModel().getSupportPerson().addAll(crmUserIdList);

                }else if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.SALE_SUPPORT.getCode()))){
                    List<String> userIdList = sysUserList.stream().map(item -> String.valueOf(item.getUserId())).collect(Collectors.toList());
                    params.getModel().getSalePerson().addAll(userIdList);
                }
            } else if (dataScopeSelf.equals(dataScope)) {
                if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.PRESALE.getCode()))){
                    params.getModel().getSupportPerson().add(currentUser.getCrmUserId());
                }else if(support.stream().anyMatch(item -> item.getRoleKey().equals(RoleKeyType.SALE_SUPPORT.getCode()))){
                    params.getModel().getSalePerson().add(String.valueOf(currentUser.getUserId()));
                }
            }
        }

    }

    @Override
    public List<QuoteOpportunities> getAboutQuoteList(Long id){
        QuoteOpportunities currentQuote = quoteOpportunitiesMapper.selectById(id);
        if(ObjectUtil.isEmpty(currentQuote)){
            throw new RuntimeException("无效的报价");
        }
        List<QuoteOpportunities> aboutQuoteList = quoteOpportunitiesMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                .eq(QuoteOpportunities::getSupportCrmId,currentQuote.getSupportCrmId()));
        return aboutQuoteList;
    }

    @Override
    public QuoteIndexVO getIndexPageInfo() {
        //当前登录人
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }
        QuoteIndexVO result = new QuoteIndexVO();

        //成本区域的查询
        PageParams<QuoteOpportunitiesQuery> pageParamCost = new PageParams<QuoteOpportunitiesQuery>();
        pageParamCost.setModel(new QuoteOpportunitiesQuery());
        pageParamCost.getModel().setStatusList(Arrays.asList("1","2","3","4","5","6","7","8","9","10","12","13"));
        fillAuthInfo(currentUser,pageParamCost);
        List<QuoteOpportunitiesVO> costList = getQuoteOpportunitiesList(pageParamCost.getModel(),false);

        //待成本：00001301,1
        result.setListWithCostCount(
                costList.stream().filter(x -> "1".equals(x.getStatus()) && "00001301".equals(x.getParentStatus())).count()
        );
        //待更新：00001301,13
        result.setListAwaitUpdateCount(
                costList.stream().filter(x -> "13".equals(x.getStatus()) && "00001301".equals(x.getParentStatus())).count()
        );
        //已成本/无法, 无法:status除了1和13,parent status无要求
        result.setListWithCostedCount(
                costList.stream().filter(x -> !Arrays.asList("1","13").contains(x.getStatus()) && Arrays.asList("COST","ROUGH").contains(x.getType())).count()
        );
        result.setListWithIncapableCount(
                costList.stream().filter(x -> !Arrays.asList("1","13").contains(x.getStatus()) && QuoteType.INCAPABLE.getCode().equals(x.getType())).count()
        );
        //待签合同 5
        result.setListWithContractCount(
                costList.stream().filter(x -> QuoteStatusEnum.WAIT_SIGN.getCode().equals(x.getStatus()) && "00001301".equals(x.getParentStatus())).count()
        );

        //其它区域查询
        QuoteOpportunitiesQuery othersQuery = new  QuoteOpportunitiesQuery();
        othersQuery.setAuth(true);
        othersQuery.setParentStatusList(Arrays.asList("00001301"));
        othersQuery.setStatusList(Arrays.asList("2","3","8","4","10","9","5"));
        List<QuoteOpportunitiesVO> othersList = getQuoteOpportunitiesList(othersQuery,false);

        //待销售 2
        result.setListWithSalesCount(
                othersList.stream().filter(x -> QuoteStatusEnum.WAIT_SALE_QUOTE.getCode().equals(x.getStatus())).count()
        );
        //待报价审批 3
        result.setListWithApprovalCount(
                othersList.stream().filter(x -> QuoteStatusEnum.SALE_PENDING_APPROVAL.getCode().equals(x.getStatus())).count()
        );
        //报价审批未通过 8
        result.setListWithApprovalFailCount(
                othersList.stream().filter(x -> QuoteStatusEnum.SALE_REJECTED.getCode().equals(x.getStatus())).count()
        );
        //报价单 4
        result.setListWithQuoteCount(
                othersList.stream().filter(x -> QuoteStatusEnum.SALE_QUOTE.getCode().equals(x.getStatus())).count()
        );
        //待签约审批 10
        result.setListWithSignCount(
                othersList.stream().filter(x -> QuoteStatusEnum.SIGN_PENDING_APPROVAL.getCode().equals(x.getStatus())).count()
        );
        //签约审批未通过 9
        result.setListWithSignFailCount(
                othersList.stream().filter(x -> QuoteStatusEnum.SIGN_REJECTED.getCode().equals(x.getStatus())).count()
        );


        return result;
    }

    @Override
    public Boolean loseApproval(ApprovalDTO dto) {
        //修改审核状态和审核时间
        //TODO:
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.LOST.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getLoseAuditTime, DateTime.now())
                .set(QuoteOpportunities::getLoseAuditLog,"批准")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));

        // 老丢单代码
        /*
        QuoteOpportunitiesParent quoteParent = parentMapper.selectQuoteOpportunitiesParentById(quote.getOpportunitiesParentId());
        if(ObjectUtil.isEmpty(quoteParent)){
            return false;
        }
        //把所有的version状态改成未生效
        //setAllVersionStatusByParent(quoteParent.getId(),VersionStatus.INACTIVE);

        //把这个商机的所有成本报价全部丢单(除了废弃)
        LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                .eq(QuoteOpportunities::getOpportunitiesParentId,quote.getOpportunitiesParentId())
                .ne(QuoteOpportunities::getStatus,QuoteStatusEnum.ABANDON.getCode())
                .set(QuoteOpportunities::getStatus,QuoteStatusEnum.LOST.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getLoseTime,DateTime.now())
                .set(QuoteOpportunities::getReasonDesc,dto.getReasonDesc());
        //把这个商机也丢单
        LambdaUpdateWrapper<QuoteOpportunitiesParent> parentUpdateWrapper = Wrappers.<QuoteOpportunitiesParent>lambdaUpdate()
                .eq(QuoteOpportunitiesParent::getId,quote.getOpportunitiesParentId())
                .set(QuoteOpportunitiesParent::getStatus, QuoteParentStatusEnum.DISCARD.getCode());

        baseMapper.update(null,updateWrapper);
        parentMapper.update(null,parentUpdateWrapper);

        // 同步CRM
        QuoteOpportunitiesParent quoteOpportunitiesParent = parentMapper.selectOne(Wrappers.<QuoteOpportunitiesParent>lambdaQuery().eq(QuoteOpportunitiesParent::getId, quote.getOpportunitiesParentId()));
        if(ObjectUtil.isNotEmpty(quoteOpportunitiesParent)){
            syncCrmMapper.syncQuoteStatus(BusinessOpportunityUpdateDTO.builder()
                    .id(String.valueOf(quoteOpportunitiesParent.getCrmId()))
                    .currentState(CrmQuoteStatusEnum.FAIL.getCode())
                    .build());
        }
        */
        /**
         * 发邮件
         */
        /*
        ThreadUtil.runInThread(() -> {
            log.info("----------开始发送丢单邮件----------");
            String emailType = EmailTypeEnum.LOSS_NOTIFICATION.getCode();
            QuoteEmailSetting emailSetting = quoteEmailSettingMapper
                    .selectOne(Wrappers.<QuoteEmailSetting>lambdaQuery().eq(QuoteEmailSetting::getType, emailType));
            if (ObjectUtil.isEmpty(emailSetting) || ObjectUtil.isEmpty(emailSetting.getSender())) {
                return ;
            }

            List<SendEmailInfoDTO> emailInfoDTOList = new LinkedList<>();
            SendEmailInfoDTO email = new SendEmailInfoDTO();
            email.setSysUser(sysUserMapper.selectUserById(Long.parseLong(emailSetting.getSender())));

            EmailSubjectInfoConvertDTO subject = new EmailSubjectInfoConvertDTO();
            subject.setBusinessSubject(quoteParent.getName());
            subject.setCurrentDate(DateTime.now().toDateStr());
            email.setEmailSubjectInfoConvertDTO(subject);

            EmailModelInfoConvertDTO model = new EmailModelInfoConvertDTO();
            model.setBusinessSubject(quoteParent.getName());//商机主题
            model.setPreSaleUser(quoteParent.getSaleName());//销售
            model.setPreSaleUserName(quote.getSupportName());//售前
            model.setLoseReasonDesc(dto.getReasonDesc());//丢单说明
            model.setQuoteSystemLink(systemConfig.getDomainName());//链接
            QuotePresaleInfo presaleInfo = presaleInfoMapper.selectOne(Wrappers.<QuotePresaleInfo>lambdaQuery()
                    .eq(QuotePresaleInfo::getOpportunitiesId, dto.getId())
                    .eq(QuotePresaleInfo::getStatus, VersionStatus.ACTIVE.getCode()));
            QuoteSignInfo signInfo = signInfoMapper.selectOne(Wrappers.<QuoteSignInfo>lambdaQuery()
                    .eq(QuoteSignInfo::getOpportunitiesId, dto.getId())
                    .eq(QuoteSignInfo::getStatus, VersionStatus.ACTIVE.getCode()));
            List<String> signStatusList = Arrays.asList("5", "6", "9", "10");
            if (ObjectUtil.isNotEmpty(presaleInfo)) {
                //无法报价
                if (quote.getType().equals(QuoteType.INCAPABLE.getCode())) {
                    model.setSaleQuote(DecimalUtil.getStr(presaleInfo.getUnableQuoteAmount(),DecimalUtil.displayScale));//金额
                    model.setApprovalResult(getStr(quote.getSaleAuditLog()));//审核记录
                    if (signStatusList.contains(quote.getStatus()) && ObjectUtil.isNotEmpty(signInfo)) {
                        model.setApplySignAmount(DecimalUtil.getStr(signInfo.getNorthAmount(),DecimalUtil.displayScale));//签约金额
                        model.setSignApprovalResult(getStr(quote.getSignAuditLog()));//审核记录
                    }

                } else {
                    model.setCostTotal(
                            DecimalUtil.addAll(
                                    presaleInfo.getPrePrice(), presaleInfo.getDevPrice(), presaleInfo.getProdPrice(),
                                    presaleInfo.getOtherPrice(), presaleInfo.getSelfPrice(), presaleInfo.getExtPrice(),
                                    presaleInfo.getImpPrice()
                            ).setScale(2).toString()
                    );
                    //暂时用含外采总额和利润率
                    model.setSaleQuote(
                            DecimalUtil.addAll(
                                    presaleInfo.getPreExtQuote(), presaleInfo.getDevExtQuote(), presaleInfo.getProdExtQuote(),
                                    presaleInfo.getOtherExtQuote(), presaleInfo.getSelfExtQuote(), presaleInfo.getExtExtQuote(),
                                    presaleInfo.getImpExtQuote()
                            ).setScale(2).toString()
                    );//金额
                    model.setProfitRate(DecimalUtil.getStr(presaleInfo.getProjProfitRateIncl(),DecimalUtil.displayScale,true));//利润率
                    model.setApprovalResult(getStr(quote.getSaleAuditLog()));//审核记录
                    if (signStatusList.contains(quote.getStatus()) && ObjectUtil.isNotEmpty(signInfo)) {
                        model.setApplySignAmount(DecimalUtil.getStr(signInfo.getNorthAmount(),DecimalUtil.displayScale));//签约金额
                        model.setSignProfitRate(DecimalUtil.getStr(signInfo.getCostProfitRate(),DecimalUtil.displayScale,true));//签约利润率
                        model.setSignApprovalResult(getStr(quote.getSignAuditLog()));//签约审核记录
                    }
                }
            }
            email.setEmailModelInfoConvertDTO(model);
            emailInfoDTOList.add(email);

            quoteMailUtil.sendEmail(emailInfoDTOList, emailType);
            log.info("----------丢单邮件发送完毕----------");
        });
        */

        return true;
    }

    @Override
    public Boolean loseReject(ApprovalDTO dto) {
        //修改审核状态和审核时间
        baseMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.LOSE_REJECTED.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getLoseAuditTime, DateTime.now())
                .set(QuoteOpportunities::getLoseAuditLog,"拒绝")
                .eq(QuoteOpportunities::getId,dto.getOpportunitiesId()));
        return true;
    }

    private void calcProfitRateExclToQuote(QuoteOpportunitiesVO vo,QuoteOpportunitiesDetail detail,QuoteOpportunitiesRough rough,QuotePresaleInfo info,Map<String,ManageQuote> manageQuoteMap){
        if(QuoteType.COST.getCode().equals(vo.getType())){
            //详细报价
            QuoteOpportunitiesDetailQuery detailQuery = new QuoteOpportunitiesDetailQuery();
            detailQuery.setId(detail.getId());
            QuoteOpportunitiesDetailVO detailVO = getDetailInfo(detailQuery);
            if(ObjectUtil.isEmpty(detailVO)){
                throw new RuntimeException("无效的详细报价信息");
            }
            fillCostPrice(detailVO,null,info,QuoteType.COST);
            //取整
            quoteUtil.toRoundPresale(info,RoundingMode.CEILING);
            calcProfitRateExclToPresale(info,manageQuoteMap);
            vo.setProjProfitRateExcl(info.getProjProfitRateExcl());

        } else if (QuoteType.ROUGH.getCode().equals(vo.getType())) {
            QuoteOpportunitiesRoughDetail roughDetail = roughDetailMapper
                    .selectOne(Wrappers.<QuoteOpportunitiesRoughDetail>lambdaQuery()
                            .eq(QuoteOpportunitiesRoughDetail::getRoughId,rough.getId()));
            fillCostPrice(null,roughDetail,info,QuoteType.ROUGH);
            //取整
            quoteUtil.toRoundPresale(info,RoundingMode.CEILING);
            calcProfitRateExclToPresale(info,manageQuoteMap);
            vo.setProjProfitRateExcl(info.getProjProfitRateExcl());
        }else {
            vo.setProjProfitRateExcl(BigDecimal.ZERO);
        }
    }


    private QuoteOpportunitiesDetailVO getDetailInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        LambdaQueryWrapper<QuoteOpportunitiesDetail> queryChainWrapper = new LambdaQueryWrapper<QuoteOpportunitiesDetail>();
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getId()), QuoteOpportunitiesDetail::getId, quoteOpportunitiesDetailQuery.getId());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getOpportunitiesId()), QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getOpportunitiesId());
        queryChainWrapper.in(CollUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getQuoteIdList()), QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getQuoteIdList());
        queryChainWrapper.eq(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getStatus()), QuoteOpportunitiesDetail::getStatus, quoteOpportunitiesDetailQuery.getStatus());

        QuoteOpportunitiesDetail opportunitiesDetail = detailMapper.selectOne(queryChainWrapper);
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

    private void calcProfitRateExclToPresale(QuotePresaleInfo info,Map<String, ManageQuote> manageQuoteMap){
        BigDecimal softwareTotal = DecimalUtil.addAll(info.getPrePrice(),info.getDevPrice(),info.getProdPrice(),info.getOtherPrice());
        //项目利润率(不含外采硬件)
        if(DecimalUtil.addAll(softwareTotal,info.getSelfPrice(),info.getImpPrice()).compareTo(BigDecimal.ZERO) == 0){
            info.setProjProfitRateExcl(BigDecimal.ZERO);
        }
        else {
            //拆成6个，用各自的税率算
            info.setProjProfitRateExcl(
                    (
                            info.getPreExtQuote().divide(manageQuoteMap.get(RealQuoteType.SALES_SUPPORT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getPrePrice())
                                    .add(
                                            info.getDevExtQuote().divide(manageQuoteMap.get(RealQuoteType.SOFTWARE_DEVELOPMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getDevPrice())
                                    )
                                    .add(
                                            info.getProdExtQuote().divide(manageQuoteMap.get(RealQuoteType.PRODUCT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getProdPrice())
                                    )
                                    .add(
                                            info.getOtherExtQuote().divide(manageQuoteMap.get(RealQuoteType.OTHER.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getOtherPrice())
                                    )
                                    .add(
                                            info.getSelfExtQuote().divide(manageQuoteMap.get(RealQuoteType.SELF_DEVELOPED.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getSelfPrice())
                                    )
                                    .add(
                                            info.getImpExtQuote().divide(manageQuoteMap.get(RealQuoteType.IMPLEMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(info.getImpPrice())
                                    )
                    ).divide(
                            DecimalUtil.addAll(softwareTotal,info.getSelfPrice(),info.getImpPrice()),DecimalUtil.defaultScale,RoundingMode.HALF_UP
                    )
            );
        }
    }

    private void fillCostPrice(QuoteOpportunitiesDetailVO detailInfo,QuoteOpportunitiesRoughDetail roughDetail,QuotePresaleInfo info,QuoteType type){
        info.setPrePrice(BigDecimal.ZERO);
        info.setDevPrice(BigDecimal.ZERO);
        info.setProdPrice(BigDecimal.ZERO);
        info.setOtherPrice(BigDecimal.ZERO);
        info.setSelfPrice(BigDecimal.ZERO);
        info.setExtPrice(BigDecimal.ZERO);
        info.setImpPrice(BigDecimal.ZERO);
        if(QuoteType.COST.equals(type)){
            Map<Long, ManageProduct> productMap = null;
            Map<Long, ManageHardwareSelf> selfMap = null;
            Map<Long, ManageHardwareExt> extMap = null;
            //产品
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesProductVO()) &&
                    CollUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList())
            ){
                Set<Long> productIdList = detailInfo.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList()
                        .stream().filter(x -> ObjectUtil.isNotEmpty(x.getProductId())).map(x -> x.getProductId()).collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(productIdList)){
                    productMap = productMapper.selectBatchIds(productIdList).stream().collect(Collectors.toMap(ManageProduct::getId,x -> x));

                    info.setProdPrice(BigDecimal.ZERO);
                    Map<Long, ManageProduct> finalProductMap = productMap;
                    detailInfo.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList().forEach(p ->{
                        ManageProduct product = MapUtil.getMapValue(finalProductMap,p.getProductId());
                        if(ObjectUtil.isNotEmpty(product)){
                            //数量*单价
                            info.setProdPrice(
                                    info.getProdPrice().add(product.getCostPrice().multiply(p.getNumber()))
                            );
                        }

                    });
                }
            }
            //自研
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesSelfVO()) &&
                    CollUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList())){
                Set<Long> selfIdList = detailInfo.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList()
                        .stream().filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId())).map(x -> x.getHardwareId()).collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(selfIdList)){
                    selfMap = hardwareSelfMapper.selectBatchIds(selfIdList).stream().collect(Collectors.toMap(ManageHardwareSelf::getId,x -> x));
                    info.setSelfPrice(BigDecimal.ZERO);
                    Map<Long, ManageHardwareSelf> finalSelfMap = selfMap;
                    detailInfo.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList().forEach(s ->{
                        ManageHardwareSelf self = MapUtil.getMapValue(finalSelfMap,s.getHardwareId());
                        if(ObjectUtil.isNotEmpty(self)){
                            //数量*单价
                            info.setSelfPrice(
                                    info.getSelfPrice().add(self.getUnitPrice().multiply(s.getNumber()))
                            );
                        }
                    });

                }
            }

            //外采
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesExternalVO()) &&
                    CollUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList())
            ){
                Set<Long> extIdList = detailInfo.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList()
                        .stream().filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId())).map(x -> x.getHardwareId()).collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(extIdList)){
                    extMap = hardwareExtMapper.selectBatchIds(extIdList).stream().collect(Collectors.toMap(ManageHardwareExt::getId,x -> x));
                    info.setExtPrice(BigDecimal.ZERO);
                    Map<Long, ManageHardwareExt> finalExtMap = extMap;
                    detailInfo.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList().forEach(e ->{
                        ManageHardwareExt ext = MapUtil.getMapValue(finalExtMap,e.getHardwareId());
                        if(ObjectUtil.isNotEmpty(ext)){
                            //数量*单价
                            info.setExtPrice(
                                    info.getExtPrice().add(ext.getEstimatedCost().multiply(e.getNumber()))
                            );
                        }
                    });
                }
            }
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesSupportVO())){
                QuoteOpportunitiesSupportVO quoteOpportunitiesSupportVO = detailInfo.getQuoteOpportunitiesSupportVO();
                info.setPrePrice(getValue(quoteOpportunitiesSupportVO.getTotalCost()));
            }
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesCustomizableVo())){
                QuoteOpportunitiesCustomizableVO quoteOpportunitiesCustomizableVo = detailInfo.getQuoteOpportunitiesCustomizableVo();
                info.setDevPrice(getValue(quoteOpportunitiesCustomizableVo.getTotalSoftwareCosts()));
            }
            /*if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesProductVO())){
                QuoteOpportunitiesProductVO quoteOpportunitiesCustomizableVo = detailInfo.getQuoteOpportunitiesProductVO();
                info.setProdPrice(getValue(quoteOpportunitiesCustomizableVo.getTotalCost()));
            }*/
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesOtherVO())){
                QuoteOpportunitiesOtherVO quoteOpportunitiesOtherVO = detailInfo.getQuoteOpportunitiesOtherVO();
                info.setOtherPrice(getValue(quoteOpportunitiesOtherVO.getTotalCost()));
            }
            /*if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesSelfVO())){
                QuoteOpportunitiesSelfVO quoteOpportunitiesSelfVO = detailInfo.getQuoteOpportunitiesSelfVO();
                info.setSelfPrice(getValue(quoteOpportunitiesSelfVO.getTotalCost()));
            }*/
            /*if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesExternalVO())){
                QuoteOpportunitiesExternalVO quoteOpportunitiesExternalVO = detailInfo.getQuoteOpportunitiesExternalVO();
                info.setExtPrice(getValue(quoteOpportunitiesExternalVO.getTotalEstimatedlCost()));
            }*/
            if(ObjectUtil.isNotEmpty(detailInfo.getQuoteOpportunitiesImplement())){
                QuoteOpportunitiesImplement quoteOpportunitiesImplement = detailInfo.getQuoteOpportunitiesImplement();
                info.setImpPrice(getValue(quoteOpportunitiesImplement.getTotalCost()));
            }
        }
        else if (QuoteType.ROUGH.equals(type)) {
            if(ObjectUtil.isNotEmpty(roughDetail.getSupportAmount())){
                info.setPrePrice(getValue(roughDetail.getSupportAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getCustomAmount())){
                info.setDevPrice(getValue(roughDetail.getCustomAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getProductAmount())){
                info.setProdPrice(getValue(roughDetail.getProductAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getOtherAmount())){
                info.setOtherPrice(getValue(roughDetail.getOtherAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getSelfAmount())){
                info.setSelfPrice(getValue(roughDetail.getSelfAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getExternalAmount())){
                info.setExtPrice(getValue(roughDetail.getExternalAmount()));
            }
            if(ObjectUtil.isNotEmpty(roughDetail.getImplementAmount())){
                info.setImpPrice(getValue(roughDetail.getImplementAmount()));
            }
        }

    }

    private Boolean isMultiDept(Map<String,SysUser> userMap,String supportPerson){
        Boolean value = false;
        List<String> userCrmIdList = StrUtil.splitList(supportPerson,",");
        List<SysUser> userList = new LinkedList<>();
        userCrmIdList.forEach(crm ->{
            SysUser user = MapUtil.getMapValue(userMap,crm);
            if(ObjectUtil.isNotEmpty(user)){
                userList.add(user);
            }
        });
        Set<Long> deptIdList = userList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getDeptId())).map(x -> x.getDeptId()).collect(Collectors.toSet());
        if(deptIdList.size() > 1){
            value = true;
        }
        return value;
    }
}
