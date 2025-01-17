package com.internal.quote.domain;

import java.math.BigDecimal;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 报价系统-商机外采硬件小记对象 quote_opportunities_external
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesExternal", description = "报价系统-商机外采硬件小记对象")
@TableName("quote_opportunities_external")
public class QuoteOpportunitiesExternal extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @TableField(value = "opportunities_id")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @TableField(value = "opportunities_detail_id")
    @Excel(name = "商机ID")
    private Long opportunitiesDetailId;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    @TableField(value = "serial_number")
    @Excel(name = "序号")
    private String serialNumber;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    @Excel(name = "名称")
    private String name;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @TableField(value = "unit")
    @Excel(name = "单位")
    private String unit;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @TableField(value = "unit_price")
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @TableField(value = "number")
    @Excel(name = "数量")
    private BigDecimal number;

    /**
     * 采购询价
     */
    @ApiModelProperty(value = "采购询价")
    @TableField(value = "purchase_inquiry")
    @Excel(name = "采购询价")
    private BigDecimal purchaseInquiry;

    /**
     * 发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]
     */
    @ApiModelProperty(value = "发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]")
    @TableField(value = "type")
    @Excel(name = "发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]")
    private String type;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    @TableField(value = "rate")
    @Excel(name = "税率")
    private BigDecimal rate;

    /**
     * 预估成本价
     */
    @ApiModelProperty(value = "预估成本价")
    @TableField(value = "estimated_cost")
    @Excel(name = "预估成本价")
    private BigDecimal estimatedCost;

    /**
     * 建议对外报价
     */
    @ApiModelProperty(value = "建议对外报价")
    @TableField(value = "external_quote")
    @Excel(name = "建议对外报价")
    private BigDecimal externalQuote;

    /**
     * 设置参数
     */
    @ApiModelProperty(value = "设置参数")
    @TableField(value = "setting_param")
    @Excel(name = "设置参数")
    private String settingParam;

    /**
     * 硬件id
     */
    @ApiModelProperty(value = "硬件id")
    @TableField(value = "hardware_id")
    @Excel(name = "硬件id")
    private Long hardwareId;

}