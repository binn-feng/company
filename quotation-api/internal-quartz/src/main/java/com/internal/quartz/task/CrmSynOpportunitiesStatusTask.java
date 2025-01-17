package com.internal.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.QuoteParentStatusEnum;
import com.internal.common.utils.MailUtil;
import com.internal.quote.domain.QuoteOpportunitiesParent;
import com.internal.quote.dto.BusinessOpportunityStatusDTO;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.mapper.SyncCrmMapper;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SystemEmailMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 定时任务调度测试
 *
 * @author every
 */
@Component("CrmSynOpportunitiesStatusTask")
@Slf4j
@AllArgsConstructor
public class CrmSynOpportunitiesStatusTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemEmailMapper systemEmailMapper;


    /**
     * CRM商机同步定时任务
     */
    public void sync() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始CRM商机状态同步---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<BusinessOpportunityStatusDTO> businessOpportunityStatusDTOList = null;
        try {
            businessOpportunityStatusDTOList = syncCrmMapper.syncCrmStatusInfo();
            log.info("businessOpportunityDTOList:{}", businessOpportunityStatusDTOList);
        } catch (Exception e) {
            msg = "调用crm数据库同步状态失败，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步状态失败，请查看邮件详情 " + e.getMessage());
        }
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(businessOpportunityStatusDTOList)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = syncQuoteStatus(businessOpportunityStatusDTOList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" : (String) resMap.get("strDetail");
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM项目状态同步【成功】";
            } else {
                title = "CRM项目状态同步【异常】";
            }
        }else{
            title = "CRM项目状态同步【无数据】";
            content = "同步状态已全部成功。";
        }

        //指定收件人
        List<SysUser> receiveUser = new ArrayList<>();
        SysUser user = new SysUser();
        user.setEmail(systemConfig.getSyncProjectManagerEmail());
        user.setNickName(systemConfig.getSyncProjectManagerName());
        receiveUser.add(user);
        // 获取邮件信息
        SystemEmail systemEmail = systemEmailMapper.selectOne(Wrappers.<SystemEmail>lambdaQuery()
                .eq(SystemEmail::getType, "QUOTE")
                .ge(SystemEmail::getSendNum, 0)
                .lt(SystemEmail::getSendNum, 90).last("limit 1"));
        if(ObjectUtil.isNotEmpty(systemEmail)){
            log.info("----------开发发送同步邮件----------");
            //发送邮件
            MailUtil.sendEmailToSyncProjectManager("true", systemEmail.getHost(),
                    systemEmail.getUsername(), systemEmail.getPassword(),
                    title, msg + " <br/> " + strDetail + content,
                    false, null, true, receiveUser);
            log.info("----------开发发送同步邮件成功----------");
            systemEmail.setSendNum(systemEmail.getSendNum() + 1);
            systemEmailMapper.updateById(systemEmail);
        }else{
            log.error("邮件已无发送次数。");
        }


        log.info("--------------结束执行CRM商机状态同步---------------");

    }

    /**
     * 同步本地商机状态
     *
     * @param businessOpportunityStatusDTOList businessOpportunityStatusDTOList
     * @return Map<String, Object>
     */
    private Map<String, Object> syncQuoteStatus(List<BusinessOpportunityStatusDTO> businessOpportunityStatusDTOList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        StringBuilder emailTemplateBuilder = new StringBuilder();
        try {
            // 获取系统已存在数据
            List<QuoteOpportunitiesParent> existsQuoteOpportunitiesParentList = quoteOpportunitiesParentMapper.selectList(Wrappers.lambdaQuery());
            businessOpportunityStatusDTOList.forEach(item ->{
                errorQuoteName.set(item.getName());
                // 处理商机主表信息
                QuoteOpportunitiesParent quoteOpportunitiesParent = existsQuoteOpportunitiesParentList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmId()) && existItem.getCrmId().equals(item.getId())).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(quoteOpportunitiesParent) && !(quoteOpportunitiesParent.getCrmState().equals(item.getCurrentState())
                        && quoteOpportunitiesParent.getStatus().equals(item.getCurrentState())
                        && quoteOpportunitiesParent.getCrmUpdateTime().equals(item.getUpdateTime()))) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getName()).append("</p>")
                            .append("<p><strong>状态: </strong> 已从").append(QuoteParentStatusEnum.getInfo(quoteOpportunitiesParent.getStatus()))
                            .append("，已成功进行更新为：").append(QuoteParentStatusEnum.getInfo(item.getCurrentState())).append("</p>").toString());
                    quoteOpportunitiesParent.setCrmState(item.getCurrentState());
                    quoteOpportunitiesParent.setStatus(item.getCurrentState());
                    quoteOpportunitiesParent.setCrmUpdateTime(item.getUpdateTime());
                    quoteOpportunitiesParentMapper.updateById(quoteOpportunitiesParent);
                }
            });

        } catch (Exception e) {
            log.error("CRM商机状态同步异常", e);
            resultMap.put("result", Boolean.FALSE);
            strDetail.set(strDetail + errorQuoteName.get() + "该项目同步数据失败，请检查数据是否正确, 失败原因: " + e);
            resultMap.put("strDetail", strDetail.get());
            return resultMap;
        }
        strDetail.set(strDetail + "同步状态已全部成功。");
        resultMap.put("strDetail", strDetail.get());
        resultMap.put("result", Boolean.TRUE);
        return resultMap;
    }


}
