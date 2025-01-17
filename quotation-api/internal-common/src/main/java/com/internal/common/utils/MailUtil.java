package com.internal.common.utils;

import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.email.SendEmailBaseConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;


@Component
public class MailUtil {

    @Value("${system.send.email.nickname}")
    private String nickname;


    /**
     * @param flag                     smtp是否需要认证
     * @param host                     邮件服务器主机名
     * @param username                 用户名
     * @param password                 密码
     * @param title                    标题
     * @param content                  内容
     * @param cc                       是否抄送
     * @param copyUser                 抄送人
     * @param to                       是否发送
     * @param receiveUser              接收人
     * @param emailModelInfoConvertDTO 邮箱模版占位符转换信息
     * @param emSubjectInfoConvertDTO  邮箱主题占位符转换信息
     */
    public static void sendEmailToAllUser(String flag, String host,
                                          String username, String password,
                                          String title, String content,
                                          Boolean cc, List<SysUser> copyUser,
                                          Boolean to, List<SysUser> receiveUser,
                                          EmailModelInfoConvertDTO emailModelInfoConvertDTO,
                                          EmailSubjectInfoConvertDTO emSubjectInfoConvertDTO) throws MessagingException, GeneralSecurityException {

        SendEmailBaseConfig config = new SendEmailBaseConfig(flag, host, username, password, title, content, Boolean.TRUE,
                cc, copyUser, null, to, receiveUser, null, emailModelInfoConvertDTO, emSubjectInfoConvertDTO);
        config.sendEmailToAllUser();
    }


    /**
     * 发送邮件给同步项目管理员
     *
     * @param flag        smtp是否需要认证
     * @param host        邮件服务器主机名
     * @param username    用户名
     * @param password    密码
     * @param title       标题
     * @param content     内容
     * @param cc          是否抄送
     * @param copyUser    抄送人
     * @param to          是否发送
     * @param receiveUser 接收人
     */
    public static void sendEmailToSyncProjectManager(String flag, String host, String username, String password,
                                                     String title, String content,
                                                     Boolean cc, List<SysUser> copyUser,
                                                     Boolean to, List<SysUser> receiveUser) throws GeneralSecurityException, MessagingException {

        SendEmailBaseConfig config = new SendEmailBaseConfig(flag, host, username, password, title, content, true,
                cc, copyUser, null, to, receiveUser, null, null, null);

        System.out.println("————————————开始发送邮件-同步项目名称————————————");
        config.sendEmailToSyncProjectManager(content);
        System.out.println("————————————发送邮件完毕-同步项目名称————————————");

    }

}
