package com.internal.quote.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 报价系统-商机报价信息对象 quote_opportunities
 *
 * @author internal
 * @date 2024-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunities", description = "报价系统-商机报价信息对象")
@TableName("quote_opportunities")
public class QuoteOpportunities extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 商机主表ID
     */
    @ApiModelProperty(value = "商机主表ID")
    @TableField(value = "opportunities_parent_id")
    @Excel(name = "商机主表ID")
    private Long opportunitiesParentId;
    /**
     * 对应CRM售前支持ID
     */
    @ApiModelProperty(value = "对应CRM售前支持ID")
    @TableField(value = "support_crm_id")
    @Excel(name = "对应CRM售前支持ID")
    private String supportCrmId;
    /**
     * 售前支持类型
     */
    @ApiModelProperty(value = "售前支持类型")
    @TableField(value = "support_type")
    @Excel(name = "售前支持类型")
    private String supportType;
    /**
     * 售前支持类型标识
     */
    @ApiModelProperty(value = "售前支持类型标识")
    @TableField(value = "support_identification")
    @Excel(name = "售前支持类型标识")
    private String supportIdentification;
    /**
     * 售前支持人员ID
     */
    @ApiModelProperty(value = "售前支持人员ID")
    @TableField(value = "support_person")
    @Excel(name = "售前支持人员ID")
    private String supportPerson;
    /**
     * 售前支持人员姓名
     */
    @ApiModelProperty(value = "售前支持人员姓名")
    @TableField(value = "support_name")
    @Excel(name = "售前支持人员姓名")
    private String supportName;
    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @TableField(value = "type")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    @TableField(value = "appendix")
    @Excel(name = "附件")
    private String appendix;
    /**
     * 报价说明[无法报价类型时使用]
     */
    @ApiModelProperty(value = "报价说明[无法报价类型时使用]")
    @TableField(value = "quote_desc")
    @Excel(name = "报价说明[无法报价类型时使用]")
    private String quoteDesc;

    /**
     * 丢单说明
     */
    @ApiModelProperty(value = "丢单说明")
    @TableField(value = "reason_desc")
    @Excel(name = "丢单说明")
    private String reasonDesc;

    /**
     * 丢单时间
     */
    @ApiModelProperty(value = "丢单时间")
    @TableField(value = "lose_time")
    @Excel(name = "丢单时间")
    private Date loseTime;


    /**
     * 销售试报价说明
     */
    @ApiModelProperty(value = "销售试报价说明")
    @TableField(value = "trial_quote_desc")
    @Excel(name = "销售试报价说明")
    private String trialQuoteDesc;

    /**
     * 报价金额[粗略详细为总金额、无法报价为销售试报价金额]
     */
    @ApiModelProperty(value = "报价金额[粗略详细为总金额、无法报价为销售试报价金额]")
    @TableField(value = "amount")
    @Excel(name = "报价金额[粗略详细为总金额、无法报价为销售试报价金额]")
    private BigDecimal amount;


    /**
     * 商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）
     */
    @ApiModelProperty(value = "商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    @TableField(value = "status")
    @Excel(name = "商机状态", readConverterExp = "商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    private String status;

    /**
     * 附件信息
     */
    @ApiModelProperty(value = "附件信息")
    @TableField(exist = false)
    private List<QuoteOpportunitiesFile> quoteOpportunitiesFileList;

    /**
     * 是否存在暂存信息
     */
    @ApiModelProperty(value = "是否存在暂存信息")
    @TableField(exist = false)
    private Boolean isExistDraft;

    /**
     * 当前商机生效版本
     */
    @ApiModelProperty(value = "当前商机生效版本")
    @TableField(exist = false)
    private String currentVersion;

    /**
     * 销售审核时间
     */
    @ApiModelProperty(value = "销售审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "sale_audit_time")
    private Date saleAuditTime;

    /**
     * 签约审核时间
     */
    @ApiModelProperty(value = "签约审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "sign_audit_time")
    private Date signAuditTime;

    /**
     * 销售审核记录
     */
    @ApiModelProperty(value = "销售审核记录")
    @TableField(value = "sale_audit_log")
    private String saleAuditLog;

    /**
     * 签约审核记录
     */
    @ApiModelProperty(value = "签约审核记录")
    @TableField(value = "sign_audit_log")
    private String signAuditLog;

    /**
     * 报价内容
     */
    @ApiModelProperty(value = "报价内容")
    @Excel(name = "报价内容")
    @TableField(exist = false)
    private String quoteContent;

    /**
     * 流程类别[1:正常,2:需更新成本报价]
     */
    @ApiModelProperty(value = "流程类别[1:正常流程,2:需更新成本报价]")
    @Excel(name = "流程类别[1:正常流程,2:需更新成本报价]")
    @TableField(value = "process_category")
    private String processCategory;

    /**
     * 报价方式[0:默认,1:自己报价,2:其他人报价]
     */
    @ApiModelProperty(value = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    @Excel(name = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    @TableField(value = "quote_method")
    private String quoteMethod;

    /**
     * 报价人列表
     */
    @ApiModelProperty(value = "报价人列表")
    @Excel(name = "报价人列表")
    @TableField(value = "quoters")
    private String quoters;

    /**
     * 不报价人列表
     */
    @ApiModelProperty(value = "不报价人列表")
    @Excel(name = "不报价人列表")
    @TableField(value = "non_quoters")
    private String nonQuoters;

    /**
     * crm更新时间(判断一致性)
     */
    @TableField(value = "crm_update_time", fill = FieldFill.INSERT_UPDATE)
    private Date crmUpdateTime;

    /**
     * 废弃原因
     */
    @ApiModelProperty(value = "废弃原因")
    @Excel(name = "废弃原因")
    @TableField(value = "abandon_desc")
    private String abandonDesc;

    /**
     * 丢单类型
     */
    @ApiModelProperty(value = "丢单类型")
    @TableField(value = "lose_type")
    @Excel(name = "丢单类型")
    private String loseType;

    /**
     * 丢单审核时间
     */
    @ApiModelProperty(value = "丢单审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "lose_audit_time")
    private Date loseAuditTime;

    /**
     * 丢单审核记录
     */
    @ApiModelProperty(value = "丢单审核记录")
    @TableField(value = "lose_audit_log")
    private String loseAuditLog;

    /**
     * 丢单审批人
     */
    @ApiModelProperty(value = "丢单审批人")
    @TableField(value = "lose_audit_person")
    private Long loseAuditPerson;

    /**
     * 销售审批人
     */
    @ApiModelProperty(value = "销售审批人")
    @TableField(value = "sale_audit_person")
    private Long saleAuditPerson;

    /**
     * 签约审批人
     */
    @ApiModelProperty(value = "签约审批人")
    @TableField(value = "sign_audit_person")
    private Long signAuditPerson;


}