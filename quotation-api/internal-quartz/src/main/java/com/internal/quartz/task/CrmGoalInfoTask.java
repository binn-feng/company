package com.internal.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.utils.MailUtil;
import com.internal.common.utils.bean.BeanUtils;
import com.internal.manager.domain.ManagerBuGoal;
import com.internal.manager.domain.ManagerBuGoals;
import com.internal.manager.mapper.ManagerBuGoalMapper;
import com.internal.manager.mapper.ManagerBuGoalsMapper;
import com.internal.quote.domain.QuoteSalesContract;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
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
import java.util.stream.Collectors;

/**
 * 同步目标信息
 *
 * @author every
 */
@Component("CrmGoalInfoTask")
@Slf4j
@AllArgsConstructor
public class CrmGoalInfoTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final SystemEmailMapper systemEmailMapper;
    private final ManagerBuGoalMapper managerBuGoalMapper;
    private final ManagerBuGoalsMapper managerBuGoalsMapper;


    /**
     * 同步目标信息
     */
    public void sync() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始同步目标信息---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<ManagerBuGoal> managerBuGoals = null;
        List<ManagerBuGoals> managerBuGoalsList = null;
        try {
            managerBuGoals = syncCrmMapper.syncSalesTarget();
            managerBuGoalsList = syncCrmMapper.syncSalesTargetGoal();
            log.info("managerBuGoals:{}", JSON.toJSONString(managerBuGoals));
            log.info("managerBuGoalsList:{}", JSON.toJSONString(managerBuGoalsList));
        } catch (Exception e) {
            msg = "调用crm数据库同步目标信息失败，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步目标信息失败，请查看邮件详情 " + e.getMessage());
        }
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(managerBuGoals)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = syncGoalInfo(managerBuGoals, managerBuGoalsList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" : (String) resMap.get("strDetail");
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM同步目标信息同步【成功】";
            } else {
                title = "CRM同步目标信息同步【异常】";
            }
        } else {
            title = "CRM同步目标信息同步【无数据】";
            content = "同步同步目标信息已全部成功。";
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


        log.info("--------------结束执行CRM目标信息同步---------------");

    }

    /**
     * 同步目标信息
     *
     * @param managerBuGoals 目标信息
     * @param managerBuGoalsList 目标子信息
     * @return Map<String, Object>
     */
    private Map<String, Object> syncGoalInfo(List<ManagerBuGoal> managerBuGoals, List<ManagerBuGoals> managerBuGoalsList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        StringBuilder emailTemplateBuilder = new StringBuilder();
        Set<String> goalId = new HashSet<>(); // 解决目标主表重复添加
        Set<String> goalsId = new HashSet<>(); // 解决目标明细数据重复添加
        try {
            // 获取系统已存在数据
            List<ManagerBuGoal> managerBuGoalList = managerBuGoalMapper.selectList(Wrappers.lambdaQuery());
            // 查询系统中已存在的子项目信息
            List<ManagerBuGoals> existsBuGoalList = managerBuGoalsMapper.selectList(Wrappers.lambdaQuery());
            managerBuGoals.forEach(item -> {
                errorQuoteName.set(item.getResponsePerson() + ": " + item.getTargetYear());
                // 获取对应系统中已存在的目标信息
                ManagerBuGoal managerBuGoal = managerBuGoalList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmId()) && existItem.getCrmId().equals(item.getCrmId())).findFirst().orElse(null);
                if (!goalId.contains(item.getCrmId())) {
                    if (ObjectUtil.isNotNull(managerBuGoal)) {
                        syncGoalLogic(strDetail, item, managerBuGoal);
                    } else {
                        managerBuGoal = new ManagerBuGoal();
                        fillGoalInfo(item, managerBuGoal);
                        managerBuGoalMapper.insert(managerBuGoal);
                        // 同步本地缓存列表
                        goalId.add(item.getCrmId());
                        managerBuGoalList.add(managerBuGoal);
                        strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标信息</h2>")
                                .append("<p><strong>目标信息: </strong> ").append(item.getResponsePerson()).append(":").append(item.getTargetYear()).append("</p>")
                                .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                    }
                    // 同步本地缓存列表
                    goalId.add(item.getCrmId());
                }

                // 同步子项目信息
                List<ManagerBuGoals> buGoalsList = managerBuGoalsList.stream().filter(subItem -> subItem.getCrmParentId().equals(item.getCrmId())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(buGoalsList)) {
                    ManagerBuGoal finalManagerBuGoal = managerBuGoal;
                    buGoalsList.forEach(subItem ->{
                        // 获取对应系统中已存在的目标信息
                        ManagerBuGoals buGoals = existsBuGoalList.stream().filter(existItem ->
                                ObjectUtil.isNotEmpty(existItem.getCrmId()) && existItem.getCrmId().equals(subItem.getCrmId())).findFirst().orElse(null);
                        if (!goalsId.contains(subItem.getCrmId())) {
                            if (ObjectUtil.isNotNull(buGoals)) {
                                syncGoalsLogic(strDetail, subItem, buGoals);
                            } else {
                                buGoals = new ManagerBuGoals();
                                fillGoalsInfo(subItem, buGoals);
                                buGoals.setParentId(finalManagerBuGoal.getId());
                                managerBuGoalsMapper.insert(buGoals);
                                // 同步本地缓存列表
                                goalsId.add(subItem.getCrmId());
                                existsBuGoalList.add(buGoals);
                                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标子信息</h2>")
                                        .append("<p><strong>评估类型: </strong> ").append(subItem.getAssessment()).append("</p>")
                                        .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                            }
                            // 同步本地缓存列表
                            goalsId.add(subItem.getCrmId());
                        }
                    });
                }
            });

        } catch (Exception e) {
            log.error("CRM目标信息同步异常", e);
            resultMap.put("result", Boolean.FALSE);
            strDetail.set(strDetail + errorQuoteName.get() + "该目标信息同步数据失败，请检查数据是否正确, 失败原因: " + e);
            resultMap.put("strDetail", strDetail.get());
            return resultMap;
        }
        strDetail.set(strDetail + "目标信息同步已全部成功。");
        resultMap.put("strDetail", strDetail.get());
        resultMap.put("result", Boolean.TRUE);
        return resultMap;
    }



    /**
     * 同步目标判断
     *
     * @param strDetail     描述信息
     * @param item          同步目标信息
     * @param managerBuGoal 系统目标信息
     */
    private void syncGoalLogic(AtomicReference<String> strDetail, ManagerBuGoal item, ManagerBuGoal managerBuGoal) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM商机到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(managerBuGoal.getUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(managerBuGoal.getUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillGoalInfo(item, managerBuGoal);
                managerBuGoalMapper.updateById(managerBuGoal);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标信息</h2>")
                        .append("<p><strong>名称: </strong> ").append(item.getResponsePerson()).append(":").append(item.getTargetYear()).append("</p>")
                        .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(managerBuGoal.getUpdateTime())
                    && item.getUpdateTime().getTime() != managerBuGoal.getUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillGoalInfo(item, managerBuGoal);
                managerBuGoalMapper.updateById(managerBuGoal);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标信息</h2>")
                        .append("<p><strong>名称: </strong> ").append(item.getResponsePerson()).append(":").append(item.getTargetYear()).append("</p>")
                        .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
            }
        }

    }

    /**
     * 填充基础信息
     *
     * @param item          同步目标
     * @param managerBuGoal 保存目标
     */
    private void fillGoalInfo(ManagerBuGoal item, ManagerBuGoal managerBuGoal) {
        BeanUtils.copyProperties(item, managerBuGoal);
    }


    /**
     * 同步子目标信息
     * @param strDetail     描述信息
     * @param item          同步目标信息
     * @param buGoals       同步子目标信息
     */
    private void syncGoalsLogic(AtomicReference<String> strDetail, ManagerBuGoals item, ManagerBuGoals buGoals) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM目标信息到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(buGoals.getUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(buGoals.getUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillGoalsInfo(item, buGoals);
                managerBuGoalsMapper.updateById(buGoals);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标子信息</h2>")
                        .append("<p><strong>评估类型: </strong> ").append(item.getAssessment()).append("</p>")
                        .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(buGoals.getUpdateTime())
                    && item.getUpdateTime().getTime() != buGoals.getUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillGoalsInfo(item, buGoals);
                managerBuGoalsMapper.updateById(buGoals);
                strDetail.set(strDetail + emailTemplateBuilder.append("<h2>目标子信息</h2>")
                        .append("<p><strong>评估类型: </strong> ").append(item.getAssessment()).append("</p>")
                        .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
            }
        }

    }

    /**
     * 填充子项目信息
     * @param subItem         同步子项目信息
     * @param buGoals         同步子项目信息
     */
    private void fillGoalsInfo(ManagerBuGoals subItem, ManagerBuGoals buGoals) {
        BeanUtils.copyProperties(subItem, buGoals);
    }
}
