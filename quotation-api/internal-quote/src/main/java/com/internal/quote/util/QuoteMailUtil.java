package com.internal.quote.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.dto.SendEmailInfoDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SendEmailBaseConfig;
import com.internal.common.email.SystemConfig;
import com.internal.common.utils.MailUtil;
import com.internal.quote.domain.QuoteEmailSetting;
import com.internal.quote.mapper.QuoteEmailSettingMapper;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SysUserMapper;
import com.internal.system.mapper.SystemEmailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * @author zdliu
 */
@Component
@Slf4j
public class QuoteMailUtil {

    @Value("${system.send.email.nickname}")
    private String nickname;


    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private QuoteEmailSettingMapper quoteEmailSettingMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SystemEmailMapper systemEmailMapper;


    /**
     *
     * @param flag smtp是否需要认证
     * @param host 邮件服务器主机名
     * @param username 用户名
     * @param password 密码
     * @param cc  是否抄送
     * @param copyUser  抄送人
     * @param to  是否发送
     * @param receiveUser  接收人
     * @param emailModelInfoConvertDTO  邮箱模版占位符转换信息
     * @param emSubjectInfoConvertDTO  邮箱主题占位符转换信息
     */
    public void sendEmailToAllUser(String flag, String host, String username, String password,
                                          String title, String content,
                                          Boolean cc, List<SysUser> copyUser,
                                          Boolean to, List<SysUser> receiveUser,
                                          EmailModelInfoConvertDTO emailModelInfoConvertDTO,
                                          EmailSubjectInfoConvertDTO emSubjectInfoConvertDTO) throws MessagingException, GeneralSecurityException {

        SendEmailBaseConfig config = new SendEmailBaseConfig(flag,host,username,password,title,content,false,
                cc,copyUser,null,to,receiveUser,null,emailModelInfoConvertDTO,emSubjectInfoConvertDTO);
        config.sendEmailToAllUser();
    }



    /**
     * 发送邮件给同步项目管理员
     * @param flag smtp是否需要认证
     * @param host 邮件服务器主机名
     * @param username 用户名
     * @param password 密码
     * @param title  标题
     * @param content  内容
     * @param cc  是否抄送
     * @param copyUser  抄送人
     * @param to  是否发送
     * @param receiveUser  接收人
     */
    public void sendEmailToSyncProjectManager(String flag,String host, String username, String password,
                                              String title,String content,
                                              Boolean cc,List<SysUser> copyUser,
                                              Boolean to,List<SysUser> receiveUser) throws GeneralSecurityException, MessagingException {

        SendEmailBaseConfig config = new SendEmailBaseConfig(flag,host,username,password,title,content,true,
                cc,copyUser,null,to,receiveUser,null,null,null);

        System.out.println("————————————开始发送邮件-同步项目名称————————————");
        config.sendEmailToSyncProjectManager(content);
        System.out.println("————————————发送邮件完毕-同步项目名称————————————");

    }
    /**
     * 发送邮件
     * @param sendEmailInfoDTOList 邮件组装信息
     * @param type 邮件类型
     */
    public void sendEmail(List<SendEmailInfoDTO> sendEmailInfoDTOList, String type) {
        if (CollUtil.isEmpty(sendEmailInfoDTOList)) {
            return;
        }
        log.info("-------------开始发送销售邮件-------------");
        List<SysUser> userList = sysUserMapper.selectList(Wrappers.lambdaQuery());
        QuoteEmailSetting quoteEmailSetting = quoteEmailSettingMapper
                .selectOne(Wrappers.<QuoteEmailSetting>lambdaQuery().eq(QuoteEmailSetting::getType, type));
        // 查询邮件记录，筛选条件为邮件类型为 "QUOTE"，发送次数在 0 到 100 之间
        List<SystemEmail> systemEmailList = systemEmailMapper.selectList(Wrappers.<SystemEmail>lambdaQuery()
                .eq(SystemEmail::getType, "QUOTE")
                .ge(SystemEmail::getSendNum, 0)
                .lt(SystemEmail::getSendNum, 90));
        // 随机数生成器
        Random random = new Random();

        // 遍历待发送邮件信息的列表
        sendEmailInfoDTOList.forEach(item -> {
            if (ObjectUtil.isNotEmpty(quoteEmailSetting)) {
                SystemEmail selectedEmail = null;
                boolean emailFound = false;
                // 尝试随机选择一个邮件对象，直到找到发送次数满足条件的邮件
                for (int attempts = 0; attempts < 10; attempts++) { // 最大尝试次数为10次
                    if (!systemEmailList.isEmpty()) {
                        // 随机选择一个邮件对象
                        selectedEmail = systemEmailList.get(random.nextInt(systemEmailList.size()));
                        // 检查发送次数是否在范围内
                        if (selectedEmail.getSendNum() >= 0 && selectedEmail.getSendNum() < 90) {
                            emailFound = true; // 找到符合条件的邮件
                            break; // 跳出循环
                        }
                    } else {
                        log.warn("没有可用的邮件对象，无法发送邮件");
                        break; // 如果列表为空，直接跳出循环
                    }
                }
                // 如果找到了符合条件的邮件对象
                if (emailFound) {
                    // 更新发送次数
                    selectedEmail.setSendNum(selectedEmail.getSendNum() + 1); // 发送次数加 1
                    // 直接更新 systemEmailList 中的值
                    int index = systemEmailList.indexOf(selectedEmail);
                    systemEmailList.set(index, selectedEmail); // 更新本地列表中的值
                    log.info("开始发送邮件,邮件类型:{},收件人:{}", type, JSON.toJSONString(item.getSysUser().getNickName()));
                    try {
                        // 添加默认抄送人
                        List<String> copyUserList = Arrays.asList(quoteEmailSetting.getCarbonCopy().split(","));
                        item.getCopyUser().addAll(userList.stream()
                                .filter(user -> copyUserList.contains(user.getUserId().toString()))
                                .filter(ObjectUtil::isNotNull)
                                .collect(Collectors.toList()));
                        // 发送邮件
                        MailUtil.sendEmailToAllUser("true", selectedEmail.getHost(),
                                selectedEmail.getUsername(), selectedEmail.getPassword(),
                                quoteEmailSetting.getEmailSubject(), quoteEmailSetting.getEmailTemplate(),
                                Boolean.TRUE, item.getCopyUser(),
                                Boolean.TRUE, Collections.singletonList(item.getSysUser()),
                                item.getEmailModelInfoConvertDTO(), item.getEmailSubjectInfoConvertDTO());
                        Thread.sleep(2000);
                        log.info("邮件发送成功,用户信息:{}", JSON.toJSONString(item.getSysUser()));
                    } catch (MessagingException | GeneralSecurityException | InterruptedException e) {
                        log.error("发送邮件失败,用户信息:{},异常信息:{}", JSON.toJSONString(item.getSysUser()), e.getMessage());
                    } finally {
                        // 更新发送次数到数据库
                        systemEmailMapper.updateById(selectedEmail);
                    }
                } else {
                    log.warn("今日邮件次数已达到最大值，无法发送邮件！");
                }
            }
        });
        log.info("-------------销售邮件发送完毕-------------");
    }

}
