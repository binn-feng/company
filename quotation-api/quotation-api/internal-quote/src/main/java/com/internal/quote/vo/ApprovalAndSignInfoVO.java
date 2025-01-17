package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.internal.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 签约和审批页对象
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "ApprovalAndSignInfoVO", description = "签约和审批页对象")
public class ApprovalAndSignInfoVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
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
     * 北光报价总金额
     */
    @ApiModelProperty(value = "北光报价总金额")
    @Excel(name = "北光报价总金额")
    private BigDecimal northAmount = BigDecimal.ZERO;

    /**
     * 北光报价金额
     */
    @ApiModelProperty(value = "北光报价金额")
    @Excel(name = "北光报价金额")
    private BigDecimal northQuoteAmount = BigDecimal.ZERO;

    /**
     * 软件对外报价
     */
    @ApiModelProperty(value = "软件对外报价")
    @Excel(name = "软件对外报价")
    private BigDecimal softWareExtQuote = BigDecimal.ZERO;
    /**
     * 软件预计税后收入
     */
    @ApiModelProperty(value = "软件预计税后收入")
    @Excel(name = "软件预计税后收入")
    private BigDecimal softWareExtIncome = BigDecimal.ZERO;
    /**
     * 产品平台对外报价
     */
    @ApiModelProperty(value = "产品平台对外报价")
    @Excel(name = "产品平台对外报价")
    private BigDecimal prodExtQuote = BigDecimal.ZERO;
    /**
     * 产品平台预计税后收入
     */
    @ApiModelProperty(value = "产品平台预计税后收入")
    @Excel(name = "产品平台预计税后收入")
    private BigDecimal prodExtIncome = BigDecimal.ZERO;
    /**
     * 自研对外报价
     */
    @ApiModelProperty(value = "自研对外报价")
    @Excel(name = "自研对外报价")
    private BigDecimal selfExtQuote = BigDecimal.ZERO;
    /**
     * 自研预计税后收入
     */
    @ApiModelProperty(value = "自研预计税后收入")
    @Excel(name = "自研预计税后收入")
    private BigDecimal selfExtIncome = BigDecimal.ZERO;
    /**
     * 实施对外报价
     */
    @ApiModelProperty(value = "实施对外报价")
    @Excel(name = "实施对外报价")
    private BigDecimal impExtQuote = BigDecimal.ZERO;
    /**
     * 实施预计税后收入
     */
    @ApiModelProperty(value = "实施预计税后收入")
    @Excel(name = "实施预计税后收入")
    private BigDecimal impExtIncome = BigDecimal.ZERO;
    /**
     * 其他对外报价
     */
    @ApiModelProperty(value = "其他对外报价")
    @Excel(name = "其他对外报价")
    private BigDecimal otherExtQuote = BigDecimal.ZERO;
    /**
     * 其他预计税后收入
     */
    @ApiModelProperty(value = "其他预计税后收入")
    @Excel(name = "其他预计税后收入")
    private BigDecimal otherExtIncome = BigDecimal.ZERO;
    /**
     * 外采对外报价
     */
    @ApiModelProperty(value = "外采对外报价（北光报价金额）")
    @Excel(name = "外采对外报价")
    private BigDecimal extExtQuote = BigDecimal.ZERO;
    /**
     * 外采预计税后收入
     */
    @ApiModelProperty(value = "外采预计税后收入(税后总收入)")
    @Excel(name = "外采预计税后收入")
    private BigDecimal extExtIncome = BigDecimal.ZERO;

    /**
     * 预计税后总收入
     */
    @ApiModelProperty(value = "预计税后总收入")
    @Excel(name = "预计税后总收入")
    private BigDecimal totalExtIncome = BigDecimal.ZERO;

    /**
     * 人工成本
     */
    @ApiModelProperty(value = "人工成本")
    @Excel(name = "人工成本")
    private BigDecimal laborPrice = BigDecimal.ZERO;

    /**
     * 产品成本价
     */
    @ApiModelProperty(value = "产品成本价")
    @Excel(name = "产品成本价")
    private BigDecimal prodPrice = BigDecimal.ZERO;

    /**
     * 自研成本价
     */
    @ApiModelProperty(value = "自研成本价")
    @Excel(name = "自研成本价")
    private BigDecimal selfPrice = BigDecimal.ZERO;
    /**
     * 实施成本价
     */
    @ApiModelProperty(value = "实施成本价")
    @Excel(name = "实施成本价")
    private BigDecimal impPrice = BigDecimal.ZERO;
    /**
     * 其他成本价
     */
    @ApiModelProperty(value = "其他成本价")
    @Excel(name = "其他成本价")
    private BigDecimal otherPrice = BigDecimal.ZERO;
    /**
     * 硬件外采成本价
     */
    @ApiModelProperty(value = "硬件外采成本价（预计总成本）")
    @Excel(name = "硬件外采成本价")
    private BigDecimal extPrice = BigDecimal.ZERO;

    /**
     * 预计总成本
     */
    @ApiModelProperty(value = "预计总成本")
    @Excel(name = "预计总成本")
    private BigDecimal totalCost = BigDecimal.ZERO;
    /**
     * 预计利润
     */
    @ApiModelProperty(value = "预计利润")
    @Excel(name = "预计利润")
    private BigDecimal totalProfit = BigDecimal.ZERO;
    /**
     * 外采预计利润
     */
    @ApiModelProperty(value = "外采预计利润")
    @Excel(name = "外采预计利润")
    private BigDecimal extProfit = BigDecimal.ZERO;
    /**
     * 成本利润率
     */
    @ApiModelProperty(value = "成本利润率")
    @Excel(name = "成本利润率")
    private BigDecimal costProfitRate = BigDecimal.ZERO;
    /**
     * 外采利润率
     */
    @ApiModelProperty(value = "外采利润率")
    @Excel(name = "外采利润率")
    private BigDecimal extProfitRate = BigDecimal.ZERO;
    /**
     * 总成本利润率
     */
    @ApiModelProperty(value = "总成本利润率")
    @Excel(name = "总成本利润率")
    private BigDecimal totalCostProfitRate = BigDecimal.ZERO;


    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String signType;

    /** 试报价金额(无法报价) */
    @ApiModelProperty(value = "试报价金额(无法报价)")
    @Excel(name = "试报价金额(无法报价)")
    private BigDecimal unableQuoteAmount;

    /** 欣象试报价金额(无法报价) */
    @ApiModelProperty(value = "欣象试报价金额(无法报价)")
    @Excel(name = "欣象试报价金额(无法报价)")
    private BigDecimal xxUnableQuoteAmount;
    /** 报价说明(无法报价) */
    @ApiModelProperty(value = "报价说明(无法报价)")
    @Excel(name = "报价说明")
    private String valuationDesc;

    /**
     * 关单或跟进说明
     */
    @ApiModelProperty(value = "关单或跟进说明")
    @Excel(name = "关单或跟进说明")
    private String closeNote;
}
