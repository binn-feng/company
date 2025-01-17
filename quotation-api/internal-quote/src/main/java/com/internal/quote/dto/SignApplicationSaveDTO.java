package com.internal.quote.dto;

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

/**
 * 签约申请DTO
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "SignApplicationSaveDTO", description = "签约申请DTO")
public class SignApplicationSaveDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 商机ID */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesParentId;

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String contractType;


    /** 预计开始时间(暂时不用) */
    /*@ApiModelProperty(value = "预计开始时间(暂时不用)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计开始时间(暂时不用)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartTime;*/

    /** 预计结束时间(暂时不用) */
    /*@ApiModelProperty(value = "预计结束时间(暂时不用)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计结束时间(暂时不用)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndTime;*/

    /** 申请签约说明 */
    @ApiModelProperty(value = "申请签约说明")
    @Excel(name = "申请签约说明")
    private String signDesc;

    /**
     * 签约图片（Base64）
     */
    @ApiModelProperty(value = "签约图片（Base64）")
    @Excel(name = "签约图片（Base64）")
    private String signImg;


    /**
     * 北光报价总金额
     */
    @ApiModelProperty(value = "北光报价总金额")
    @Excel(name = "北光报价总金额")
    private BigDecimal northAmount;

    /**
     * 北光报价金额
     */
    @ApiModelProperty(value = "北光报价金额")
    @Excel(name = "北光报价金额")
    private BigDecimal northQuoteAmount;

    /**
     * 软件对外报价
     */
    @ApiModelProperty(value = "软件对外报价")
    @Excel(name = "软件对外报价")
    private BigDecimal softWareExtQuote;
    /**
     * 软件预计税后收入
     */
    @ApiModelProperty(value = "软件预计税后收入")
    @Excel(name = "软件预计税后收入")
    private BigDecimal softWareExtIncome;
    /**
     * 产品平台对外报价
     */
    @ApiModelProperty(value = "产品平台对外报价")
    @Excel(name = "产品平台对外报价")
    private BigDecimal prodExtQuote;
    /**
     * 产品平台预计税后收入
     */
    @ApiModelProperty(value = "产品平台预计税后收入")
    @Excel(name = "产品平台预计税后收入")
    private BigDecimal prodExtIncome;
    /**
     * 自研对外报价
     */
    @ApiModelProperty(value = "自研对外报价")
    @Excel(name = "自研对外报价")
    private BigDecimal selfExtQuote;
    /**
     * 自研预计税后收入
     */
    @ApiModelProperty(value = "自研预计税后收入")
    @Excel(name = "自研预计税后收入")
    private BigDecimal selfExtIncome;
    /**
     * 实施对外报价
     */
    @ApiModelProperty(value = "实施对外报价")
    @Excel(name = "实施对外报价")
    private BigDecimal impExtQuote;
    /**
     * 实施预计税后收入
     */
    @ApiModelProperty(value = "实施预计税后收入")
    @Excel(name = "实施预计税后收入")
    private BigDecimal impExtIncome;
    /**
     * 其他对外报价
     */
    @ApiModelProperty(value = "其他对外报价")
    @Excel(name = "其他对外报价")
    private BigDecimal otherExtQuote;
    /**
     * 其他预计税后收入
     */
    @ApiModelProperty(value = "其他预计税后收入")
    @Excel(name = "其他预计税后收入")
    private BigDecimal otherExtIncome;
    /**
     * 外采对外报价
     */
    @ApiModelProperty(value = "外采对外报价（北光报价金额）")
    @Excel(name = "外采对外报价")
    private BigDecimal extExtQuote;
    /**
     * 外采预计税后收入
     */
    @ApiModelProperty(value = "外采预计税后收入(税后总收入)")
    @Excel(name = "外采预计税后收入")
    private BigDecimal extExtIncome;

    /**
     * 预计税后总收入
     */
    @ApiModelProperty(value = "预计税后总收入")
    @Excel(name = "预计税后总收入")
    private BigDecimal totalExtIncome;

    /**
     * 人工成本
     */
    @ApiModelProperty(value = "人工成本")
    @Excel(name = "人工成本")
    private BigDecimal laborPrice;

    /**
     * 产品成本价
     */
    @ApiModelProperty(value = "产品成本价")
    @Excel(name = "产品成本价")
    private BigDecimal prodPrice;

    /**
     * 自研成本价
     */
    @ApiModelProperty(value = "自研成本价")
    @Excel(name = "自研成本价")
    private BigDecimal selfPrice;
    /**
     * 实施成本价
     */
    @ApiModelProperty(value = "实施成本价")
    @Excel(name = "实施成本价")
    private BigDecimal impPrice;
    /**
     * 其他成本价
     */
    @ApiModelProperty(value = "其他成本价")
    @Excel(name = "其他成本价")
    private BigDecimal otherPrice;
    /**
     * 硬件外采成本价
     */
    @ApiModelProperty(value = "硬件外采成本价（预计总成本）")
    @Excel(name = "硬件外采成本价")
    private BigDecimal extPrice;

    /**
     * 预计总成本
     */
    @ApiModelProperty(value = "预计总成本")
    @Excel(name = "预计总成本")
    private BigDecimal totalCost;
    /**
     * 预计利润
     */
    @ApiModelProperty(value = "预计利润")
    @Excel(name = "预计利润")
    private BigDecimal totalProfit;
    /**
     * 外采预计利润
     */
    @ApiModelProperty(value = "外采预计利润")
    @Excel(name = "外采预计利润")
    private BigDecimal extProfit;
    /**
     * 成本利润率
     */
    @ApiModelProperty(value = "成本利润率")
    @Excel(name = "成本利润率")
    private BigDecimal costProfitRate;
    /**
     * 外采利润率
     */
    @ApiModelProperty(value = "外采利润率")
    @Excel(name = "外采利润率")
    private BigDecimal extProfitRate;
    /**
     * 总成本利润率
     */
    @ApiModelProperty(value = "总成本利润率")
    @Excel(name = "总成本利润率")
    private BigDecimal totalCostProfitRate;

}
