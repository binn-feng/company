package com.internal.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.enums.UserStatus;
import com.internal.common.utils.MailUtil;
import com.internal.common.utils.SecurityUtils;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.mapper.SyncCrmMapper;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SysDeptMapper;
import com.internal.system.mapper.SysUserMapper;
import com.internal.system.mapper.SystemEmailMapper;
import com.internal.system.service.ISysConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 同步CRM用户、部门、角色信息
 *
 * @author every
 */
@Component("CrmSynUserInfoTask")
@Slf4j
@AllArgsConstructor
public class CrmSynUserInfoTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;
    private final SystemEmailMapper systemEmailMapper;
    private final SysUserMapper sysUserMapper;
    private final SysDeptMapper sysDeptMapper;
    private final ISysConfigService configService;


    /**
     * CRM用户同步定时任务
     */
    public void syncUser() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始同步CRM用户、部门信息---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<SysUser> sysUserList = null;
        try {
            sysUserList = syncCrmMapper.syncCrmUserInfo();
            log.info("sysUserList:{}", sysUserList);
        } catch (Exception e) {
            msg = "调用crm数据库同步用户失败，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步用户失败，请查看邮件详情 " + e.getMessage());
        }
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(sysUserList)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = syncUser(sysUserList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" : (String) resMap.get("strDetail");
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM用户信息同步【成功】";
            } else {
                title = "CRM用户信息同步【异常】";
            }
        } else {
            title = "CRM用户信息同步【无数据】";
            content = "同步用户信息已全部成功。";
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
     * @param businessOpportunityStatusDTOList businessOpportunityStatusDTOList
     * @return Map<String, Object>
     */
    private Map<String, Object> syncUser(List<SysUser> businessOpportunityStatusDTOList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        StringBuilder emailTemplateBuilder = new StringBuilder();
        Set<String> userId = new HashSet<>(); // 解决用户重复添加
        try {
            // 获取系统已存在数据
            List<SysUser> sysUserList = sysUserMapper.selectList(Wrappers.lambdaQuery());
            businessOpportunityStatusDTOList.forEach(item -> {
                errorQuoteName.set(item.getNickName());
                // 获取对应系统中的用户
                SysUser sysUser = sysUserList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmUserId()) && existItem.getCrmUserId().equals(item.getCrmUserId())).findFirst().orElse(null);
                if (!userId.contains(item.getCrmUserId())) {
                    if (ObjectUtil.isNotNull(sysUser)) {
                        syncUserLogic(strDetail, item, sysUser);
                    } else {
                        sysUser = new SysUser();
                        fillUserInfo(item, sysUser);
                        sysUser.setUserType("01");
                        String password = configService.selectConfigByKey("sys.user.initPassword");
                        sysUser.setPassword(SecurityUtils.encryptPassword(password));
                        sysUserMapper.insert(sysUser);
                        // 同步本地缓存列表
                        userId.add(item.getCrmUserId());
                        sysUserList.add(sysUser);
                        strDetail.set(strDetail + emailTemplateBuilder.append("<h2>用户信息</h2>")
                                .append("<p><strong>名称: </strong> ").append(item.getNickName()).append("</p>")
                                .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                    }
                    // 同步本地缓存列表
                    userId.add(item.getCrmUserId());
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
     * @param sysUser   系统用户
     */
    private void syncUserLogic(AtomicReference<String> strDetail, SysUser item, SysUser sysUser) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM商机到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(sysUser.getUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(sysUser.getUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillUserInfo(item, sysUser);
                sysUserMapper.updateById(sysUser);
                if (!strDetail.toString().contains(item.getNickName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getNickName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
                }
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(sysUser.getUpdateTime())
                    && item.getUpdateTime().getTime() != sysUser.getUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillUserInfo(item, sysUser);
                sysUserMapper.updateById(sysUser);
                if (!strDetail.toString().contains(item.getNickName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getNickName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
                }
            }
        }


    }

    /**
     * 填充基础信息
     *
     * @param item    同步用户
     * @param sysUser 保存用户
     */
    private void fillUserInfo(SysUser item, SysUser sysUser) {
        sysUser.setCrmUserId(item.getCrmUserId());
        sysUser.setNickName(item.getNickName());
        sysUser.setUserName(item.getUserName());
        sysUser.setEmail(item.getEmail());
        sysUser.setSex(item.getSex());
        sysUser.setUpdateTime(item.getUpdateTime());
        sysUser.setStatus(item.getStatus());
        sysUser.setDelFlag(item.getStatus().equals(UserStatus.DISABLE.getCode()) ? "2" : "0");
        SysDept sysDept = sysDeptMapper.selectOne(Wrappers.lambdaQuery(SysDept.class).eq(SysDept::getCrmDeptId, item.getCrmDeptId()));
        if (ObjectUtil.isNotEmpty(sysDept)) {
            sysUser.setDeptId(sysDept.getDeptId());
        }

    }
}
