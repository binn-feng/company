package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 报价系统-主表resultVO
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesVO", description = "报价系统-主表resultVO")
public class QuoteOpportunitiesVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesParentId;

    /**
     * 商机名称
     */
    @ApiModelProperty(value = "商机名称")
    @Excel(name = "商机名称")
    private String name;

    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;

    /**
     * 类别[1:正常流程,2:需更新成本报价]
     */
    @ApiModelProperty(value = "类别[1:正常流程,2:需更新成本报价]")
    @Excel(name = "类别[1:正常流程,2:需更新成本报价]")
    private String category;

    /**
     * 所属销售ID
     */
    @ApiModelProperty(value = "所属销售ID")
    @Excel(name = "所属销售ID")
    private Long saleId;

    /**
     * 销售名称
     */
    @ApiModelProperty(value = "销售名称")
    @Excel(name = "销售名称")
    private String saleName;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String customersName;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    @Excel(name = "附件")
    private String appendix;

    /**
     * 商机介绍
     */
    @ApiModelProperty(value = "商机介绍")
    @Excel(name = "商机介绍")
    private String introduce;

    /**
     * 报价说明[无法报价类型时使用]
     */
    @ApiModelProperty(value = "报价说明[无法报价类型时使用]")
    @Excel(name = "报价说明[无法报价类型时使用]")
    private String quoteDesc;
    /**
     * 原因说明
     */
    @ApiModelProperty(value = "丢单说明")
    @Excel(name = "丢单说明")
    private String reasonDesc;

    /**
     * 丢单时间
     */
    @ApiModelProperty(value = "丢单时间")
    @Excel(name = "丢单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loseTime;

    /**
     * 销售试报价说明
     */
    @ApiModelProperty(value = "销售试报价说明")
    @Excel(name = "销售试报价说明")
    private String trialQuoteDesc;

    /**
     * 成本报价金额
     */
    @ApiModelProperty(value = "成本报价金额")
    @Excel(name = "成本报价金额")
    private BigDecimal amount;
    /**
     * 销售报价金额
     */
    @ApiModelProperty(value = "销售报价金额")
    @Excel(name = "销售报价金额")
    private BigDecimal projExtQuoteTotal;
    /**
     * 签约金额
     */
    @ApiModelProperty(value = "签约金额")
    @Excel(name = "签约金额")
    private BigDecimal signQuoteTotal;

    /**
     * 售前ID(不用)
     */
    /*@ApiModelProperty(value = "售前ID(不用)")
    @Excel(name = "售前ID(不用)")
    private Long preSaleId;*/

    /**
     * 售前名称(显示用)
     */
    @ApiModelProperty(value = "售前名称(显示用)")
    @Excel(name = "售前名称(显示用)")
    private String preSaleName;

    /**
     * 商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）
     */
    @ApiModelProperty(value = "商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    @Excel(name = "商机状态", readConverterExp = "商机状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    private String status;

    /**
     * 是否存在暂存信息
     */
    @ApiModelProperty(value = "是否存在暂存信息")
    private Boolean isExistDraft;

    /**
     * 成本报价版本(报价类型+报价版本号)
     */
    @ApiModelProperty(value = "成本报价版本(报价类型+报价版本号)")
    private String currentVersion;
    /**
     * 成本报价版本
     */
    @ApiModelProperty(value = "成本报价版本")
    private String costQuoteVersion;

    /**
     * 审核理由(销售报价)
     */
    @ApiModelProperty(value = "审核理由(销售报价)")
    private String saleAuditDesc;
    /**
     * 审核理由(签约)
     */
    @ApiModelProperty(value = "审核理由(签约)")
    private String signAuditDesc;

    /** 销售审核时间 */
    @ApiModelProperty(value = "销售审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleAuditTime;


    /** 签约审核时间 */
    @ApiModelProperty(value = "签约审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 销售报价利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "销售报价利润率(不包含外采硬件)")
    @Excel(name = "销售报价利润率(不包含外采硬件)")
    private BigDecimal projProfitRateExcl;
    /**
     * 签约利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "签约利润率(不包含外采硬件)")
    @Excel(name = "签约利润率(不包含外采硬件)")
    private BigDecimal signProjProfitRateExcl;

    /**
     * 关单或跟进说明
     */
    @ApiModelProperty(value = "关单或跟进说明")
    @Excel(name = "关单或跟进说明")
    private String closeNote;
    /**
     * 情况说明(无法报价)
     */
    @ApiModelProperty(value = "情况说明(无法报价)")
    @Excel(name = "情况说明(无法报价)")
    private String description;

    /**
     * 报价内容
     */
    @ApiModelProperty(value = "报价内容")
    @Excel(name = "报价内容")
    private String quoteContent;

    /**
     * 对应CRM售前支持ID
     */
    @ApiModelProperty(value = "对应CRM售前支持ID")
    @Excel(name = "对应CRM售前支持ID")
    private String supportCrmId;
    /**
     * 售前支持类型
     */
    @ApiModelProperty(value = "售前支持类型")
    @Excel(name = "售前支持类型")
    private String supportType;

    /**
     * 售前支持人员ID
     */
    @ApiModelProperty(value = "售前支持人员ID")
    @Excel(name = "售前支持人员ID")
    private String supportPerson;


    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String contractType;

    /**
     * 流程类别[1:正常流程,2:需更新成本报价]
     */
    @ApiModelProperty(value = "流程类别[1:正常流程,2:需更新成本报价]")
    @Excel(name = "流程类别[1:正常流程,2:需更新成本报价]")
    private String processCategory;

    /**
     * 报价方式[0:默认,1:自己报价,2:其他人报价]
     */
    @ApiModelProperty(value = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    @Excel(name = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    private String quoteMethod;

    /**
     * 报价人列表
     */
    @ApiModelProperty(value = "报价人列表")
    @Excel(name = "报价人列表")
    private String quoters;

    /**
     * 不报价人列表
     */
    @ApiModelProperty(value = "不报价人列表")
    @Excel(name = "不报价人列表")
    private String nonQuoters;

    /**
     * 商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]
     */
    @ApiModelProperty(value = "商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]")
    @TableField(value = "status")
    @Excel(name = "商机状态", readConverterExp = "商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]")
    private String parentStatus;

    /**
     * 由其他人报价
     */
    @ApiModelProperty(value = "由其他人报价")
    @Excel(name = "由其他人报价")
    private Boolean quoteByOthers = false;

    /**
     * 已选择报价方式
     */
    @ApiModelProperty(value = "已选择报价方式")
    @Excel(name = "已选择报价方式")
    private Boolean methodSelected = false;

    /** 签约申请时间 */
    @ApiModelProperty(value = "签约申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 是多部门
     */
    @ApiModelProperty(value = "是多部门")
    @Excel(name = "是多部门")
    private Boolean isMultiDept;

    /**
     * 废弃原因
     */
    @ApiModelProperty(value = "废弃原因")
    @Excel(name = "废弃原因")
    private String abandonDesc;

    /**
     * 丢单类型
     */
    @ApiModelProperty(value = "丢单类型")
    @Excel(name = "丢单类型")
    private String loseType;

    /**
     * 丢单审核时间
     */
    @ApiModelProperty(value = "丢单审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loseAuditTime;

    /**
     * 丢单审核记录
     */
    @ApiModelProperty(value = "丢单审核记录")
    private String loseAuditLog;

    /**
     * 丢单审批人
     */
    @ApiModelProperty(value = "丢单审批人")
    private Long loseAuditPerson;

    /**
     * 销售审批人
     */
    @ApiModelProperty(value = "销售审批人")
    private Long saleAuditPerson;

    /**
     * 签约审批人
     */
    @ApiModelProperty(value = "签约审批人")
    private Long signAuditPerson;
}