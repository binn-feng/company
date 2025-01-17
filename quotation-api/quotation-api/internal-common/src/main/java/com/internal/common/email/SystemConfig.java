package com.internal.common.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zdliu
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    /**
     * 域名
     */
    private String domainName;
    /**
     * CRM项目同步接口
     */
    private String crmProjectSyncUrl;
    /**
     * 项目经理
     */
    private String syncProjectManagerEmail;
    /**
     * 项目经理姓名
     */
    private String syncProjectManagerName;
    /**
     * 发送邮件
     */
    private Send send;


    public static class Send {
        private Email email;

        // Getters and Setters

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public static class Email {
            private String nickname;

            // Getters and Setters

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }

}