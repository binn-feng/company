package com.internal.quartz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SystemConfig;
import com.internal.common.utils.MailUtil;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
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
 * 同步CRM部门、部门、角色信息
 *
 * @author every
 */
@Component("CrmSynDeptInfoTask")
@Slf4j
@AllArgsConstructor
public class CrmSynDeptInfoTask {

    private final SyncCrmMapper syncCrmMapper;
    private final SystemConfig systemConfig;
    private final SystemEmailMapper systemEmailMapper;
    private final SysDeptMapper sysDeptMapper;


    /**
     * CRM部门同步定时任务
     */
    public void syncDept() throws MessagingException, GeneralSecurityException {
        log.info("--------------开始同步CRM部门信息---------------");
        // 定义邮件相关信息
        String title = "";
        String msg = "";
        String strDetail = "";
        String content = "";
        boolean result = true;

        List<SysDept> sysDeptList = null;
        try {
            sysDeptList = syncCrmMapper.syncCrmDeptInfo();
            log.info("sysUserList:{}", sysDeptList);
        } catch (Exception e) {
            msg = "调用crm数据库同步部门失败，请查看邮件详情 ";
            strDetail = e.getMessage();
            log.error("调用crm数据库同步部门失败，请查看邮件详情 " + e.getMessage());
        }
        // 开始设置同步信息
        if (CollUtil.isNotEmpty(sysDeptList)) {
            //同步本地的项目字典
            try {
                Map<String, Object> resMap = syncDept(sysDeptList);
                result = (Boolean) resMap.get("result");
                strDetail = resMap.get("strDetail") == null ? "" : (String) resMap.get("strDetail");
            } catch (Exception e) {
                msg = "同步出现异常，请查看邮件详情 ";
                strDetail = strDetail + e.getMessage();
            }

            if (ObjectUtil.isNotEmpty(result) && result) {
                title = "CRM部门信息同步【成功】";
            } else {
                title = "CRM部门信息同步【异常】";
            }
        } else {
            title = "CRM部门信息同步【无数据】";
            content = "同步部门信息已全部成功。";
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


        log.info("--------------结束执行CRM部门信息同步---------------");

    }

    /**
     * 同步部门信息
     *
     * @param businessOpportunityStatusDTOList businessOpportunityStatusDTOList
     * @return Map<String, Object>
     */
    private Map<String, Object> syncDept(List<SysDept> businessOpportunityStatusDTOList) {
        // 公共参数
        AtomicReference<String> errorQuoteName = new AtomicReference<>("");
        Map<String, Object> resultMap = new HashMap<>();
        AtomicReference<String> strDetail = new AtomicReference<>("");
        StringBuilder emailTemplateBuilder = new StringBuilder();
        Set<String> deptId = new HashSet<>(); // 解决部门重复添加
        try {
            // 获取系统已存在数据
            List<SysDept> sysDeptList = sysDeptMapper.selectList(Wrappers.lambdaQuery());
            businessOpportunityStatusDTOList.forEach(item -> {
                errorQuoteName.set(item.getDeptName());
                // 获取对应系统中的部门
                SysDept sysDept = sysDeptList.stream().filter(existItem ->
                        ObjectUtil.isNotEmpty(existItem.getCrmDeptId()) && existItem.getCrmDeptId().equals(item.getCrmDeptId())).findFirst().orElse(null);
                if (!deptId.contains(item.getCrmDeptId())) {
                    if (ObjectUtil.isNotNull(sysDept)) {
                        syncDeptLogic(strDetail, item, sysDept);
                    } else {
                        sysDept = new SysDept();
                        fillDeptInfo(item, sysDept);
                        sysDeptMapper.insert(sysDept);
                        // 同步本地缓存列表
                        deptId.add(item.getCrmDeptId());
                        sysDeptList.add(sysDept);
                        strDetail.set(strDetail + emailTemplateBuilder.append("<h2>部门信息</h2>")
                                .append("<p><strong>名称: </strong> ").append(item.getDeptName()).append("</p>")
                                .append("<p><strong>状态: </strong> 已成功进行新增</p>").toString());
                    }
                    // 同步本地缓存列表
                    deptId.add(item.getCrmDeptId());
                }
            });

        } catch (Exception e) {
            log.error("CRM部门信息同步异常", e);
            resultMap.put("result", Boolean.FALSE);
            strDetail.set(strDetail + errorQuoteName.get() + "该部门同步数据失败，请检查数据是否正确, 失败原因: " + e);
            resultMap.put("strDetail", strDetail.get());
            return resultMap;
        }
        strDetail.set(strDetail + "部门信息同步已全部成功。");
        resultMap.put("strDetail", strDetail.get());
        resultMap.put("result", Boolean.TRUE);
        return resultMap;
    }

    /**
     * 同步部门判断
     *
     * @param strDetail 描述信息
     * @param item      同步部门
     * @param sysDept   系统部门
     */
    private void syncDeptLogic(AtomicReference<String> strDetail, SysDept item, SysDept sysDept) {
        StringBuilder emailTemplateBuilder = new StringBuilder();
        // 判断是否需要同步CRM商机到本地
        if (ObjectUtil.isNotEmpty(item.getUpdateTime()) || ObjectUtil.isNotEmpty(sysDept.getUpdateTime())) {
            if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isEmpty(sysDept.getUpdateTime())) {
                // 当CRM时间不为null且本地时间为null，更新本地商机
                fillDeptInfo(item, sysDept);
                sysDeptMapper.updateById(sysDept);
                if (!strDetail.toString().contains(item.getDeptName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getDeptName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于本地时间为空同时CRM时间不为空，已进行数据更新</p>").toString());
                }
            } else if (ObjectUtil.isNotEmpty(item.getUpdateTime()) && ObjectUtil.isNotEmpty(sysDept.getUpdateTime())
                    && item.getUpdateTime().getTime() != sysDept.getUpdateTime().getTime()) {
                // 当CRM时间和本地时间都不为null，且CRM时间不等于本地时间，更新本地商机
                fillDeptInfo(item, sysDept);
                sysDeptMapper.updateById(sysDept);
                if (!strDetail.toString().contains(item.getDeptName())) {
                    strDetail.set(strDetail + emailTemplateBuilder.append("<h2>商机信息</h2>")
                            .append("<p><strong>名称: </strong> ").append(item.getDeptName()).append("</p>")
                            .append("<p><strong>状态: </strong> 由于数据时间不一致，已进行数据更新</p>").toString());
                }
            }
        }


    }

    /**
     * 填充基础信息
     *
     * @param item    同步部门
     * @param sysDept 保存部门
     */
    private void fillDeptInfo(SysDept item, SysDept sysDept) {
        sysDept.setCrmDeptId(item.getCrmDeptId());
        sysDept.setDeptName(item.getDeptName());
        sysDept.setUpdateTime(item.getUpdateTime());
        sysDept.setStatus(item.getStatus());
        if (item.getParentId() == 0) {
            sysDept.setParentId(0L);
            sysDept.setAncestors("0");
        } else {
            SysDept exisysDept = sysDeptMapper.selectOne(Wrappers.lambdaQuery(SysDept.class).eq(SysDept::getCrmDeptId, item.getAncestors()));
            if (ObjectUtil.isNotEmpty(sysDept)) {
                sysDept.setParentId(exisysDept.getDeptId());
                sysDept.setAncestors("0" + "," + exisysDept.getDeptId());
            }
        }
    }

}
