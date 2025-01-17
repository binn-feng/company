package com.internal.common.email;

import com.internal.common.core.domain.dto.EmailModelInfoConvertDTO;
import com.internal.common.core.domain.dto.EmailSubjectInfoConvertDTO;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.utils.TemplateUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

@Slf4j
public class SendEmailBaseConfig {

    private String flag;    //smtp是否需要认证
    private String host;    //邮件服务器主机名
    private String username;    //用户名
    private String password;    //密码
    private String title;   //标题
    private String content; //发送的内容
    private Boolean listFlag;   //是否发送复杂内容
    private Boolean cc; //是否抄送
    private List<SysUser> copyUser;  //抄送人
    private Address[] copyAddress;  //报错后 抄送人
    private Boolean to; //是否发送
    private List<SysUser> receiveUser;   //发送人
    private Address[] receiveAddress;   //报错后 发送人
    private EmailModelInfoConvertDTO emailModelInfoConvertDTO; // 邮箱模版占位符转换信息
    private EmailSubjectInfoConvertDTO emSubjectInfoConvertDTO; // 邮箱主题占位符转换信息


    //如果其他邮箱验证 在sendEmailToAllUser()，添加if判断


    public SendEmailBaseConfig(String flag, String host, String username, String password, String title, String content, Boolean listFlag,
                               Boolean cc, List<SysUser> copyUser, Address[] copyAddress, Boolean to, List<SysUser> receiveUser, Address[] receiveAddress,
                               EmailModelInfoConvertDTO emailModelInfoConvertDTO, EmailSubjectInfoConvertDTO emSubjectInfoConvertDTO) {
        this.flag = flag;
        this.host = host;
        this.username = username;
        this.password = password;
        this.title = title;
        this.content = content;
        this.listFlag = listFlag;
        this.cc = cc;
        this.copyUser = copyUser;
        this.copyAddress = copyAddress;
        this.to = to;
        this.receiveUser = receiveUser;
        this.receiveAddress = receiveAddress;
        this.emailModelInfoConvertDTO = emailModelInfoConvertDTO;
        this.emSubjectInfoConvertDTO = emSubjectInfoConvertDTO;
    }

    //每天定时和crm同步项目名称任务提醒邮件
    public void sendEmailToSyncProjectManager(String contentStr) throws MessagingException, GeneralSecurityException {
        sendEmailNTES(contentStr);
    }


