package com.internal.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.utils.MailUtil;
import com.internal.common.utils.bean.BeanUtils;
import com.internal.quote.domain.QuotePaymentPlan;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.mapper.QuotePaymentPlanMapper;
import com.internal.quote.mapper.QuoteSalesContractMapper;
import com.internal.quote.mapper.SyncCrmMapper;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import com.internal.system.mapper.SystemEmailMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 同步收款计划
 *
 * @author every
 */
@Component("CrmSynPaymentPlanInfoTask")
@Slf4j
@AllArgsConstructor
public class CrmSynPaymentPlanInfoTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemEmailMapper systemEmailMapper;
    private final SysUserMapper sysUserMapper;
    private final SysDeptMapper sysDeptMapper;
    private final QuotePaymentPlanMapper quotePaymentPlanMapper;

    /**
     * CRM用户同步定时任务
     */
    public void syncPlan() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始同步收款计划信息---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<QuotePaymentPlan> quotePaymentPlanList = null;
        try {
            quotePaymentPlanList = syncCrmMapper.syncPaymentPlan();
            log.info("quotePaymentPlanList:{}", quotePaymentPlanList);
        } catch (Exception e) {
            msg = "调用crm数据库同步收款计划，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步收款计划，请查看邮件详情 " + e.getMessage());
        }
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(quotePaymentPlanList)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = syncPaymentPlan(quotePaymentPlanList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" : (String) resMap.get("strDetail");
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM收款计划同步【成功】";
            } else {
                title = "CRM收款计划同步【异常】";
            }
        } else {
            title = "CRM收款计划同步【无数据】";
            content = "同步收款计划已全部成功。";
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
        if (ObjectUtil.isNotEmpty(systemEmail)) {
            log.info("----------开发发送同步邮件----------");
            //发送邮件
            MailUtil.sendEmailToSyncProjectManager("true", systemEmail.getHost(),
                    systemEmail.getUsername(), systemEmail.getPassword(),
                    title, msg + " <br/> " + strDetail + content,
                    false, null, true, receiveUser);
            log.info("----------开发发送同步邮件成功----------");
            systemEmail.setSendNum(systemEmail.getSendNum() + 1);
            systemEmailMapper.updateById(systemEmail);
        } else {
            log.error("邮件已无发送次数。");
        }


        log.info("--------------结束执行CRM用户信息同步---------------");

    }

    /**
     * 同步用户信息
     *
     * @param quotePaymentPlanList quotePaymentPlanList
     * @return Map<String, Object>
     */
    private Map<String, Object> syncPaymentPlan(List<QuotePaymentPlan> quotePaymentPlanList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        StringBuilder emailTemplateBuilder = new StringBuilder();
        Set<String> planId = new HashSet<>(); // 解决回款计划重复添加
        try {
            // 获取系统已存在数据
            List<QuotePaymentPlan> existsQuotePaymentPlanList =  quotePaymentPlanMapper.selectList(Wrappers.lambdaQuery());
            quotePaymentPlanList.forEach(item -> {
                errorQuoteName.set(item.getCrmPaymentPlanId());
                // 获取对应系统中的收款计划
                QuotePaymentPlan quotePaymentPlan = existsQuotePaymentPlanList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmPaymentPlanId()) && existItem.getCrmPaymentPlanId().equals(item.getCrmPaymentPlanId())).findFirst().orElse(null);
                if (!planId.contains(item.getCrmPaymentPlanId())) {
                    if (ObjectUtil.isNotNull(quotePaymentPlan)) {
                        syncPaymentPlanLogic(strDetail, item, quotePaymentPlan);
                    } else {
                        quotePaymentPlan = new QuotePaymentPlan();
                        fillPaymentPlanInfo(item, quotePaymentPlan);
                        quotePaymentPlanMapper.insert(quotePaymentPlan);
                        // 同步本地缓存列表
                        planId.add(item.getCrmPaymentPlanId());
                        existsQuotePaymentPlanList.add(quotePaymentPlan);
                        strDetail.set(strDetail + emailTemplateBuilder.append("<h2>收款计划信息</h2>")
                                .append("<p><strong>合同号: </strong> ").append(item.getContractNumber()).append("</p>")
                                .append("<p><strong>回款笔次: </strong> ").append(item.getPaymentInstallment()).append("次</p>")
                                .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                    }
                    // 同步本地缓存列表
                    planId.add(item.getCrmPaymentPlanId());
                }
            });

        } catch (Exception e) {
            log.error("CRM用户信息同步异常", e);
            resultMap.put("result", Boolean.FALSE);
            strDetail.set(strDetail + errorQuoteName.get() + "该用户同步数据失败，请检查数据是否正确, 失败原因: " + e);
            resultMap.put("strDetail", strDetail.get());
            return resultMap;
        }
        strDetail.set(strDetail + "用户信息同步已全部成功。");
        resultMap.put("strDetail", strDetail.get());
        resultMap.put("result", Boolean.TRUE);
        return resultMap;
    }

    /**
     * 同步用户判断
     *
     * @param strDetail 描述信息
     * @param item      同步用户
     * @param quotePaymentPlan   系统用户
     */
    private void syncPaymentPlanLogic(AtomicReference<String> strDetail, QuotePaymentPlan item, QuotePaymentPlan quotePaymentPlan) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM商机到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(quotePaymentPlan.getUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(quotePaymentPlan.getUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillPaymentPlanInfo(item, quotePaymentPlan);
                quotePaymentPlanMapper.updateById(quotePaymentPlan);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>收款计划信息</h2>")
                        .append("<p><strong>合同号: </strong> ").append(item.getContractNumber()).append("</p>")
                        .append("<p><strong>回款笔次: </strong> ").append(item.getPaymentInstallment()).append("次</p>")
                        .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(quotePaymentPlan.getUpdateTime())
                    && item.getUpdateTime().getTime() != quotePaymentPlan.getUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillPaymentPlanInfo(item, quotePaymentPlan);
                quotePaymentPlanMapper.updateById(quotePaymentPlan);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>收款计划信息</h2>")
                        .append("<p><strong>合同号: </strong> ").append(item.getContractNumber()).append("</p>")
                        .append("<p><strong>回款笔次: </strong> ").append(item.getPaymentInstallment()).append("次</p>")
                        .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
            }
        }

    }

    /**
     * 填充基础信息
     *
     * @param item    同步用户
     * @param sysUser 保存用户
     */
    private void fillPaymentPlanInfo(QuotePaymentPlan item, QuotePaymentPlan sysUser) {
        BeanUtils.copyProperties(item, sysUser);
    }
}
