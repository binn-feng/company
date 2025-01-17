package com.internal.quote.domain;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.config.ListTypeHandler;
import com.internal.quote.dto.EmailUserInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 报价系统-工作流邮箱设置对象 quote_email_setting
 *
 * @author internal
 * @date 2024-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteEmailSetting", description = "报价系统-工作流邮箱设置对象")
@TableName("quote_email_setting")
public class QuoteEmailSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型[ 需成本报价: NEED_COST_QUOTATION,
     * 完成成本报价:COMPLETE_COST_QUOTATION,
     * 报价审批: QUOTATION_APPROVAL,
     * 报价审批结果通知: QUOTATION_APPROVAL_RESULT_NOTIFICATION,
     * 签约申请: SIGNING_APPLICATION,
     * 签约审批结果通知: SIGNING_APPROVAL_RESULT_NOTIFICATION,
     * 丢单通知: LOSS_NOTIFICATION
     * ]
     */
    @ApiModelProperty(value = "类型[ 需成本报价: NEED_COST_QUOTATION, 完成成本报价:COMPLETE_COST_QUOTATION, 报价审批: QUOTATION_APPROVAL, 报价审批结果通知: QUOTATION_APPROVAL_RESULT_NOTIFICATION, 签约申请: SIGNING_APPLICATION, 签约审批结果通知: SIGNING_APPROVAL_RESULT_NOTIFICATION, : LOSS_NOTIFICATION ]")
    @TableField(value = "type")
    @Excel(name = "类型[ 需成本报价: NEED_COST_QUOTATION, 完成成本报价:COMPLETE_COST_QUOTATION, 报价审批: QUOTATION_APPROVAL, 报价审批结果通知: QUOTATION_APPROVAL_RESULT_NOTIFICATION, 签约申请: SIGNING_APPLICATION, 签约审批结果通知: SIGNING_APPROVAL_RESULT_NOTIFICATION, : LOSS_NOTIFICATION ]")
    private String type;

    /**
     * 发送人
     */
    @ApiModelProperty(value = "发送人")
    @TableField(value = "sender")
    @Excel(name = "发送人")
    private String sender;


    /**
     * 抄送人
     */
    @ApiModelProperty(value = "抄送人")
    @TableField(value = "carbon_copy")
    @Excel(name = "抄送人")
    private String carbonCopy;

    /**
     * 发送人信息
     */
    @ApiModelProperty(value = "发送人信息")
    @TableField(value = "sender_info",typeHandler = ListTypeHandler.class)
    @Excel(name = "发送人信息")
    private List<EmailUserInfoDTO> senderInfo;


    /**
     * 抄送人信息
     */
    @ApiModelProperty(value = "抄送人信息")
    @TableField(value = "carbon_copy_info", typeHandler = ListTypeHandler.class)
    @Excel(name = "抄送人信息")
    private List<EmailUserInfoDTO> carbonCopyInfo;


    /**
     * 邮件主题
     */
    @ApiModelProperty(value = "邮件主题")
    @TableField(value = "email_subject")
    @Excel(name = "邮件主题")
    private String emailSubject;

    /**
     * 邮件模版
     */
    @ApiModelProperty(value = "邮件模版")
    @TableField(value = "email_template")
    @Excel(name = "邮件模版")
    private String emailTemplate;

    public QuoteEmailSetting(Long id, String type, String sender, String carbonCopy, List<EmailUserInfoDTO> senderInfo,
                             List<EmailUserInfoDTO> carbonCopyInfo, String emailSubject, String emailTemplate) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.carbonCopy = carbonCopy;
        this.senderInfo = senderInfo;
        this.carbonCopyInfo = carbonCopyInfo;
        this.emailSubject = emailSubject;
        this.emailTemplate = emailTemplate;
    }
}