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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.util.StringUtil;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.*;
import com.internal.common.exception.ServiceException;
import com.internal.common.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageHardwareSelf;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.domain.ManageQuote;
import com.internal.manager.mapper.ManageHardwareExtMapper;
import com.internal.manager.mapper.ManageHardwareSelfMapper;
import com.internal.manager.mapper.ManageProductMapper;
import com.internal.manager.mapper.ManageQuoteMapper;
import com.internal.manager.service.IManageQuoteService;
import com.internal.manager.service.IManageWarningService;
import com.internal.quote.domain.*;
import com.internal.quote.dto.*;
import com.internal.quote.mapper.*;
import com.internal.quote.service.IQuoteOpportunitiesDetailService;
import com.internal.quote.service.IQuoteOpportunitiesRoughService;
import com.internal.quote.service.IQuoteOpportunitiesService;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.quote.util.QuoteUtil;
import com.internal.quote.vo.*;
import com.internal.system.mapper.SysUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.internal.quote.service.IQuotePresaleInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机售前报价信息Service业务层处理
 * 
 * @author internal
 * @date 2024-10-30
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuotePresaleInfoServiceImpl extends ServiceImpl<QuotePresaleInfoMapper, QuotePresaleInfo> implements IQuotePresaleInfoService
{
    private final QuotePresaleInfoMapper quotePresaleInfoMapper;
    private final QuoteOpportunitiesMapper quoteMapper;
    private final QuoteOpportunitiesProductMapper quoteProductMapper;
    private final QuoteOpportunitiesSelfMapper quoteSelfMapper;
    private final QuoteOpportunitiesExternalMapper quoteExtMapper;
    private final QuoteOpportunitiesRoughMapper quoteOpportunitiesRoughMapper;
    private final QuoteOpportunitiesDetailMapper detailMapper;
    private final QuoteOpportunitiesRoughDetailMapper quoteOpportunitiesRoughDetailMapper;
    private final IQuoteOpportunitiesDetailService quoteOpportunitiesDetailService;
    private final IQuoteOpportunitiesService quoteService;
    private final IQuoteOpportunitiesRoughService roughService;
    private final QuoteOpportunitiesUnableMapper unableMapper;
    private final ManageQuoteMapper manageQuoteMapper;
    private final ManageProductMapper productMapper;
    private final ManageHardwareSelfMapper hardwareSelfMapper;
    private final ManageHardwareExtMapper hardwareExtMapper;
    private final IManageQuoteService manageQuoteService;
    private final IManageWarningService manageWarningService;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemConfig systemConfig;
    private final QuoteMailUtil quoteMailUtil;
    private final SysUserMapper sysUserMapper;
    private final QuoteEmailSettingMapper quoteEmailSettingMapper;
    private final QuoteUtil quoteUtil;

    /**
     * 查询报价系统-商机售前报价信息
     * 
     * @param id 报价系统-商机售前报价信息主键
     * @return 报价系统-商机售前报价信息
     */
    @Override
    public QuotePresaleInfo selectQuotePresaleInfoById(Long id)
    {
        return quotePresaleInfoMapper.selectQuotePresaleInfoById(id);
    }

    /**
     * 查询报价系统-商机售前报价信息列表
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 报价系统-商机售前报价信息
     */
    @Override
    public List<QuotePresaleInfo> selectQuotePresaleInfoList(QuotePresaleInfo quotePresaleInfo)
    {
        return quotePresaleInfoMapper.selectQuotePresaleInfoList(quotePresaleInfo);
    }

    /**
     * 新增报价系统-商机售前报价信息
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 结果
     */
    @Override
    public int insertQuotePresaleInfo(QuotePresaleInfo quotePresaleInfo)
    {
        return quotePresaleInfoMapper.insert(quotePresaleInfo);
    }

    /**
     * 新增销售报价版本
     * @param saveDTO
     * @return
     */
    @Override
    public Integer addSalesQuotesVersion(QuotePresaleInfoSaveDTO saveDTO)
    {
        QuotePresaleInfo newItem = BeanUtil.toBean(saveDTO,QuotePresaleInfo.class);
        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(saveDTO.getOpportunitiesId());
        Set<Long> quoteIdLIst = aboutQuoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }

        if(QuoteType.COST.getCode().equals(quote.getType())){
            QuoteOpportunitiesDetail detail = detailMapper.selectOne(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                    .in(QuoteOpportunitiesDetail::getOpportunitiesId,quoteIdLIst)
                    .eq(QuoteOpportunitiesDetail::getStatus,VersionStatus.ACTIVE.getCode()));
            newItem.setCostId(detail.getId());
        } else if (QuoteType.ROUGH.getCode().equals(quote.getType())) {
            QuoteOpportunitiesRough rough = quoteOpportunitiesRoughMapper.selectOne(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                    .in(QuoteOpportunitiesRough::getOpportunitiesId,quoteIdLIst)
                    .eq(QuoteOpportunitiesRough::getStatus,VersionStatus.ACTIVE.getCode()));
            newItem.setCostId(rough.getId());
        }else {
            QuoteOpportunitiesUnable unable = unableMapper.selectOne(Wrappers.<QuoteOpportunitiesUnable>lambdaQuery()
                    .in(QuoteOpportunitiesUnable::getOpportunitiesId,quoteIdLIst)
                    .eq(QuoteOpportunitiesUnable::getStatus,VersionStatus.ACTIVE.getCode()));
            newItem.setCostId(unable.getId());
        }

        newItem.setType(quote.getType());
        newItem.setCreateTime(DateUtils.getNowDate());
        newItem.setStatus(VersionStatus.ACTIVE.getCode());

        //先把其它的QuotePresaleInfo的状态都弄成DISABLED
        quotePresaleInfoMapper.update(null,Wrappers.<QuotePresaleInfo>lambdaUpdate()
                .set(QuotePresaleInfo::getStatus,VersionStatus.INACTIVE.getCode())
                .in(QuotePresaleInfo::getOpportunitiesId,quoteIdLIst));

        //生成Version版本号
        QuotePresaleInfo quotePresaleInfo = baseMapper.selectList(Wrappers.<QuotePresaleInfo>lambdaQuery()
                .in(QuotePresaleInfo::getOpportunitiesId,quoteIdLIst))
                .stream().max(Comparator.comparing(QuotePresaleInfo::getCreateTime)).orElse(null);
        if(ObjectUtil.isNotNull(quotePresaleInfo)){
            newItem.setPreSaleVersion(VersionUtil.incrementMajor(quotePresaleInfo.getPreSaleVersion()));
        }else {
            newItem.setPreSaleVersion(VersionUtil.getFirstVersion());
        }
        LambdaUpdateWrapper<QuoteOpportunities> updateWrapper = Wrappers.<QuoteOpportunities>lambdaUpdate()
                        .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                        .set(QuoteOpportunities::getSaleAuditLog,"")
                        .set(QuoteOpportunities::getSaleAuditTime,null)
                        .set(QuoteOpportunities::getProcessCategory,QuoteProcessCategory.NORMAL.getCode())
                        .eq(QuoteOpportunities::getId,quote.getId());
        //修改审核状态和审核时间
        if(saveDTO.isUnableQuote()){
            //无法报价：进入审核
            updateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.SALE_PENDING_APPROVAL.getCode());
        }else {
            //利润率
            BigDecimal profit = manageWarningService.getProfit().getExternalProfit();
            //利润率<0.15：待审核; 利润率>0.15:销售已报价
            if(saveDTO.getProjProfitRateExcl().compareTo(profit) < 0){
                updateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.SALE_PENDING_APPROVAL.getCode());
                // 发送邮件
                ThreadUtil.runInThread(() -> {
                    sendEmail(saveDTO);
                });

            }else {
                updateWrapper.set(QuoteOpportunities::getStatus,QuoteStatusEnum.SALE_QUOTE.getCode());
            }
        }
        quoteMapper.update(null,updateWrapper);


        return quotePresaleInfoMapper.insert(newItem);
    }

    /**
     * 添加销售报价版本
     * @param saveDTO saveDTO
     */
    private void sendEmail(QuotePresaleInfoSaveDTO saveDTO) {
        log.info("----------开始发送利润低于15%审批邮件----------");
        SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
        EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
        EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
        List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
        // 设置收件人
        QuoteEmailSetting quoteEmailSetting = quoteEmailSettingMapper.selectOne(Wrappers.<QuoteEmailSetting>lambdaQuery()
                .eq(QuoteEmailSetting::getType, EmailTypeEnum.QUOTATION_APPROVAL.getCode()));
        List<SysUser> sysuUserList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(quoteEmailSetting)) {
            String sender = quoteEmailSetting.getSender();
            // 处理发送者
            Arrays.stream(StringUtil.isNotEmpty(sender) ? sender.split(",") : new String[]{})
                    .map(Long::parseLong)
                    .forEach(userId -> sysUserList.stream()
                            .filter(sysUser -> sysUser.getUserId().equals(userId))
                            .findFirst()
                            .ifPresent(sysuUserList::add));
        }
        sysuUserList.forEach(item -> {
            // 设置发送人
            sendEmailInfoDTO.setSysUser(item);
            QuoteOpportunities quoteOpportunities = quoteMapper.selectById(saveDTO.getOpportunitiesId());
            if (ObjectUtil.isNotEmpty(quoteOpportunities)) {
                sysUserList.stream()
                        .filter(sysUser -> ObjectUtil.isNotEmpty(sysUser.getCrmUserId()) && quoteOpportunities.getSupportPerson().contains(sysUser.getCrmUserId()))
                        .findFirst().ifPresent(sysUser -> {
                    emailModelInfoConvertDTO.setPreSaleUserName(sysUser.getNickName());
                });
            }
            // 获取所属销售
            QuoteOpportunitiesParent quoteOpportunitiesParent = quoteOpportunitiesParentMapper.selectById(quoteOpportunities.getOpportunitiesParentId());
            if (ObjectUtil.isNotEmpty(quoteOpportunitiesParent)) {
                sysUserList.stream().filter(sysUser -> sysUser.getUserId().equals(quoteOpportunitiesParent.getSaleId())).findFirst().ifPresent(sysUser -> {
                    emailModelInfoConvertDTO.setPreSaleUser(sysUser.getNickName());
                    // 设置抄送人
                    sendEmailInfoDTO.setCopyUser(new ArrayList<SysUser>() {{
                        add(sysUser);
                    }});
                });
                // 设置商机主题
                emailSubjectInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
                emailSubjectInfoConvertDTO.setCurrentDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
                emailModelInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
            }

            // 设置模版信息
            emailModelInfoConvertDTO.setCostTotal(DecimalUtil.getStr(saveDTO.getProjCostTotal(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setSaleQuote(DecimalUtil.getStr(saveDTO.getProjExtQuoteTotal(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setProfitRate(DecimalUtil.getStr(saveDTO.getProjProfitRateExcl(),DecimalUtil.displayScale,true));
            emailModelInfoConvertDTO.setContinueFollowReason(saveDTO.getCloseNote());
            emailModelInfoConvertDTO.setQuoteSystemLink(systemConfig.getDomainName());

            sendEmailInfoDTO.setEmailModelInfoConvertDTO(emailModelInfoConvertDTO);
            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(emailSubjectInfoConvertDTO);
            quoteMailUtil.sendEmail(Collections.singletonList(sendEmailInfoDTO), EmailTypeEnum.QUOTATION_APPROVAL.getCode());
        });
        log.info("----------利润低于15%审批邮件发送完毕----------");
    }
    @Override
    public QuotePresaleInfoVO getDetailInfoById(QuotePresaleInfoQuery quotePresaleInfoQuery) {
        QuotePresaleInfo info = baseMapper.selectById(quotePresaleInfoQuery.getId());
        QuotePresaleInfoVO vo = BeanUtil.toBean(info,QuotePresaleInfoVO.class);
        if(!QuoteType.INCAPABLE.getCode().equals(info.getType())){
            List<ManageQuote> manageQuoteList = manageQuoteMapper.selectList(Wrappers.<ManageQuote>lambdaQuery());
            //取整
            quoteUtil.toRoundPresaleVO(vo,RoundingMode.CEILING);
            //计算合计
            calculateTotal(vo);
            //计算利润率
            calculateProfitRate(vo,manageQuoteList,true);

            //如果是粗略报价只填总值的情况(不需要了，先注释)
            /*
            if(QuoteType.ROUGH.getCode().equals(info.getType()) && quoteUtil.roughDetailIsEmpty(info)){
                QuoteOpportunitiesRough rough = quoteOpportunitiesRoughMapper.selectOne(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                        .eq(QuoteOpportunitiesRough::getOpportunitiesId, quotePresaleInfoQuery.getOpportunitiesId())
                        .eq(QuoteOpportunitiesRough::getStatus, VersionStatus.ACTIVE.getCode()));
                if(ObjectUtil.isNotEmpty(rough)){
                    BigDecimal profitRate = manageWarningService.getProfit().getExternalProfit();
                    BigDecimal taxRate = BigDecimal.valueOf(0.13);
                    vo.setProjCostTotal(DecimalUtil.addAll(rough.getAmount()));
                    vo.setProjSugQuoteTotal(
                            vo.getProjCostTotal()
                                    .multiply(taxRate.add(BigDecimal.valueOf(1))).multiply(profitRate.add(BigDecimal.valueOf(1)))
                    );
                }

            }
            */
        }else {
            //如果是无法报价，把那俩数字塞到软件那块(北光的前端自己计算了，就塞到dev那里)
            vo.setDevExtQuote(vo.getUnableQuoteAmount());
            vo.setXxSoftWareQuoteTotal(vo.getXxUnableQuoteAmount());
        }
        switch (QuoteType.valueOf(info.getType())){
            case COST:
                QuoteOpportunitiesDetail detail = detailMapper.selectById(info.getCostId());
                if(ObjectUtil.isNotEmpty(detail)){
                    vo.setCurrentVersion(QuoteType.COST.getInfo() + detail.getValuationVersion());
                }
                break;
            case ROUGH:
                QuoteOpportunitiesRough rough = quoteOpportunitiesRoughMapper.selectById(info.getCostId());
                if(ObjectUtil.isNotEmpty(rough)){
                    vo.setCurrentVersion(QuoteType.ROUGH.getInfo() + rough.getValuationVersion());
                    vo.setRoughDesc(rough.getValuationDesc());
                }
                break;
            case INCAPABLE:
                QuoteOpportunitiesUnable unable = unableMapper.selectById(info.getCostId());
                if(ObjectUtil.isNotEmpty(unable)){
                    vo.setCurrentVersion(QuoteType.INCAPABLE.getInfo() + unable.getValuationVersion());
                }
                break;
            default:
                break;
        }

        return vo;
    }

    @Override
    public QuotePreSaleDetailInfoVO reSales(QuotePresaleInfoQuery query) {
        if(ObjectUtil.isEmpty(query.getOpportunitiesId())){
            throw new ServiceException("请求参数异常");
        }
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(query.getOpportunitiesId());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        QuotePreSaleDetailInfoVO resultInfo = BeanUtil.toBean(quote, QuotePreSaleDetailInfoVO.class);
        Set<Long> quoteIdList = aboutQuoteList.stream().map(QuoteOpportunities::getId).collect(Collectors.toSet());

        //查询旧版销售报价信息
        QuotePresaleInfo info = baseMapper.selectOne(Wrappers.<QuotePresaleInfo>lambdaQuery()
                .in(QuotePresaleInfo::getOpportunitiesId,quoteIdList)
                .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        if(ObjectUtil.isEmpty(info)){
            throw new RuntimeException("无销售报价信息");
        }

        //是否覆盖上一版销售报价数据
        Boolean fillOldInfo = true;
        if(ObjectUtil.isNotEmpty(query.getQuoteType()) && !query.getQuoteType().equals(info.getSignType())){
            //如果切换type了，就不用上一版数据做覆盖
            fillOldInfo = false;
        }

        // 获取系统设置税和利润率
        List<ManageQuote> manageQuoteList = manageQuoteMapper.selectList(Wrappers.<ManageQuote>lambdaQuery());
        switch (QuoteType.valueOf(quote.getType())){
            case COST:
                QuoteOpportunitiesDetailVO detailVO = quoteOpportunitiesDetailService.getDetailInfo(QuoteOpportunitiesDetailQuery.builder()
                        .quoteIdList(quoteIdList.stream().collect(Collectors.toList()))
                        .status(VersionStatus.ACTIVE.getCode())
                        .build());
                if(ObjectUtil.isNotNull(detailVO)){
                    resultInfo.setQuoteVersion(QuoteType.valueOf(quote.getType()).getInfo() + "-" + detailVO.getValuationVersion());
                    resultInfo.setQuoteDesc(detailVO.getValuationDesc());
                    QuotePresaleInfoVO presaleInfoVO = setDetailAmountRateAndProfit(manageQuoteList,detailVO);
                    presaleInfoVO.setSignType(ContractType.XX.getCode());
                    //计算建议报价数据
                    calculateSuggested(presaleInfoVO);
                    Boolean isNl = ContractType.NL.getCode().equals(query.getQuoteType());
                    //从管理系统查询成本数据，欣象对外报价
                    setPresaleInfoFromManager(presaleInfoVO,detailVO,isNl);
                    if(fillOldInfo){
                        //把旧的销售报价的数据塞进去
                        fillOldPresaleInfo(info,presaleInfoVO);
                    }
                    //取整
                    quoteUtil.toRoundPresaleVO(presaleInfoVO,RoundingMode.CEILING);
                    //计算总和
                    calculateTotal(presaleInfoVO);
                    // 计算税率和利润率
                    calculateProfitRate(presaleInfoVO,manageQuoteList,false);
                    resultInfo.setQuotesPresaleInfo(presaleInfoVO);
                }
                break;
            case ROUGH:
                QuoteOpportunitiesRough rough = quoteOpportunitiesRoughMapper.selectOne(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                        .in(QuoteOpportunitiesRough::getOpportunitiesId, quoteIdList)
                        .eq(QuoteOpportunitiesRough::getStatus, VersionStatus.ACTIVE.getCode()));
                if(ObjectUtil.isNotEmpty(rough)){
                    resultInfo.setQuoteVersion(QuoteType.valueOf(quote.getType()).getInfo() + "-" + rough.getValuationVersion());
                    resultInfo.setQuoteAmount(rough.getAmount());
                    resultInfo.setQuoteDesc(rough.getValuationDesc());
                    QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail = quoteOpportunitiesRoughDetailMapper
                            .selectOne(Wrappers.<QuoteOpportunitiesRoughDetail>lambdaQuery()
                                            .eq(QuoteOpportunitiesRoughDetail::getRoughId,rough.getId()));
                    QuotePresaleInfoVO presaleInfoVO = setRoughAmountRateAndProfit(manageQuoteList, quoteOpportunitiesRoughDetail, new QuotePresaleInfoVO());
                    presaleInfoVO.setSignType(info.getSignType());
                    // 建议对外报价（成本*(1+税率)*（1+利润率））
                    calculateSuggested(presaleInfoVO);
                    if(fillOldInfo){
                        //把旧的销售报价的数据塞进去
                        fillOldPresaleInfo(info,presaleInfoVO);
                    }
                    //取整
                    quoteUtil.toRoundPresaleVO(presaleInfoVO,RoundingMode.CEILING);
                    //计算总和
                    calculateTotal(presaleInfoVO);
                    // 计算税率和利润率
                    calculateProfitRate(presaleInfoVO,manageQuoteList,false);
                    resultInfo.setQuotesPresaleInfo(presaleInfoVO);
                }
                break;
            case INCAPABLE:
                QuotePresaleInfoVO presaleInfoVO = new QuotePresaleInfoVO();
                //把旧的销售报价的数据塞进去
                fillOldPresaleInfo(info,presaleInfoVO);
                resultInfo.setQuotesPresaleInfo(presaleInfoVO);
                break;
            default:
                break;
        }
        if(ObjectUtil.isEmpty(query.getQuoteType())){
            //如果参数为空，就用上一版销售的type
        }else {
            //参数不为空就用参数
            resultInfo.getQuotesPresaleInfo().setSignType(query.getQuoteType());
        }
        return resultInfo;
    }


    /**
     * 修改报价系统-商机售前报价信息
     * 
     * @param quotePresaleInfo 报价系统-商机售前报价信息
     * @return 结果
     */
    @Override
    public int updateQuotePresaleInfo(QuotePresaleInfo quotePresaleInfo)
    {
        quotePresaleInfo.setUpdateTime(DateUtils.getNowDate());
        return quotePresaleInfoMapper.updateById(quotePresaleInfo);
    }

    /**
     * 批量删除报价系统-商机售前报价信息
     * 
     * @param ids 需要删除的报价系统-商机售前报价信息主键
     * @return 结果
     */
    @Override
    public int deleteQuotePresaleInfoByIds(Long[] ids)
    {
        return quotePresaleInfoMapper.deleteQuotePresaleInfoByIds(ids);
    }

    /**
     * 删除报价系统-商机售前报价信息信息
     * 
     * @param id 报价系统-商机售前报价信息主键
     * @return 结果
     */
    @Override
    public int deleteQuotePresaleInfoById(Long id)
    {
        return quotePresaleInfoMapper.deleteQuotePresaleInfoById(id);
    }

    @Override
    public QuotePreSaleDetailInfoVO getSalesRoughInfo(QuotePresaleInfoQuery quotePresaleInfoQuery) {
        if(ObjectUtil.isEmpty(quotePresaleInfoQuery.getOpportunitiesId())){
            throw new ServiceException("请求参数异常");
        }
        QuoteOpportunities quoteOpportunities = quoteMapper.selectById(quotePresaleInfoQuery.getOpportunitiesId());
        if(ObjectUtil.isNotEmpty(quoteOpportunities)){
            QuotePreSaleDetailInfoVO quotePreSaleDetailInfo = BeanUtil.toBean(quoteOpportunities, QuotePreSaleDetailInfoVO.class);
            QuoteOpportunitiesRough opportunitiesRough = quoteOpportunitiesRoughMapper.selectOne(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                    .eq(QuoteOpportunitiesRough::getOpportunitiesId, quotePresaleInfoQuery.getOpportunitiesId())
                    .eq(QuoteOpportunitiesRough::getStatus, VersionStatus.ACTIVE.getCode()));
            if(ObjectUtil.isNotNull(opportunitiesRough)){
                quotePreSaleDetailInfo.setQuoteVersion(QuoteType.valueOf(quoteOpportunities.getType()).getInfo() + "-" + opportunitiesRough.getValuationVersion());
                quotePreSaleDetailInfo.setQuoteAmount(opportunitiesRough.getAmount());
                quotePreSaleDetailInfo.setQuoteDesc(opportunitiesRough.getValuationDesc());
            }
            // 获取系统设置税和利润率
            List<ManageQuote> manageQuoteList = manageQuoteMapper.selectList(Wrappers.<ManageQuote>lambdaQuery());
            // 设置销售报价基本信息
            QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail = quoteOpportunitiesRoughDetailMapper
                    .selectOne(
                            Wrappers.<QuoteOpportunitiesRoughDetail>lambdaQuery()
                                    .eq(QuoteOpportunitiesRoughDetail::getRoughId,opportunitiesRough.getId())
                    );
            QuotePresaleInfoVO presaleInfoVO = setRoughAmountRateAndProfit(manageQuoteList, quoteOpportunitiesRoughDetail, new QuotePresaleInfoVO());
            presaleInfoVO.setSignType(ContractType.XX.getCode());

            // 建议对外报价（成本*(1+税率)*（1+利润率））
            calculateSuggested(presaleInfoVO);
            //取整
            quoteUtil.toRoundPresaleVO(presaleInfoVO,RoundingMode.CEILING);
            //计算总和
            calculateTotal(presaleInfoVO);
            // 计算税率和利润率
            calculateProfitRate(presaleInfoVO,manageQuoteList,false);
            quotePreSaleDetailInfo.setQuotesPresaleInfo(presaleInfoVO);
            /*
            //粗略报价只填总值处理，已堵死
            if(!quoteUtil.roughDetailIsEmpty(quoteOpportunitiesRoughDetail)){
                // 建议对外报价（成本*(1+税率)*（1+利润率））
                calculateSuggested(presaleInfoVO);
                //取整
                quoteUtil.toRoundPresaleVO(presaleInfoVO,RoundingMode.CEILING);
                //计算总和
                calculateTotal(presaleInfoVO,false);
                // 计算税率和利润率
                calculateProfitRate(presaleInfoVO,manageQuoteList,false);
                quotePreSaleDetailInfo.setQuotesPresaleInfo(presaleInfoVO);
            }
            else {
                //特殊情况:如果只填了总成本，没填单个成本的话，直接算总数
                //公式:{粗略报价}* （1+13%）*（1+{利润率要求}）
                presaleInfoVO.setProjCostTotal(DecimalUtil.addAll(opportunitiesRough.getAmount()));
                BigDecimal profitRate = manageWarningService.getProfit().getExternalProfit();
                BigDecimal taxRate = BigDecimal.valueOf(0.13);
                presaleInfoVO.setProjSugQuoteTotal(
                        presaleInfoVO.getProjCostTotal()
                                .multiply(taxRate.add(BigDecimal.valueOf(1))).multiply(profitRate.add(BigDecimal.valueOf(1)))
                );
                presaleInfoVO.setProjExtQuoteTotal(presaleInfoVO.getProjSugQuoteTotal());
                presaleInfoVO.setProjProfitRateIncl(
                        (
                                presaleInfoVO.getProjExtQuoteTotal()
                                .divide(taxRate.add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                                .subtract(presaleInfoVO.getProjCostTotal())
                        )
                        .divide(presaleInfoVO.getProjCostTotal(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                );
                presaleInfoVO.setProjProfitRateExcl(presaleInfoVO.getProjProfitRateIncl());
                quotePreSaleDetailInfo.setQuotesPresaleInfo(presaleInfoVO);
            }
            */
            return quotePreSaleDetailInfo;
        }
        return null;
    }

    @Override
    public QuotePreSaleDetailInfoVO getSalesDetailInfo(QuotePresaleInfoQuery quotePresaleInfoQuery) {
        if(ObjectUtil.isEmpty(quotePresaleInfoQuery.getOpportunitiesId())){
            throw new ServiceException("请求参数异常");
        }
        QuoteOpportunities quoteOpportunities = quoteMapper.selectById(quotePresaleInfoQuery.getOpportunitiesId());
        if(ObjectUtil.isNotEmpty(quoteOpportunities)){
            QuotePreSaleDetailInfoVO quotePreSaleDetailInfo = BeanUtil.toBean(quoteOpportunities, QuotePreSaleDetailInfoVO.class);
            // 获取系统设置税和利润率
            List<ManageQuote> manageQuoteList = manageQuoteMapper.selectList(Wrappers.<ManageQuote>lambdaQuery());
            // 设置销售报价基本信息
            QuoteOpportunitiesDetailVO detailServiceDetailInfo = quoteOpportunitiesDetailService.getDetailInfo(QuoteOpportunitiesDetailQuery.builder()
                    .opportunitiesId(quotePresaleInfoQuery.getOpportunitiesId())
                    .status(VersionStatus.ACTIVE.getCode())
                    .build());
            if(ObjectUtil.isNotNull(detailServiceDetailInfo)){
                quotePreSaleDetailInfo.setQuoteVersion(QuoteType.valueOf(quoteOpportunities.getType()).getInfo() + "-" + detailServiceDetailInfo.getVersion());
                //quotePreSaleDetailInfo.setQuoteAmount(detailServiceDetailInfo.getAmount());
                quotePreSaleDetailInfo.setQuoteDesc(detailServiceDetailInfo.getValuationDesc());
                QuotePresaleInfoVO quoteSaleInfoVO = setDetailAmountRateAndProfit(manageQuoteList,detailServiceDetailInfo);
                quoteSaleInfoVO.setSignType(ContractType.XX.getCode());
                //计算建议报价数据
                calculateSuggested(quoteSaleInfoVO);
                Boolean isNl = ContractType.NL.getCode().equals(quotePresaleInfoQuery.getQuoteType());
                //从管理系统查询成本数据，欣象对外报价
                setPresaleInfoFromManager(quoteSaleInfoVO,detailServiceDetailInfo,isNl);
                //取整
                quoteUtil.toRoundPresaleVO(quoteSaleInfoVO,RoundingMode.CEILING);
                // 计算税率和利润率
                calculateTotal(quoteSaleInfoVO);
                calculateProfitRate(quoteSaleInfoVO,manageQuoteList,false);
                quotePreSaleDetailInfo.setQuotesPresaleInfo(quoteSaleInfoVO);
            }
            return quotePreSaleDetailInfo;
        }
        return null;
    }

    /**
     * 从管理系统查询产品，硬件自研和硬件外采的成本价和欣象报价信息
     * @param vo
     * @param detailVO
     */
    private void setPresaleInfoFromManager(QuotePresaleInfoVO vo,QuoteOpportunitiesDetailVO detailVO,Boolean isNl)
    {
        //只适用于产品和硬件外采
        Map<Long,ManageProduct> productMap = null;
        Map<Long,ManageHardwareSelf> selfMap = null;
        Map<Long,ManageHardwareExt> extMap = null;
        //产品
        if(ObjectUtil.isNotEmpty(detailVO.getQuoteOpportunitiesProductVO()) &&
                CollUtil.isNotEmpty(detailVO.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList())
        ){
            Set<Long> productIdList = detailVO.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList()
                    .stream().filter(x -> ObjectUtil.isNotEmpty(x.getProductId())).map(x -> x.getProductId()).collect(Collectors.toSet());
            if(CollUtil.isNotEmpty(productIdList)){
                productMap = productMapper.selectBatchIds(productIdList).stream().collect(Collectors.toMap(ManageProduct::getId,x -> x));

                vo.setProdPrice(BigDecimal.ZERO);
                final BigDecimal[] extQuote = {new BigDecimal(0)};
                vo.setXxProdQuote(BigDecimal.ZERO);
                Map<Long, ManageProduct> finalProductMap = productMap;
                detailVO.getQuoteOpportunitiesProductVO().getQuoteOpportunitiesProductList().forEach(p ->{
                    ManageProduct product = MapUtil.getMapValue(finalProductMap,p.getProductId());
                    if(ObjectUtil.isNotEmpty(product)){
                        //数量*单价
                        //成本
                        vo.setProdPrice(
                                vo.getProdPrice().add(product.getCostPrice().multiply(p.getNumber()))
                        );
                        //对外报价
                        extQuote[0] = extQuote[0].add(product.getExtPrice().multiply(p.getNumber()));
                    }

                });
                //产品的建议报价，北光报价，欣象报价
                if(isNl){
                    //北光，建议=北光=value，欣象=北光/0.97
                    vo.setProdExtQuote(extQuote[0]);
                    vo.setProdSugQuote(vo.getProdExtQuote());
                    vo.setXxProdQuote(vo.getXxProdQuote().divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale,RoundingMode.HALF_UP));
                }else {
                    //欣象，建议=欣象=value，北光=欣象*0.97
                    vo.setXxProdQuote(
                            extQuote[0]
                    );
                    vo.setProdExtQuote(
                            vo.getXxProdQuote().multiply(BigDecimal.valueOf(0.97))
                    );
                    vo.setProdSugQuote(vo.getXxProdQuote());
                }
            }
        }
        //自研
        //自研只取成本
        if(ObjectUtil.isNotEmpty(detailVO.getQuoteOpportunitiesSelfVO()) &&
                CollUtil.isNotEmpty(detailVO.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList())){
            Set<Long> selfIdList = detailVO.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList()
                    .stream().filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId())).map(x -> x.getHardwareId()).collect(Collectors.toSet());
            if(CollUtil.isNotEmpty(selfIdList)){
                selfMap = hardwareSelfMapper.selectBatchIds(selfIdList).stream().collect(Collectors.toMap(ManageHardwareSelf::getId,x -> x));
                vo.setSelfPrice(BigDecimal.ZERO);
                vo.setXxSelfQuote(BigDecimal.ZERO);
                Map<Long, ManageHardwareSelf> finalSelfMap = selfMap;
                detailVO.getQuoteOpportunitiesSelfVO().getQuoteOpportunitiesSelfList().forEach(s ->{
                    ManageHardwareSelf self = MapUtil.getMapValue(finalSelfMap,s.getHardwareId());
                    if(ObjectUtil.isNotEmpty(self)){
                        //数量*单价
                        vo.setSelfPrice(
                                vo.getSelfPrice().add(self.getUnitPrice().multiply(s.getNumber()))
                        );
                        /*vo.setXxSelfQuote(
                                vo.getXxSelfQuote().add(self.getExtPrice().multiply(s.getNumber()))
                        );
                        vo.setSelfSugQuote(vo.getSelfExtQuote());*/
                    }
                });

                //保险起见，这仨再算一次
                vo.setSelfSugQuote(
                        vo.getSelfPrice()
                                .multiply(vo.getSelfTaxRate().add(BigDecimal.valueOf(1)))
                                .multiply(vo.getSelfProfitRate().add(BigDecimal.valueOf(1)))
                );
                vo.setSelfExtQuote(vo.getSelfSugQuote());
                vo.setXxSelfQuote(
                        vo.getSelfPrice()
                                .multiply(vo.getSelfTaxRate().add(BigDecimal.valueOf(1)))
                                .multiply(vo.getSelfProfitRate().add(BigDecimal.valueOf(1)))
                                .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
                );

            }
        }

        //外采
        if(ObjectUtil.isNotEmpty(detailVO.getQuoteOpportunitiesExternalVO()) &&
                CollUtil.isNotEmpty(detailVO.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList())
        ){
            Set<Long> extIdList = detailVO.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList()
                    .stream().filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId())).map(x -> x.getHardwareId()).collect(Collectors.toSet());
            if(CollUtil.isNotEmpty(extIdList)){
                extMap = hardwareExtMapper.selectBatchIds(extIdList).stream().collect(Collectors.toMap(ManageHardwareExt::getId,x -> x));
                vo.setExtPrice(BigDecimal.ZERO);
                vo.setXxExtQuote(BigDecimal.ZERO);
                Map<Long, ManageHardwareExt> finalExtMap = extMap;
                detailVO.getQuoteOpportunitiesExternalVO().getQuoteOpportunitiesExternalList().forEach(e ->{
                    ManageHardwareExt ext = MapUtil.getMapValue(finalExtMap,e.getHardwareId());
                    if(ObjectUtil.isNotEmpty(ext)){
                        //数量*单价
                        vo.setExtPrice(
                                vo.getExtPrice().add(ext.getEstimatedCost().multiply(e.getNumber()))
                        );
                        //欣象
                        vo.setXxExtQuote(
                                vo.getXxExtQuote().add(ext.getExternalQuote().multiply(e.getNumber()))
                        );
                    }
                });
                //建议
                vo.setExtSugQuote(vo.getXxExtQuote());
                //北光
                vo.setExtExtQuote(vo.getXxExtQuote());
            }
        }

    }

    @Override
    public SignDetailInfoVO approvalAndSignInfo(Long opportunitiesId,Boolean fromManage) {
        SignDetailInfoVO vo = new SignDetailInfoVO();

        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(opportunitiesId);
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }
        Set<Long> quoteIdList = aboutQuoteList.stream().map(QuoteOpportunities::getId).collect(Collectors.toSet());

        //先查出最新成本版本
        QuoteOpportunitiesRoughQuery versionQuery = new QuoteOpportunitiesRoughQuery();
        versionQuery.setLatest(true);
        versionQuery.setOpportunitiesId(quote.getId());
        List<OpportunitiesDetailVersionVO> versionVOList = quoteService.getVersionList(versionQuery);
        if(CollUtil.isEmpty(versionVOList) || versionVOList.size() != 1){
            throw new RuntimeException("无可用的成本报价");
        }
        OpportunitiesDetailVersionVO currentVersion = versionVOList.get(0);
        //填充成本报价信息
        Boolean roughOnlyTotal = fillQuoteInfo(vo,quote.getType(),currentVersion.getId(), fromManage);

        //查询销售报价信息
        QuotePresaleInfo presaleInfo = baseMapper.selectOne(Wrappers.<QuotePresaleInfo>lambdaQuery()
                .in(QuotePresaleInfo::getOpportunitiesId,quoteIdList)
                .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));

        if(ObjectUtil.isEmpty(presaleInfo)){
            throw new RuntimeException("无效的销售报价信息");
        }
        //销售报价合同类型
        vo.setCloseNote(presaleInfo.getCloseNote());
        vo.setContractType(presaleInfo.getSignType());
        vo.setSignType(presaleInfo.getSignType());
        if(!QuoteType.INCAPABLE.getCode().equals(presaleInfo.getType()))
        {
            //对外报价--来自销售报价
            vo.setSoftWareExtQuote(DecimalUtil.addAll(presaleInfo.getPreExtQuote(),presaleInfo.getDevExtQuote()));//软件
            vo.setProdExtQuote(DecimalUtil.addAll(presaleInfo.getProdExtQuote()));//产品
            vo.setOtherExtQuote(DecimalUtil.addAll(presaleInfo.getOtherExtQuote()));//其它
            vo.setSelfExtQuote(DecimalUtil.addAll(presaleInfo.getSelfExtQuote()));//自研
            vo.setExtExtQuote(DecimalUtil.addAll(presaleInfo.getExtExtQuote()));//外采
            vo.setImpExtQuote(DecimalUtil.addAll(presaleInfo.getImpExtQuote()));//实施
            //计算其它数据
            fillApprovalAndSignInfo(vo,roughOnlyTotal);
        }else {
            vo.setUnableQuoteAmount(presaleInfo.getUnableQuoteAmount());
            vo.setXxUnableQuoteAmount(presaleInfo.getXxUnableQuoteAmount());
            vo.setValuationDesc(presaleInfo.getDescription());
        }



        return vo;
    }

    /**
     * 签约申请页-填充成本报价
     * @param vo
     * @param type
     * @param versionId
     */
    @Override
    public Boolean fillQuoteInfo(ApprovalAndSignInfoVO vo,String type,Long versionId,Boolean fromManage)
    {
        Boolean result = false;
        //粗略报价或详细报价时，填充右侧成本相关数据
        if(QuoteType.COST.getCode().equals(type)){
            fillCostInfo(vo,versionId, fromManage);
        }
        else if(QuoteType.ROUGH.getCode().equals(type)){
            Boolean roughOnlyTotal = fillRoughInfo(vo,versionId);
            //特殊情况:粗略报价时可能会只填总值(已堵死)
            //如果只填总值，就不会进入签约审批页，必须更新成本报价
            /*if(roughOnlyTotal){
                result = true;

            }*/
        }
        else {
            //无法报价不会进入签约审批页，必须更新成本报价
            //fillUnableInfo(vo,currentVersion.getId());
        }

        return result;
    }

    /**
     * 签约申请页-填充成本报价-详细报价数据
     * @param vo
     * @param versionId
     */
    private void fillCostInfo(ApprovalAndSignInfoVO vo,Long versionId,Boolean fromManage)
    {
        //详细报价
        QuoteOpportunitiesDetailQuery detailQuery = new QuoteOpportunitiesDetailQuery();
        detailQuery.setId(versionId);
        QuoteOpportunitiesDetailVO detailVO = quoteOpportunitiesDetailService.getDetailInfo(detailQuery);
        if(ObjectUtil.isEmpty(detailVO)){
            throw new RuntimeException("无效的详细报价信息");
        }
        // 成本
        //人工成本 = 售前+定制开发
        vo.setLaborPrice(
                Optional.of(detailVO)
                        .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesSupportVO()).map(QuoteOpportunitiesSupportVO::getTotalCost).orElse(BigDecimal.ZERO)
                                .add(Optional.ofNullable(detail.getQuoteOpportunitiesCustomizableVo()).map(QuoteOpportunitiesCustomizableVO::getTotalSoftwareCosts).orElse(BigDecimal.ZERO)))
                        .orElse(BigDecimal.ZERO)
        );
        if(fromManage){
            //产品:特殊情况，去管理系统取
            List<QuoteOpportunitiesProduct> quoteProductList = quoteProductMapper.selectList(Wrappers.<QuoteOpportunitiesProduct>lambdaQuery()
                    .eq(QuoteOpportunitiesProduct::getOpportunitiesDetailId,versionId));
            if(CollUtil.isNotEmpty(quoteProductList)){
                Set<Long> productIdList = quoteProductList.stream()
                        .filter(x -> ObjectUtil.isNotEmpty(x.getProductId()))
                        .map(x -> x.getProductId())
                        .collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(productIdList)){
                    Map<Long,ManageProduct>  productMap = productMapper.selectBatchIds(productIdList).stream().collect(Collectors.toMap(ManageProduct::getId,x -> x));
                    Map<Long, ManageProduct> finalProductMap = productMap;
                    vo.setProdPrice(BigDecimal.ZERO);
                    quoteProductList.forEach(p ->{
                        ManageProduct product = MapUtil.getMapValue(finalProductMap,p.getProductId());
                        if(ObjectUtil.isNotEmpty(product)){
                            //数量*单价
                            vo.setProdPrice(
                                    vo.getProdPrice().add(product.getCostPrice().multiply(p.getNumber()))
                            );
                        }
                    });

                }
            }

            //自研
            List<QuoteOpportunitiesSelf> quoteSelfList = quoteSelfMapper.selectList(Wrappers.<QuoteOpportunitiesSelf>lambdaQuery()
                    .eq(QuoteOpportunitiesSelf::getOpportunitiesDetailId,versionId));
            if(CollUtil.isNotEmpty(quoteSelfList)){
                Set<Long> selfIdList = quoteSelfList.stream()
                        .filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId()))
                        .map(x -> x.getHardwareId())
                        .collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(selfIdList)){
                    Map<Long,ManageHardwareSelf> selfMap = hardwareSelfMapper.selectBatchIds(selfIdList).stream().collect(Collectors.toMap(ManageHardwareSelf::getId,x -> x));
                    vo.setSelfPrice(BigDecimal.ZERO);
                    Map<Long, ManageHardwareSelf> finalSelfMap = selfMap;
                    quoteSelfList.forEach(s ->{
                        ManageHardwareSelf self = MapUtil.getMapValue(finalSelfMap,s.getHardwareId());
                        if(ObjectUtil.isNotEmpty(self)){
                            //数量*单价
                            vo.setSelfPrice(
                                    vo.getSelfPrice().add(self.getUnitPrice().multiply(s.getNumber()))
                            );
                        }
                    });
                }
            }

            //外采
            List<QuoteOpportunitiesExternal> quoteExtList = quoteExtMapper.selectList(Wrappers.<QuoteOpportunitiesExternal>lambdaQuery()
                    .eq(QuoteOpportunitiesExternal::getOpportunitiesDetailId,versionId));
            if(ObjectUtil.isNotEmpty(quoteExtList)){
                Set<Long> extIdList = quoteExtList.stream()
                        .filter(x -> ObjectUtil.isNotEmpty(x.getHardwareId()))
                        .map(x -> x.getHardwareId())
                        .collect(Collectors.toSet());
                if(CollUtil.isNotEmpty(extIdList)){
                    Map<Long,ManageHardwareExt> extMap = hardwareExtMapper.selectBatchIds(extIdList).stream().collect(Collectors.toMap(ManageHardwareExt::getId,x -> x));
                    vo.setExtPrice(BigDecimal.ZERO);
                    Map<Long, ManageHardwareExt> finalExtMap = extMap;

                    quoteExtList.forEach(e ->{
                        ManageHardwareExt ext = MapUtil.getMapValue(finalExtMap,e.getHardwareId());
                        if(ObjectUtil.isNotEmpty(ext)){
                            //数量*单价
                            vo.setExtPrice(
                                    vo.getExtPrice().add(ext.getEstimatedCost().multiply(e.getNumber()))
                            );
                        }
                    });
                }
            }
        }else {
            //产品
            vo.setProdPrice(
                    Optional.of(detailVO)
                            .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesProductVO()).map(QuoteOpportunitiesProductVO::getTotalCost).orElse(BigDecimal.ZERO))
                            .orElse(BigDecimal.ZERO)
            );
            //自研
            vo.setSelfPrice(
                    Optional.of(detailVO)
                            .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesSelfVO()).map(QuoteOpportunitiesSelfVO::getTotalCost).orElse(BigDecimal.ZERO))
                            .orElse(BigDecimal.ZERO)
            );
            //外采
            vo.setExtPrice(
                    Optional.of(detailVO)
                            .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesExternalVO()).map(QuoteOpportunitiesExternalVO::getTotalEstimatedlCost).orElse(BigDecimal.ZERO))
                            .orElse(BigDecimal.ZERO)
            );
        }

        //实施
        vo.setImpPrice(
                Optional.of(detailVO)
                        .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesImplement()).map(QuoteOpportunitiesImplement::getTotalCost).orElse(BigDecimal.ZERO))
                        .orElse(BigDecimal.ZERO)
        );
        //其它
        vo.setOtherPrice(
                Optional.of(detailVO)
                        .map(detail -> Optional.ofNullable(detail.getQuoteOpportunitiesOtherVO()).map(QuoteOpportunitiesOtherVO::getTotalCost).orElse(BigDecimal.ZERO))
                        .orElse(BigDecimal.ZERO)
        );
        //预计总成本
        vo.setTotalCost(
                DecimalUtil.addAll(vo.getLaborPrice(),vo.getProdPrice(),vo.getSelfPrice(),vo.getImpPrice(),vo.getOtherPrice())
        );
    }

    /**
     * 签约申请页-填充成本报价-粗略报价数据
     * @param vo
     * @param versionId
     * @return
     */
    private Boolean fillRoughInfo(ApprovalAndSignInfoVO vo,Long versionId)
    {
        Boolean roughOnlyTotal = false;

        //粗略报价
        QuoteOpportunitiesDetailQuery detailQuery = new QuoteOpportunitiesDetailQuery();
        detailQuery.setId(versionId);
        QuoteOpportunitiesRoughVO roughVO = roughService.getRoughInfo(detailQuery);
        if(ObjectUtil.isEmpty(roughVO)){
            return null;
        }
        if(CollUtil.isNotEmpty(roughVO.getQuoteOpportunitiesRoughDetails()))
        {
            //如果roughVO的RoughDetails不为空，就计算RoughDetails里的数据
            QuoteOpportunitiesRoughDetail roughDetail = roughVO.getQuoteOpportunitiesRoughDetails().get(0);
            //人工成本 = 售前+定制开发
            vo.setLaborPrice(
                    DecimalUtil.addAll(roughDetail.getSupportAmount(),roughDetail.getCustomAmount())
            );
            //产品
            vo.setProdPrice(
                    DecimalUtil.addAll(roughDetail.getProductAmount())
            );
            //自研
            vo.setSelfPrice(
                    DecimalUtil.addAll(roughDetail.getSelfAmount())
            );
            //外采
            vo.setExtPrice(
                    DecimalUtil.addAll(roughDetail.getExternalAmount())
            );
            //其它
            vo.setOtherPrice(
                    DecimalUtil.addAll(roughDetail.getOtherAmount())
            );
            //实施
            vo.setImpPrice(
                    DecimalUtil.addAll(roughDetail.getImplementAmount())
            );
            //预计总成本
            vo.setTotalCost(
                    DecimalUtil.addAll(vo.getLaborPrice(),vo.getProdPrice(),vo.getSelfPrice(),vo.getImpPrice(),vo.getOtherPrice())
            );
        }
        //粗略报价只填总值相关处理，注释
        /*else
        {
            //不然就直接给个总成本
            vo.setTotalCost(DecimalUtil.addAll(roughVO.getAmount()));
            roughOnlyTotal = true;
        }*/

        return roughOnlyTotal;
    }

    /**
     * 签约申请页-计算其它数据
     * @param vo
     */
    @Override
    public void fillApprovalAndSignInfo(ApprovalAndSignInfoVO vo,Boolean roughOnlyTotal)
    {
        Map<String,ManageQuote> manageQuoteMap = manageQuoteService.getManageQuoteMap();
        //BigDecimal profitRate = manageWarningService.getProfit().getExternalProfit();

        // 北光报价金额
        vo.setNorthQuoteAmount(
                DecimalUtil.addAll(vo.getSoftWareExtQuote(),vo.getProdExtQuote(),vo.getSelfExtQuote(),vo.getImpExtQuote(),vo.getOtherExtQuote())
        );
        //北光报价总金额
        vo.setNorthAmount(
                DecimalUtil.addAll(vo.getNorthQuoteAmount(),vo.getExtExtQuote())
        );
        //欣象报价总金额
        //vo.setXxAmount(vo.getNorthAmount());
        //设置预计税后收入
        //软件税后收入统一用DEV（自定义开发）的
        vo.setSoftWareExtIncome(vo.getSoftWareExtQuote().divide(manageQuoteMap.get(RealQuoteType.SOFTWARE_DEVELOPMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)), DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        vo.setProdExtIncome(vo.getProdExtQuote().divide(manageQuoteMap.get(RealQuoteType.PRODUCT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        vo.setSelfExtIncome(vo.getSelfExtQuote().divide(manageQuoteMap.get(RealQuoteType.SELF_DEVELOPED.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        vo.setImpExtIncome(vo.getImpExtQuote().divide(manageQuoteMap.get(RealQuoteType.IMPLEMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        vo.setOtherExtIncome(vo.getOtherExtQuote().divide(manageQuoteMap.get(RealQuoteType.OTHER.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        vo.setExtExtIncome(vo.getExtExtQuote().divide(manageQuoteMap.get(RealQuoteType.EXTERNAL_PROCUREMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        //预计税后总收入
        vo.setTotalExtIncome(
                DecimalUtil.addAll(vo.getSoftWareExtIncome(),vo.getProdExtIncome(),vo.getSelfExtIncome(),vo.getImpExtIncome(),vo.getOtherExtIncome())
        );
        // 预计利润 预计税后总收入-预计总成本
        vo.setTotalProfit(vo.getTotalExtIncome().subtract(vo.getTotalCost()));
        // 成本利润率 检查预计总成本是否不等于零，如果是，则计算预计利润除以预计总成本的比值；如果不是，则返回 "-"。
        vo.setCostProfitRate(
                vo.getTotalCost().compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : vo.getTotalProfit().divide(vo.getTotalCost(),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );
        //外采利润
        vo.setExtProfit(vo.getExtExtIncome().subtract(vo.getExtPrice()));
        //外采利润率
        vo.setExtProfitRate(
                vo.getExtPrice().compareTo(BigDecimal.ZERO)==0
                        ? BigDecimal.ZERO
                        : vo.getExtProfit().divide(vo.getExtPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
        );
        // 总成本利润率 如果预计总成本不为0，则计算预计总利润除以预计总成本
        if(DecimalUtil.addAll(vo.getTotalCost(),vo.getExtPrice()).compareTo(BigDecimal.ZERO) != 0){
            vo.setTotalCostProfitRate(DecimalUtil.addAll(vo.getTotalProfit(),vo.getExtProfit())
                    .divide(DecimalUtil.addAll(vo.getTotalCost(),vo.getExtPrice()),DecimalUtil.defaultScale, RoundingMode.HALF_UP));
        }

    }

    private void fillOldPresaleInfo(QuotePresaleInfo oldItem,QuotePresaleInfoVO newItem)
    {
        newItem.setSignType(oldItem.getSignType());
        newItem.setType(oldItem.getType());

        newItem.setPreExtQuote(oldItem.getPreExtQuote());
        newItem.setDevExtQuote(oldItem.getDevExtQuote());
        newItem.setProdExtQuote(oldItem.getProdExtQuote());
        newItem.setOtherExtQuote(oldItem.getOtherExtQuote());
        newItem.setSelfExtQuote(oldItem.getSelfExtQuote());
        newItem.setExtExtQuote(oldItem.getExtExtQuote());
        newItem.setImpExtQuote(oldItem.getImpExtQuote());
        newItem.setExtProxyExtQuote(oldItem.getExtProxyExtQuote());

        newItem.setXxPreQuote(oldItem.getXxPreQuote());
        newItem.setXxDevQuote(oldItem.getXxDevQuote());
        newItem.setXxProdQuote(oldItem.getXxProdQuote());
        newItem.setXxOtherQuote(oldItem.getXxOtherQuote());
        newItem.setXxSelfQuote(oldItem.getXxSelfQuote());
        newItem.setXxExtQuote(oldItem.getXxExtQuote());
        newItem.setXxImpQuote(oldItem.getXxImpQuote());
        newItem.setXxAdditionalQuote(oldItem.getXxAdditionalQuote());
        newItem.setXxExtProxyQuote(oldItem.getXxExtProxyQuote());

        //欣象报价小计，不管是不是从数据库来的，都这样算
        newItem.setXxSoftWareQuoteTotal(
                DecimalUtil.addAll(newItem.getXxPreQuote(),newItem.getXxDevQuote(),newItem.getXxProdQuote(),newItem.getXxOtherQuote(),newItem.getXxExtProxyQuote())
        );
        newItem.setXxHardWareQuoteTotal(
                DecimalUtil.addAll(newItem.getXxSelfQuote(),newItem.getXxExtQuote())
        );

        //无法报价相关
        newItem.setUnableQuoteAmount(oldItem.getUnableQuoteAmount());
        newItem.setXxUnableQuoteAmount(oldItem.getXxUnableQuoteAmount());
    }

    @Override
    public QuotePresaleInfo getCurrentPresaleInfo(Long opportunitiesId) {
        QuotePresaleInfo info = baseMapper.selectOne(Wrappers.<QuotePresaleInfo>lambdaQuery()
                .eq(QuotePresaleInfo::getOpportunitiesId,opportunitiesId)
                .eq(QuotePresaleInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        return info;
    }

    /**
     * 是否存在成本信息
     * @param opportunitiesId
     * @return
     */
    @Override
    public Boolean existCostInfo(Long opportunitiesId) {
        Boolean result = false;
        QuoteOpportunities quote = quoteMapper.selectById(opportunitiesId);
        if(ObjectUtil.isEmpty(quote)){
            return false;
        }

        //详细报价：true
        if(QuoteType.COST.getCode().equals(quote.getType())){
            result = true;
        }
        //粗略报价：分情况
        else if(QuoteType.ROUGH.getCode().equals(quote.getType())) {
            //粗略只填总值相关处理，目前不需要了
            /*
            QuoteOpportunitiesRough rough = quoteOpportunitiesRoughMapper.selectOne(
                    Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                            .eq(QuoteOpportunitiesRough::getOpportunitiesId,opportunitiesId)
                            .eq(QuoteOpportunitiesRough::getStatus,VersionStatus.ACTIVE.getCode())
            );
            if(ObjectUtil.isNotEmpty(rough)){
                QuoteOpportunitiesRoughDetail roughDetail = quoteOpportunitiesRoughDetailMapper
                        .selectOne(
                                Wrappers.<QuoteOpportunitiesRoughDetail>lambdaQuery()
                                        .eq(QuoteOpportunitiesRoughDetail::getRoughId,rough.getId())
                        );
                result = !quoteUtil.roughDetailIsEmpty(roughDetail);
            }
            */
            result = true;
        }
        //无法报价：false
        else {
            result = false;
        }

        return result;
    }


    private QuotePresaleInfoVO setDetailAmountRateAndProfit(List<ManageQuote> manageQuoteList, QuoteOpportunitiesDetailVO detailServiceDetailInfo){
        QuotePresaleInfoVO quoteSaleInfoVO = new QuotePresaleInfoVO();
        // 售前支持
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SALES_SUPPORT);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesSupportVO())){
            QuoteOpportunitiesSupportVO quoteOpportunitiesSupportVO = detailServiceDetailInfo.getQuoteOpportunitiesSupportVO();
            quoteSaleInfoVO.setPrePrice(getValue(quoteOpportunitiesSupportVO.getTotalCost()));
        }
        // 定制开发费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SOFTWARE_DEVELOPMENT);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesCustomizableVo())){
            QuoteOpportunitiesCustomizableVO quoteOpportunitiesCustomizableVo = detailServiceDetailInfo.getQuoteOpportunitiesCustomizableVo();
            quoteSaleInfoVO.setDevPrice(getValue(quoteOpportunitiesCustomizableVo.getTotalSoftwareCosts()));
        }
        // 产品平台费用(从产品管理里取)
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.PRODUCT);

        // 其他费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.OTHER);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesOtherVO())){
            QuoteOpportunitiesOtherVO quoteOpportunitiesOtherVO = detailServiceDetailInfo.getQuoteOpportunitiesOtherVO();
            quoteSaleInfoVO.setOtherPrice(getValue(quoteOpportunitiesOtherVO.getTotalCost()));
        }
        // 硬件费用-自产硬件
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SELF_DEVELOPED);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesSelfVO())){
            QuoteOpportunitiesSelfVO quoteOpportunitiesSelfVO = detailServiceDetailInfo.getQuoteOpportunitiesSelfVO();
            quoteSaleInfoVO.setSelfPrice(getValue(quoteOpportunitiesSelfVO.getTotalCost()));
        }
        // 硬件费用-采购硬件
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.EXTERNAL_PROCUREMENT);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesExternalVO())){
            QuoteOpportunitiesExternalVO quoteOpportunitiesExternalVO = detailServiceDetailInfo.getQuoteOpportunitiesExternalVO();
            quoteSaleInfoVO.setExtPrice(getValue(quoteOpportunitiesExternalVO.getTotalEstimatedlCost()));
        }
        // 实施费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.IMPLEMENT);
        if(ObjectUtil.isNotEmpty(detailServiceDetailInfo.getQuoteOpportunitiesImplement())){
            QuoteOpportunitiesImplement quoteOpportunitiesImplement = detailServiceDetailInfo.getQuoteOpportunitiesImplement();
            quoteSaleInfoVO.setImpPrice(getValue(quoteOpportunitiesImplement.getTotalCost()));
        }
        return quoteSaleInfoVO;
    }

    /**
     * 填充售前粗略报价信息
     * @param manageQuoteList 管理系统-税率
     * @param roughDetail 操作对象
     * @param quoteSaleInfoVO 详细信息对象
     * @return
     */
    private QuotePresaleInfoVO setRoughAmountRateAndProfit(List<ManageQuote> manageQuoteList, QuoteOpportunitiesRoughDetail roughDetail, QuotePresaleInfoVO quoteSaleInfoVO) {
        /*if(ObjectUtil.isEmpty(roughDetail)){
            roughDetail = new QuoteOpportunitiesRoughDetail();
        }*/
        // 售前支持
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SALES_SUPPORT);
        if(ObjectUtil.isNotEmpty(roughDetail.getSupportAmount())){
            quoteSaleInfoVO.setPrePrice(getValue(roughDetail.getSupportAmount()));
        }
        // 定制开发费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SOFTWARE_DEVELOPMENT);
        if(ObjectUtil.isNotEmpty(roughDetail.getCustomAmount())){
            quoteSaleInfoVO.setDevPrice(getValue(roughDetail.getCustomAmount()));
        }
        // 产品平台费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.PRODUCT);
        if(ObjectUtil.isNotEmpty(roughDetail.getProductAmount())){
            quoteSaleInfoVO.setProdPrice(getValue(roughDetail.getProductAmount()));
        }
        // 其他费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.OTHER);
        if(ObjectUtil.isNotEmpty(roughDetail.getOtherAmount())){
            quoteSaleInfoVO.setOtherPrice(getValue(roughDetail.getOtherAmount()));
        }
        // 硬件费用-自产硬件
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.SELF_DEVELOPED);
        if(ObjectUtil.isNotEmpty(roughDetail.getSelfAmount())){
            quoteSaleInfoVO.setSelfPrice(getValue(roughDetail.getSelfAmount()));
        }
        // 硬件费用-采购硬件
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.EXTERNAL_PROCUREMENT);
        if(ObjectUtil.isNotEmpty(roughDetail.getExternalAmount())){
            quoteSaleInfoVO.setExtPrice(getValue(roughDetail.getExternalAmount()));
        }
        // 实施费用
        // 设置税率和利润率
        fillRateAndProfit(manageQuoteList, quoteSaleInfoVO,RealQuoteType.IMPLEMENT);
        if(ObjectUtil.isNotEmpty(roughDetail.getImplementAmount())){
            quoteSaleInfoVO.setImpPrice(getValue(roughDetail.getImplementAmount()));
        }

        return quoteSaleInfoVO;
    }

    /**
     * 添加税率
     * @param manageQuoteList 管理系统-税率
     * @param quoteSaleInfoVO 操作对象
     * @param type 类型
     */
    private static void fillRateAndProfit(List<ManageQuote> manageQuoteList, QuotePresaleInfoVO quoteSaleInfoVO, RealQuoteType type) {
        manageQuoteList.stream().filter(manageQuote -> manageQuote.getType().equals(type.getCode())).findFirst().ifPresent(manageQuote -> {
            switch (type){
                case SALES_SUPPORT:
                    quoteSaleInfoVO.setPreTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setPreProfitRate(manageQuote.getProfitability());
                    break;
                case SOFTWARE_DEVELOPMENT:
                    quoteSaleInfoVO.setDevTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setDevProfitRate(manageQuote.getProfitability());
                    break;
                case PRODUCT:
                    quoteSaleInfoVO.setProdTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setProdProfitRate(manageQuote.getProfitability());
                    break;
                case OTHER:
                    quoteSaleInfoVO.setOtherTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setOtherProfitRate(manageQuote.getProfitability());
                    break;
                case SELF_DEVELOPED:
                    quoteSaleInfoVO.setSelfTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setSelfProfitRate(manageQuote.getProfitability());
                    break;
                case EXTERNAL_PROCUREMENT:
                    quoteSaleInfoVO.setExtTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setExtProfitRate(manageQuote.getProfitability());
                    break;
                case IMPLEMENT:
                    quoteSaleInfoVO.setImpTaxRate(manageQuote.getDutyRate());
                    quoteSaleInfoVO.setImpProfitRate(manageQuote.getProfitability());
                    break;
            }
        });
    }

    /**
     * 计算建议报价;
     * 建议对外报价=成本*（1+税率）*（1+利润率）;
     * 对外报价默认值=建议对外报价;
     * 欣象对外报价默认值=[成本*（1+税率）*（1+利润率）] /（0.97）;
     * @param vo
     */
    private static void calculateSuggested(QuotePresaleInfoVO vo) {
        //详细：
            //建议报价：
                //第一次：管理系统取的，或用成本算的
                //重新：管理系统取的，或用成本算的
            //北光报价：
                //第一次：管理系统取的，或用成本算的
                //重新：上一次数据
            //欣象报价：
                //第一次：管理系统取的，或用成本算的
                //重新：上一次数据

        //粗略：
            //建议报价：
                //第一次：用成本算的
                //重新：用成本算的
            //北光报价：
                //第一次：用成本算的
                //重新：上一次数据
            //欣象报价：
                //第一次：用成本算的
                //重新：上一次数据

        //优先级：
            //建议报价：
                //管理系统取的 > 用成本算的
            //北光报价 & 欣象报价：
                //上一次数据 > 管理系统取的 > 用成本算的

        //解决方案：无脑算，用管理系统盖一次，再用第二次数据盖一次

        //产品
        vo.setProdSugQuote(
                vo.getProdPrice()
                        .multiply(vo.getProdTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getProdProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setProdExtQuote(vo.getProdSugQuote());
        vo.setXxProdQuote(
                vo.getProdPrice()
                        .multiply(vo.getProdTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getProdProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );

        //自研
        vo.setSelfSugQuote(
                vo.getSelfPrice()
                        .multiply(vo.getSelfTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getSelfProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setSelfExtQuote(vo.getSelfSugQuote());
        vo.setXxSelfQuote(
                vo.getSelfPrice()
                        .multiply(vo.getSelfTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getSelfProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );
        //外采
        //外采报价默认欣象和北光相等
        vo.setExtSugQuote(
                vo.getExtPrice()
                        .multiply(vo.getExtTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getExtProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setExtExtQuote(vo.getExtSugQuote());
        vo.setXxExtQuote(vo.getExtExtQuote());

        //售前
        vo.setPreSugQuote(
                vo.getPrePrice()
                        .multiply(vo.getPreTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getPreProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setPreExtQuote(vo.getPreSugQuote());
        vo.setXxPreQuote(
                vo.getPrePrice()
                        .multiply(vo.getPreTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getPreProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );
        //定制
        vo.setDevSugQuote(
                vo.getDevPrice()
                        .multiply(vo.getDevTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getDevProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setDevExtQuote(vo.getDevSugQuote());
        vo.setXxDevQuote(
                vo.getDevPrice()
                        .multiply(vo.getDevTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getDevProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );

        //其它
        vo.setOtherSugQuote(
                vo.getOtherPrice()
                        .multiply(vo.getOtherTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getOtherProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setOtherExtQuote(vo.getOtherSugQuote());
        vo.setXxOtherQuote(
                vo.getOtherPrice()
                        .multiply(vo.getOtherTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getOtherProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );

        //实施
        vo.setImpSugQuote(
                vo.getImpPrice()
                        .multiply(vo.getImpTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getImpProfitRate().add(BigDecimal.valueOf(1)))
        );
        vo.setImpExtQuote(vo.getImpSugQuote());
        vo.setXxImpQuote(
                vo.getImpPrice()
                        .multiply(vo.getImpTaxRate().add(BigDecimal.valueOf(1)))
                        .multiply(vo.getImpProfitRate().add(BigDecimal.valueOf(1)))
                        .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
        );
        // 外采代理费用
        if(vo.getExtSugQuote().compareTo(BigDecimal.ZERO) > 0){
            vo.setXxExtProxyQuote(
                    vo.getExtSugQuote()
                            .divide(BigDecimal.valueOf(0.97),DecimalUtil.defaultScale, RoundingMode.HALF_UP)
                            .subtract(vo.getExtSugQuote())
            );
        }
    }

    /**
     * 计算合计
     * @param vo
     */
    private void calculateTotal(QuotePresaleInfoVO vo) {
        //软件成本小计 售前+自定义+产品+其它
        vo.setSoftWareCostTotal(
                DecimalUtil.addAll(
                        vo.getPrePrice(),vo.getDevPrice(),vo.getProdPrice(),vo.getOtherPrice()
                )

        );
        //软件建议报价小计 售前+自定义+产品+其它
        vo.setSoftWareSugQuoteTotal(
                DecimalUtil.addAll(vo.getPreSugQuote(),vo.getDevSugQuote(),
                        vo.getProdSugQuote(),vo.getOtherSugQuote())
        );

        //硬件成本小计 采购+自研
        vo.setHardWareCostTotal(
                DecimalUtil.addAll(vo.getSelfPrice(),vo.getExtPrice())
        );
        //硬件建议报价小计 采购+自研
        vo.setHardWareSugQuoteTotal(
                DecimalUtil.addAll(vo.getSelfSugQuote(),vo.getExtSugQuote())
        );

        //项目成本小计 硬件+软件+实施
        vo.setProjCostTotal(
                DecimalUtil.addAll(vo.getSoftWareCostTotal(),vo.getHardWareCostTotal(),vo.getImpPrice())
        );
        //项目建议报价合计 硬件+软件+实施
        vo.setProjSugQuoteTotal(
                DecimalUtil.addAll(vo.getSoftWareSugQuoteTotal(),vo.getHardWareSugQuoteTotal(),vo.getImpSugQuote())
        );

        //软件对外报价小计 售前+自定义+产品+其它
        vo.setSoftWareExtQuoteTotal(
                DecimalUtil.addAll(vo.getPreExtQuote(),vo.getDevExtQuote(),
                        vo.getProdExtQuote(),vo.getOtherExtQuote())
        );
        //硬件对外报价小计 采购+自研
        vo.setHardWareExtQuoteTotal(
                DecimalUtil.addAll(vo.getSelfExtQuote(),vo.getExtExtQuote())
        );
        //项目外报报价合计 硬件+软件+实施
        vo.setProjExtQuoteTotal(
                DecimalUtil.addAll(vo.getSoftWareExtQuoteTotal(),vo.getHardWareExtQuoteTotal(),vo.getImpExtQuote())
        );

        //欣象报价小计，不管是不是从数据库来的，都这样算
        vo.setXxSoftWareQuoteTotal(
                DecimalUtil.addAll(vo.getXxPreQuote(),vo.getXxDevQuote(),vo.getXxProdQuote(),vo.getXxOtherQuote(),vo.getXxExtProxyQuote())
        );
        vo.setXxHardWareQuoteTotal(
                DecimalUtil.addAll(vo.getXxSelfQuote(),vo.getXxExtQuote())
        );


    }

    /**
     * 计算利润率
     * @param vo
     * @param inDb 是否是从数据库来
     */
    private void calculateProfitRate(QuotePresaleInfoVO vo,List<ManageQuote> manageQuoteList,Boolean inDb){
        Map<String,ManageQuote> manageQuoteMap = manageQuoteList.stream().collect(Collectors.toMap(x -> x.getType(),x -> x));
        //售前利润率
        if(vo.getPrePrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setPreActualProfitRate(
                    vo.getPreExtQuote().divide(manageQuoteMap.get(RealQuoteType.SALES_SUPPORT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getPrePrice())
                            .divide(vo.getPrePrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setPreActualProfitRate(BigDecimal.ZERO);
        }
        //定制利润率
        if(vo.getDevPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setDevActualProfitRate(
                    vo.getDevExtQuote().divide(manageQuoteMap.get(RealQuoteType.SOFTWARE_DEVELOPMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getDevPrice())
                            .divide(vo.getDevPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setDevActualProfitRate(BigDecimal.ZERO);
        }
        //产品利润率
        if(vo.getProdPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setProdActualProfitRate(
                    vo.getProdExtQuote().divide(manageQuoteMap.get(RealQuoteType.PRODUCT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getProdPrice())
                            .divide(vo.getProdPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setProdActualProfitRate(BigDecimal.ZERO);
        }
        //其它利润率
        if(vo.getOtherPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setOtherActualProfitRate(
                    vo.getOtherExtQuote().divide(manageQuoteMap.get(RealQuoteType.OTHER.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getOtherPrice())
                            .divide(vo.getOtherPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setOtherActualProfitRate(BigDecimal.ZERO);
        }
        //自研利润率
        if(vo.getSelfPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setSelfActualProfitRate(
                    vo.getSelfExtQuote().divide(manageQuoteMap.get(RealQuoteType.SELF_DEVELOPED.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getSelfPrice())
                            .divide(vo.getSelfPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setSelfActualProfitRate(BigDecimal.ZERO);
        }
        //外采利润率
        if(vo.getExtPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setExtActualProfitRate(
                    vo.getExtExtQuote().divide(manageQuoteMap.get(RealQuoteType.EXTERNAL_PROCUREMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getExtPrice())
                            .divide(vo.getExtPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setExtActualProfitRate(BigDecimal.ZERO);
        }
        //实施利润率
        if(vo.getImpPrice().compareTo(BigDecimal.ZERO) > 0){
            vo.setImpActualProfitRate(
                    vo.getImpExtQuote().divide(manageQuoteMap.get(RealQuoteType.IMPLEMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                            .subtract(vo.getImpPrice())
                            .divide(vo.getImpPrice(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)

            );
        }else {
            vo.setImpActualProfitRate(BigDecimal.ZERO);
        }

        //是否需要计算项目利润率:
        //如果bool为true,就是从销售报价单里查来的，数据库已经存了，就不用重新计算了
        //如果bool为false，就说明是从成本报价来的数据，必须计算
        if(!inDb){
            //项目利润率(不含外采硬件)
            if(DecimalUtil.addAll(vo.getSoftWareCostTotal(),vo.getSelfPrice(),vo.getImpPrice()).compareTo(BigDecimal.ZERO) == 0){
                vo.setProjProfitRateExcl(BigDecimal.ZERO);
            }
            else {
                //拆成6个，用各自的税率算
                vo.setProjProfitRateExcl(
                        (
                                vo.getPreExtQuote().divide(manageQuoteMap.get(RealQuoteType.SALES_SUPPORT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getPrePrice())
                                        .add(
                                                vo.getDevExtQuote().divide(manageQuoteMap.get(RealQuoteType.SOFTWARE_DEVELOPMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getDevPrice())
                                        )
                                        .add(
                                                vo.getProdExtQuote().divide(manageQuoteMap.get(RealQuoteType.PRODUCT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getProdPrice())
                                        )
                                        .add(
                                                vo.getOtherExtQuote().divide(manageQuoteMap.get(RealQuoteType.OTHER.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getOtherPrice())
                                        )
                                        .add(
                                                vo.getSelfExtQuote().divide(manageQuoteMap.get(RealQuoteType.SELF_DEVELOPED.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getSelfPrice())
                                        )
                                        .add(
                                                vo.getImpExtQuote().divide(manageQuoteMap.get(RealQuoteType.IMPLEMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getImpPrice())
                                        )
                        ).divide(
                                DecimalUtil.addAll(vo.getSoftWareCostTotal(),vo.getSelfPrice(),vo.getImpPrice()),DecimalUtil.defaultScale,RoundingMode.HALF_UP
                        )
                );
            }

            //项目利润率(含外采硬件)
            if(vo.getProjCostTotal().compareTo(BigDecimal.ZERO) == 0){
                vo.setProjProfitRateIncl(BigDecimal.ZERO);
            }
            else {
                //拆成7个，用各自的税率算
                vo.setProjProfitRateIncl(
                        (
                                vo.getPreExtQuote().divide(manageQuoteMap.get(RealQuoteType.SALES_SUPPORT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getPrePrice())
                                        .add(
                                                vo.getDevExtQuote().divide(manageQuoteMap.get(RealQuoteType.SOFTWARE_DEVELOPMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getDevPrice())
                                        )
                                        .add(
                                                vo.getProdExtQuote().divide(manageQuoteMap.get(RealQuoteType.PRODUCT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getProdPrice())
                                        )
                                        .add(
                                                vo.getOtherExtQuote().divide(manageQuoteMap.get(RealQuoteType.OTHER.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getOtherPrice())
                                        )
                                        .add(
                                                vo.getSelfExtQuote().divide(manageQuoteMap.get(RealQuoteType.SELF_DEVELOPED.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getSelfPrice())
                                        )
                                        .add(
                                                vo.getExtExtQuote().divide(manageQuoteMap.get(RealQuoteType.EXTERNAL_PROCUREMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getExtPrice())
                                        )
                                        .add(
                                                vo.getImpExtQuote().divide(manageQuoteMap.get(RealQuoteType.IMPLEMENT.getCode()).getDutyRate().add(BigDecimal.valueOf(1)),DecimalUtil.defaultScale,RoundingMode.HALF_UP).subtract(vo.getImpPrice())
                                        )
                        ).divide(vo.getProjCostTotal(),DecimalUtil.defaultScale,RoundingMode.HALF_UP)
                );
            }

        }

    }
    private BigDecimal getValue(BigDecimal b){
        return b == null ? BigDecimal.ZERO : b;
    }
}