    //每天向所有用户发送邮件
    public void sendEmailToAllUser() {
        try {
            String[] split = username.split("@");
            if ("qq.com".equals(split[1])) {
                sendEmailQQ();
                return;
            }

            if ("163.com".equals(split[1])) {
                sendEmailNTES(sendEmailContentStr());
                return;
            }
            sendEmailNTES(sendEmailContentStr());
        } catch (Exception e) {
            log.error("发送邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //初始化配置数据
    private Properties initProperties() {
        return new Properties();
    }


    //通过QQ邮箱发送
    private void sendEmailQQ() {
        try {
            Properties properties = initProperties();
            properties.setProperty("mail.host", host);
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", flag);

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            session.setDebug(true);
            Transport transport = session.getTransport();
            transport.connect(host, username, password);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            try {
                message.setSubject(new String(title.getBytes(StandardCharsets.UTF_8), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("邮件主题编码转换异常:", e);
            }

            if (listFlag) {
                content = sendEmailContentStr();
            }
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());

            if (cc) {
                if (copyUser.size() > 0) {
                    message.setRecipients(Message.RecipientType.CC, ccAddresses(copyUser));
                }
            }
            if (to) {
                message.setRecipient(Message.RecipientType.CC, new InternetAddress(username));
                if (receiveUser.size() > 0) {
                    message.setRecipients(Message.RecipientType.TO, toAddresses(receiveUser));
                }
            }

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("通过QQ邮箱发送邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //通过网易邮箱发送
    private void sendEmailNTES(String contentStr) {
        try {
            Properties properties = initProperties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", 25);
            properties.put("mail.user", username);
            properties.put("mail.password", password);

            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            Session session = Session.getInstance(properties, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            if (cc) {
                if (copyUser.size() > 0) {
                    message.setRecipients(Message.RecipientType.CC, ccAddresses(copyUser));
                }
            }
            if (to) {
                if (receiveUser.size() > 0) {
                    message.setRecipients(Message.RecipientType.TO, toAddresses(receiveUser));
                }
            }

            try {
                String encodedSubject = MimeUtility.encodeText(title, "UTF-8", null);
                message.setSubject(encodedSubject);
            } catch (UnsupportedEncodingException e) {
                log.error("邮件主题编码转换异常:", e);
            }

            if (listFlag) {
                content = contentStr;
            }
            message.setContent(content, "text/html;charset=utf-8");

            Transport transport = session.getTransport();
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("通过网易邮箱发送邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //设置邮件内容，模版转换
    private String sendEmailContentStr() {
        title = TemplateUtil.replaceTemplate(title, emSubjectInfoConvertDTO);
        String replaceTemplate = TemplateUtil.replaceTemplate(content, emailModelInfoConvertDTO);
        // 替换所有换行符为 <br/> TODO: 没起作用
        replaceTemplate = replaceTemplate.replaceAll("\r?\n", "<br/>");
        return replaceTemplate;
    }

    //设置抄送人地址
    private Address[] ccAddresses(List<SysUser> copyUser) throws AddressException {
        // 使用 Set 来跟踪唯一的电子邮件地址
        Set<String> uniqueEmails = new HashSet<>();
        // 使用 List 来存储有效的 InternetAddress
        List<Address> addressesList = new ArrayList<>();

        for (SysUser user : copyUser) {
            String email = user.getEmail();
            // 避免空值和重复
            if (email != null && uniqueEmails.add(email)) {
                addressesList.add(new InternetAddress(email));
            }
        }
        // 将 List 转换为数组并返回
        return addressesList.toArray(new Address[0]);
    }

    //设置发送人地址
    private Address[] toAddresses(List<SysUser> receiveUser) throws AddressException {
        // 使用 Set 来跟踪唯一的电子邮件地址
        Set<String> uniqueEmails = new HashSet<>();
        // 使用 List 来临时存储有效的 InternetAddress
        List<Address> addressesList = new ArrayList<>();
        for (SysUser user : receiveUser) {
            String email = user.getEmail();
            // 避免空值和重复
            if (email != null && uniqueEmails.add(email)) {
                addressesList.add(new InternetAddress(email));
            }
        }
        // 将 List 转换为数组并返回
        return addressesList.toArray(new Address[0]);
    }

    //接受者邮箱不正确，过滤后得到正确的邮箱
    private void exceptionAddress(Address[] addresses) {
        try {
            if (addresses.length > 0) {
                Address[] address = new InternetAddress[addresses.length];
                for (int i = 0; i < addresses.length; i++) {
                    address[i] = new InternetAddress(addresses[i].toString());
                }
                receiveAddress = address;
                sendMailWhenException();
            }
        } catch (Exception e) {
            log.error("处理异常地址时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //给正确的邮箱发送邮件
    private void sendMailWhenException() {
        try {
            String[] split = username.split("@");
            if ("qq.com".equals(split[1])) {
                sendMailQQWhenException();
                return;
            }

            if ("163.com".equals(split[1])) {
                sendMailNTESWhenException();
            }
        } catch (Exception e) {
            log.error("发送异常邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //正确的QQ邮箱发邮件
    private void sendMailQQWhenException() {
        try {
            Properties properties = initProperties();
            properties.setProperty("mail.host", host);
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", flag);

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            session.setDebug(true);
            Transport transport = session.getTransport();
            transport.connect(host, username, password);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            try {
                message.setSubject(new String(title.getBytes(StandardCharsets.UTF_8), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("邮件主题编码错误", e);
            }

            if (listFlag) {
                content = sendEmailContentStr();
            }
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());

            if (cc) {
                if (copyAddress.length > 0) {
                    message.setRecipients(Message.RecipientType.CC, copyAddress);
                }
            }
            if (to) {
                if (receiveAddress.length > 0) {
                    message.setRecipients(Message.RecipientType.TO, receiveAddress);
                }
            }

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("通过QQ邮箱发送异常邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

    //正确的网易邮箱发数据
    private void sendMailNTESWhenException() {
        try {
            Properties properties = initProperties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", 25);
            properties.put("mail.user", username);
            properties.put("mail.password", password);

            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            Session session = Session.getInstance(properties, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            if (cc) {
                if (copyAddress.length > 0) {
                    message.setRecipients(Message.RecipientType.CC, copyAddress);
                }
            }
            if (to) {
                if (receiveAddress.length > 0) {
                    message.setRecipients(Message.RecipientType.TO, receiveAddress);
                }
            }

            try {
                message.setSubject(new String(title.getBytes(StandardCharsets.UTF_8), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("邮件主题编码错误", e);
            }

            if (listFlag) {
                content = sendEmailContentStr();
            }
            message.setContent(content, "text/html;charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            log.error("通过网易邮箱发送异常邮件时发生错误", e);
            throw new RuntimeException("发送邮件失败，请稍后重试。");
        }
    }

}
