package com.internal.quote.dto;

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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 报价系统-商机外采硬件小记对象 quote_opportunities_external
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesExternalSaveDTO", description = "报价系统-商机外采硬件小记对象")
public class QuoteOpportunitiesExternalSaveDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesDetailId;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    @Excel(name = "序号")
    @NotEmpty(message = "序号不能为空")
    private String serialNumber;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Excel(name = "名称")
    private String name;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @NotEmpty(message = "单位不能为空")
    private String unit;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private BigDecimal number = BigDecimal.ZERO;

    /**
     * 采购询价
     */
    @ApiModelProperty(value = "采购询价")
    @NotNull(message = "采购询价不能为空")
    private BigDecimal purchaseInquiry = BigDecimal.ZERO;

    /**
     * 发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]
     */
    @ApiModelProperty(value = "发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]")
    @NotEmpty(message = "发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]不能为空")
    @Excel(name = "发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]")
    private String type;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    @NotNull(message = "税率不能为空")
    @Excel(name = "税率")
    private BigDecimal rate;

    /**
     * 预估成本价
     */
    @ApiModelProperty(value = "预估成本价")
    @Excel(name = "预估成本价")
    @NotNull(message = "预估成本价不能为空")
    private BigDecimal estimatedCost;

    /**
     * 建议对外报价
     */
    @ApiModelProperty(value = "建议对外报价")
    @Excel(name = "建议对外报价")
    @NotNull(message = "建议对外报价不能为空")
    private BigDecimal externalQuote;

    /**
     * 设置参数
     */
    @ApiModelProperty(value = "设置参数")
    @Excel(name = "设置参数")
    @NotEmpty(message = "设置参数不能为空")
    private String settingParam;

    /**
     * 硬件id
     */
    @ApiModelProperty(value = "硬件id")
    @Excel(name = "硬件id")
    private Long hardwareId;
}