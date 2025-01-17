package com.internal.quote.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.util.StringUtil;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.*;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DecimalUtil;
import com.internal.common.utils.ThreadUtil;
import com.internal.quote.domain.*;
import com.internal.quote.dto.*;
import com.internal.quote.mapper.*;
import com.internal.quote.service.IQuoteOpportunitiesService;
import com.internal.quote.service.IQuotePresaleInfoService;
import com.internal.quote.util.QuoteMailUtil;
import com.internal.quote.vo.OpportunitiesDetailVersionVO;
import com.internal.quote.vo.QuoteSignInfoVO;
import com.internal.quote.vo.SignDetailInfoVO;
import com.internal.system.mapper.SysUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.internal.quote.service.IQuoteSignInfoService;

/**
 * 报价系统-待签合同Service业务层处理
 * 
 * @author internal
 * @date 2024-11-14
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuoteSignInfoServiceImpl extends ServiceImpl<QuoteSignInfoMapper, QuoteSignInfo> implements IQuoteSignInfoService
{

    private final QuoteSignInfoMapper quoteSignInfoMapper;
    private final QuotePresaleInfoMapper presaleInfoMapper;
    private final QuoteOpportunitiesMapper quoteMapper;
    private final QuoteOpportunitiesParentMapper quoteParentMapper;
    private final IQuotePresaleInfoService presaleInfoService;
    private final IQuoteOpportunitiesService quoteService;
    private final QuoteOpportunitiesParentMapper parentMapper;
    private final SyncCrmMapper syncCrmMapper;
    private final QuoteEmailSettingMapper quoteEmailSettingMapper;
    private final SysUserMapper sysUserMapper;
    private final QuoteMailUtil quoteMailUtil;
    private final SystemConfig systemConfig;

    /**
     * 查询报价系统-待签合同
     * 
     * @param id 报价系统-待签合同主键
     * @return 报价系统-待签合同
     */
    @Override
    public QuoteSignInfo selectQuoteSignInfoById(Long id)
    {
        return quoteSignInfoMapper.selectQuoteSignInfoById(id);
    }

    /**
     * 查询报价系统-待签合同列表
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 报价系统-待签合同
     */
    @Override
    public List<QuoteSignInfo> selectQuoteSignInfoList(QuoteSignInfo quoteSignInfo)
    {
        return quoteSignInfoMapper.selectQuoteSignInfoList(quoteSignInfo);
    }

    /**
     * 新增报价系统-待签合同
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 结果
     */
    @Override
    public int insertQuoteSignInfo(QuoteSignInfo quoteSignInfo)
    {
        quoteSignInfo.setCreateTime(DateUtils.getNowDate());
        return quoteSignInfoMapper.insert(quoteSignInfo);
    }

    /**
     * 修改报价系统-待签合同
     * 
     * @param quoteSignInfo 报价系统-待签合同
     * @return 结果
     */
    @Override
    public int updateQuoteSignInfo(QuoteSignInfo quoteSignInfo)
    {
        quoteSignInfo.setUpdateTime(DateUtils.getNowDate());
        return quoteSignInfoMapper.updateById(quoteSignInfo);
    }

    /**
     * 批量删除报价系统-待签合同
     * 
     * @param ids 需要删除的报价系统-待签合同主键
     * @return 结果
     */
    @Override
    public int deleteQuoteSignInfoByIds(Long[] ids)
    {
        return quoteSignInfoMapper.deleteQuoteSignInfoByIds(ids);
    }

    /**
     * 删除报价系统-待签合同信息
     * 
     * @param id 报价系统-待签合同主键
     * @return 结果
     */
    @Override
    public int deleteQuoteSignInfoById(Long id)
    {
        return quoteSignInfoMapper.deleteQuoteSignInfoById(id);
    }

    @Override
    public SignDetailInfoVO getSignInfoById(BaseDTO dto) {
        QuoteSignInfo signInfo = baseMapper.selectById(dto.getId());
        if(ObjectUtil.isEmpty(signInfo)){
            return null;
        }

        return BeanUtil.toBean(signInfo,SignDetailInfoVO.class);
    }
    @Override
    public SignDetailInfoVO getSignInfoByOpportunitiesId(BaseDTO dto) {
        QuoteSignInfo signInfo = baseMapper.selectOne(Wrappers.<QuoteSignInfo>lambdaQuery()
                .eq(QuoteSignInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuoteSignInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        if(ObjectUtil.isEmpty(signInfo)){
            return null;
        }
        return BeanUtil.toBean(signInfo,SignDetailInfoVO.class);
    }

    @Override
    public SignDetailInfoVO getReSignInfo(BaseDTO dto) {
        SignDetailInfoVO vo = new SignDetailInfoVO();
        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(dto.getOpportunitiesId());
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
        presaleInfoService.fillQuoteInfo(vo,quote.getType(),currentVersion.getId(),true);
        //填充上一版签约申请报价信息
        QuoteSignInfo signInfo = baseMapper.selectOne(Wrappers.<QuoteSignInfo>lambdaQuery()
                .in(QuoteSignInfo::getOpportunitiesId,quoteIdList)
                .eq(QuoteSignInfo::getStatus,VersionStatus.ACTIVE.getCode()));
        if(ObjectUtil.isEmpty(signInfo)){
            throw new RuntimeException("无效的签约申请信息");
        }
        vo.setSoftWareExtQuote(signInfo.getSoftWareExtQuote());
        vo.setProdExtQuote(signInfo.getProdExtQuote());
        vo.setOtherExtQuote(signInfo.getOtherExtQuote());
        vo.setSelfExtQuote(signInfo.getSelfExtQuote());
        vo.setExtExtQuote(signInfo.getExtExtQuote());
        vo.setImpExtQuote(signInfo.getImpExtQuote());
        vo.setContractType(signInfo.getContractType());
        vo.setSignType(signInfo.getContractType());
        //计算其它数据
        presaleInfoService.fillApprovalAndSignInfo(vo,false);

        return vo;
    }

    @Override
    @DSTransactional()
    public Boolean signApplication(SignApplicationSaveDTO vo) {
        QuoteSignInfo info = BeanUtil.toBean(vo,QuoteSignInfo.class);

        //获取最新的quote
        List<QuoteOpportunities> aboutQuoteList = quoteService.getAboutQuoteList(vo.getOpportunitiesId());
        Set<Long> quoteIdLIst = aboutQuoteList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        QuoteOpportunities quote = aboutQuoteList.stream().filter(x -> !QuoteStatusEnum.ABANDON.getCode().equals(x.getStatus())).findFirst().orElse(null);
        if(ObjectUtil.isEmpty(quote)){
            throw new RuntimeException("无效的报价");
        }

        info.setId(null);
        info.setOpportunitiesId(quote.getId());
        info.setContractType(vo.getContractType());
        info.setSignDesc(vo.getSignDesc());
        //绑定销售报价单
        info.setStatus(VersionStatus.ACTIVE.getCode());
        info.setCreateTime(DateTime.now());
        info.setUpdateTime(DateTime.now());
        //修改审核状态和审核时间
        quoteMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                .set(QuoteOpportunities::getStatus, QuoteStatusEnum.SIGN_PENDING_APPROVAL.getCode())
                .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                .set(QuoteOpportunities::getSignAuditTime, null)
                .set(QuoteOpportunities::getSignAuditLog, "")
                .eq(QuoteOpportunities::getId,quote.getId()));
        //把所有签约申请的状态都弄成未生效
        baseMapper.update(null,Wrappers.<QuoteSignInfo>lambdaUpdate()
                .set(QuoteSignInfo::getStatus,VersionStatus.INACTIVE.getCode())
                .in(QuoteSignInfo::getOpportunitiesId,quoteIdLIst));
        baseMapper.insert(info);
        // 发送邮件
        ThreadUtil.runInThread(() -> {
            sendSingEmail(vo);
        });

        // 同步CRM 阶段为谈判
        if(ObjectUtil.isNotEmpty(quote)){
            QuoteOpportunitiesParent quoteOpportunitiesParent = parentMapper.selectOne(Wrappers.<QuoteOpportunitiesParent>lambdaQuery()
                    .eq(QuoteOpportunitiesParent::getId, quote.getOpportunitiesParentId()));
            if(ObjectUtil.isNotEmpty(quoteOpportunitiesParent)){
                syncCrmMapper.updateCrmStage(BusinessOpportunityUpdateDTO.builder()
                        .id(String.valueOf(quoteOpportunitiesParent.getCrmId()))
                        .stage(CrmQuoteStateEnum.NEGOTIATION.getCode())
                        .build());
            }
        }
        return true;
    }


    /*
    private SignDetailInfoVO fillSignInfo(QuoteSignInfo signInfo){
        ApprovalAndSignInfoVO approvalAndSignInfoVO = presaleInfoService.approvalAndSignInfo(signInfo.getPresaleId());
        if(ObjectUtil.isEmpty(approvalAndSignInfoVO)){
            return null;
        }
        SignDetailInfoVO vo = BeanUtil.toBean(approvalAndSignInfoVO,SignDetailInfoVO.class);
        vo.setSignDesc(signInfo.getSignDesc());
        vo.setContractType(signInfo.getContractType());
        return vo;
    }
    */
    @Override
    public QuoteSignInfoVO getStatusUpdatePageInfo(BaseDTO dto) {
        QuoteSignInfo info = baseMapper.selectOne(Wrappers.<QuoteSignInfo>lambdaQuery()
                .eq(QuoteSignInfo::getOpportunitiesId,dto.getOpportunitiesId())
                .eq(QuoteSignInfo::getStatus, VersionStatus.ACTIVE.getCode())
        );
        if(ObjectUtil.isEmpty(info)){
            return null;
        }
        QuoteSignInfoVO vo = BeanUtil.toBean(info,QuoteSignInfoVO.class);
        return vo;
    }

    @Override
    @DSTransactional()
    public Boolean updateStatus(SignInfoUpdateDTO dto) {
        QuoteSignInfo info = baseMapper.selectById(dto.getId());
        if(ObjectUtil.isEmpty(info)){
            return false;
        }
        QuoteOpportunities entity = quoteMapper.selectById(info.getOpportunitiesId());
        if(ObjectUtil.isEmpty(entity)){
            return false;
        }
        Boolean deal = false;
        if(ContractType.XX.getCode().equals(dto.getContractType())) {
            if (Boolean.TRUE.equals(dto.getXxVsCust()) && Boolean.TRUE.equals(dto.getXxVsNl())) {
                deal = true;
            }
            baseMapper.update(null, Wrappers.<QuoteSignInfo>lambdaUpdate()
                    .eq(QuoteSignInfo::getId, dto.getId())
                    .set(QuoteSignInfo::getXxVsCust, dto.getXxVsCust())
                    .set(QuoteSignInfo::getXxVsNl, dto.getXxVsNl())
            );
        }
        else if(ContractType.NL.getCode().equals(dto.getContractType())) {
            if(Boolean.TRUE.equals(dto.getNlVsCust())){
                deal = true;
            }
            baseMapper.update(null,Wrappers.<QuoteSignInfo>lambdaUpdate()
                    .eq(QuoteSignInfo::getId,dto.getId())
                    .set(QuoteSignInfo::getNlVsCust,dto.getNlVsCust())
            );
        }
        if(deal){
            //把该商机的所有报价都成交（除了废弃的）
            quoteMapper.update(null,Wrappers.<QuoteOpportunities>lambdaUpdate()
                    .eq(QuoteOpportunities::getOpportunitiesParentId,entity.getOpportunitiesParentId())
                    .set(QuoteOpportunities::getUpdateTime,DateTime.now())
                    .ne(QuoteOpportunities::getStatus,QuoteStatusEnum.ABANDON.getCode())
                    .set(QuoteOpportunities::getStatus, QuoteStatusEnum.SIGNED.getCode()));
            parentMapper.update(null,Wrappers.<QuoteOpportunitiesParent>lambdaUpdate()
                    .eq(QuoteOpportunitiesParent::getId,entity.getOpportunitiesParentId())
                    .set(QuoteOpportunitiesParent::getStatus, QuoteParentStatusEnum.SUCCESS.getCode())
            );
            // 同步CRM 阶段为成交
            QuoteOpportunities quoteOpportunities = quoteMapper.selectOne(Wrappers.<QuoteOpportunities>lambdaQuery()
                    .eq(QuoteOpportunities::getId, info.getOpportunitiesId()));
            if(ObjectUtil.isNotEmpty(quoteOpportunities)){
                /*
                Set<Long> quoteIdList = quoteMapper.selectList(Wrappers.<QuoteOpportunities>lambdaQuery()
                        .eq(QuoteOpportunities::getOpportunitiesParentId, quoteOpportunities.getOpportunitiesParentId()))
                        .stream().map(QuoteOpportunities::getOpportunitiesParentId).collect(Collectors.toSet());
                */
                QuoteOpportunitiesParent quoteOpportunitiesParent = parentMapper.selectOne(Wrappers.<QuoteOpportunitiesParent>lambdaQuery()
                        .eq(QuoteOpportunitiesParent::getId, quoteOpportunities.getOpportunitiesParentId()));
                if(ObjectUtil.isNotEmpty(quoteOpportunitiesParent)){
                    //把所有的version状态改成未生效
                    /*quoteService.setAllVersionStatusByParent(quoteOpportunitiesParent.getId(),VersionStatus.INACTIVE);
                    presaleInfoMapper.update(null,Wrappers.<QuotePresaleInfo>lambdaUpdate()
                            .set(QuotePresaleInfo::getStatus,VersionStatus.INACTIVE.getCode())
                            .in(QuotePresaleInfo::getOpportunitiesId,quoteIdList));*/
                    syncCrmMapper.updateCrmStage(BusinessOpportunityUpdateDTO.builder()
                            .id(String.valueOf(quoteOpportunitiesParent.getCrmId()))
                            .stage(CrmQuoteStateEnum.DEAL.getCode())
                            .build());
                }
            }
        }

        return true;
    }

    /**
     * 发送邮件
     * @param saveDTO signApplicationSaveDTO
     */
    private void sendSingEmail(SignApplicationSaveDTO saveDTO) {
        log.info("----------开始发送签约申请邮件----------");
        SendEmailInfoDTO sendEmailInfoDTO = new SendEmailInfoDTO();
        EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO = new EmailSubjectInfoConvertDTO();
        EmailModelInfoConvertDTO emailModelInfoConvertDTO = new EmailModelInfoConvertDTO();
        List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery());
        // 设置收件人
        QuoteEmailSetting quoteEmailSetting = quoteEmailSettingMapper.selectOne(Wrappers.<QuoteEmailSetting>lambdaQuery()
                .eq(QuoteEmailSetting::getType, EmailTypeEnum.SIGNING_APPLICATION.getCode()));
        List<SysUser> sysuUserList =  new ArrayList<>();
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
        sysuUserList.forEach(item ->{
            // 设置发送人
            sendEmailInfoDTO.setSysUser(item);
            QuoteOpportunities quoteOpportunities = quoteMapper.selectById(saveDTO.getOpportunitiesId());
            if(ObjectUtil.isNotEmpty(quoteOpportunities)){
                sysUserList.stream()
                        .filter(sysUser -> ObjectUtil.isNotEmpty(sysUser.getCrmUserId()) && quoteOpportunities.getSupportPerson().contains(sysUser.getCrmUserId()))
                        .findFirst().ifPresent(sysUser ->{
                    emailModelInfoConvertDTO.setPreSaleUserName(sysUser.getNickName());
                });
            }
            // 获取所属销售
            QuoteOpportunitiesParent quoteOpportunitiesParent = quoteParentMapper.selectById(quoteOpportunities.getOpportunitiesParentId());
            if(ObjectUtil.isNotEmpty(quoteOpportunitiesParent)){
                sysUserList.stream().filter(sysUser -> sysUser.getUserId().equals(quoteOpportunitiesParent.getSaleId())).findFirst().ifPresent(sysUser -> {
                    emailModelInfoConvertDTO.setPreSaleUser(sysUser.getNickName());
                    // 设置抄送人
                    sendEmailInfoDTO.setCopyUser(new ArrayList<SysUser>(){{add(sysUser);}});
                });
                // 设置商机主题
                emailSubjectInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
                emailSubjectInfoConvertDTO.setCurrentDate(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
                emailModelInfoConvertDTO.setBusinessSubject(quoteOpportunitiesParent.getName());
            }

            // 设置模版信息
            emailModelInfoConvertDTO.setCostTotal(DecimalUtil.getStr(saveDTO.getTotalCost(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setNorthTotalAmount(DecimalUtil.getStr(saveDTO.getNorthAmount(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setNorthQuoteAmount(DecimalUtil.getStr(saveDTO.getNorthQuoteAmount(),DecimalUtil.displayScale));
            emailModelInfoConvertDTO.setCostProfitRate(DecimalUtil.getStr(saveDTO.getCostProfitRate(),DecimalUtil.displayScale,true));
            emailModelInfoConvertDTO.setTotalCostProfitRate(DecimalUtil.getStr(saveDTO.getTotalCostProfitRate(),DecimalUtil.displayScale,true));
            emailModelInfoConvertDTO.setSignApprovalDetailPic(saveDTO.getSignImg());
            emailModelInfoConvertDTO.setContinueSignReason(StringUtil.isEmpty(saveDTO.getSignDesc()) ? "无" : saveDTO.getSignDesc());
            emailModelInfoConvertDTO.setQuoteSystemLink(systemConfig.getDomainName());

            sendEmailInfoDTO.setEmailModelInfoConvertDTO(emailModelInfoConvertDTO);
            sendEmailInfoDTO.setEmailSubjectInfoConvertDTO(emailSubjectInfoConvertDTO);
            quoteMailUtil.sendEmail(Collections.singletonList(sendEmailInfoDTO), EmailTypeEnum.SIGNING_APPLICATION.getCode());
        });
        log.info("----------签约申请邮件发送完毕----------");

    }

}
