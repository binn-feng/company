package com.internal.quote.dto;

import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价系统-商机售前报价信息对象 quote_presale_info
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuotePresaleInfoDTO", description = "报价系统-商机售前报价信息对象")
public class QuotePresaleInfoDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 售前报价版本
     */
    @ApiModelProperty(value = "售前报价版本")
    @Excel(name = "售前报价版本")
    private String preSaleVersion;

    /**
     * 售前成本价
     */
    @ApiModelProperty(value = "售前成本价")
    @Excel(name = "售前成本价")
    private BigDecimal prePrice;

    /**
     * 售前支持税率
     */
    @ApiModelProperty(value = "售前支持税率")
    @Excel(name = "售前支持税率")
    private BigDecimal preTaxRate;

    /**
     * 售前支持利润率
     */
    @ApiModelProperty(value = "售前支持利润率")
    @Excel(name = "售前支持利润率")
    private BigDecimal preProfitRate;

    /**
     * 售前支持建议报价
     */
    @ApiModelProperty(value = "售前支持建议报价")
    @Excel(name = "售前支持建议报价")
    private BigDecimal preSugQuote;

    /**
     * 售前支持对外报价
     */
    @ApiModelProperty(value = "售前支持对外报价")
    @Excel(name = "售前支持对外报价")
    private BigDecimal preExtQuote;

    /**
     * 定制开发成本价
     */
    @ApiModelProperty(value = "定制开发成本价")
    @Excel(name = "定制开发成本价")
    private BigDecimal devPrice;

    /**
     * 定制开发税率
     */
    @ApiModelProperty(value = "定制开发税率")
    @Excel(name = "定制开发税率")
    private BigDecimal devTaxRate;

    /**
     * 定制开发利润率
     */
    @ApiModelProperty(value = "定制开发利润率")
    @Excel(name = "定制开发利润率")
    private BigDecimal devProfitRate;

    /**
     * 定制开发建议报价
     */
    @ApiModelProperty(value = "定制开发建议报价")
    @Excel(name = "定制开发建议报价")
    private BigDecimal devSugQuote;

    /**
     * 定制开发对外报价
     */
    @ApiModelProperty(value = "定制开发对外报价")
    @Excel(name = "定制开发对外报价")
    private BigDecimal devExtQuote;

    /**
     * 产品平台成本价
     */
    @ApiModelProperty(value = "产品平台成本价")
    @Excel(name = "产品平台成本价")
    private BigDecimal prodPrice;

    /**
     * 产品平台税率
     */
    @ApiModelProperty(value = "产品平台税率")
    @Excel(name = "产品平台税率")
    private BigDecimal prodTaxRate;

    /**
     * 产品平台利润率
     */
    @ApiModelProperty(value = "产品平台利润率")
    @Excel(name = "产品平台利润率")
    private BigDecimal prodProfitRate;

    /**
     * 产品平台建议报价
     */
    @ApiModelProperty(value = "产品平台建议报价")
    @Excel(name = "产品平台建议报价")
    private BigDecimal prodSugQuote;

    /**
     * 产品平台对外报价
     */
    @ApiModelProperty(value = "产品平台对外报价")
    @Excel(name = "产品平台对外报价")
    private BigDecimal prodExtQuote;

    /**
     * 其他成本价
     */
    @ApiModelProperty(value = "其他成本价")
    @Excel(name = "其他成本价")
    private BigDecimal otherPrice;

    /**
     * 其他税率
     */
    @ApiModelProperty(value = "其他税率")
    @Excel(name = "其他税率")
    private BigDecimal otherTaxRate;

    /**
     * 其他利润率
     */
    @ApiModelProperty(value = "其他利润率")
    @Excel(name = "其他利润率")
    private BigDecimal otherProfitRate;

    /**
     * 其他建议报价
     */
    @ApiModelProperty(value = "其他建议报价")
    @Excel(name = "其他建议报价")
    private BigDecimal otherSugQuote;

    /**
     * 其他对外报价
     */
    @ApiModelProperty(value = "其他对外报价")
    @Excel(name = "其他对外报价")
    private BigDecimal otherExtQuote;

    /**
     * 自研成本价
     */
    @ApiModelProperty(value = "自研成本价")
    @Excel(name = "自研成本价")
    private BigDecimal selfPrice;

    /**
     * 自研税率
     */
    @ApiModelProperty(value = "自研税率")
    @Excel(name = "自研税率")
    private BigDecimal selfTaxRate;

    /**
     * 自研利润率
     */
    @ApiModelProperty(value = "自研利润率")
    @Excel(name = "自研利润率")
    private BigDecimal selfProfitRate;

    /**
     * 自研建议报价
     */
    @ApiModelProperty(value = "自研建议报价")
    @Excel(name = "自研建议报价")
    private BigDecimal selfSugQuote;

    /**
     * 自研对外报价
     */
    @ApiModelProperty(value = "自研对外报价")
    @Excel(name = "自研对外报价")
    private BigDecimal selfExtQuote;

    /**
     * 外采成本价
     */
    @ApiModelProperty(value = "外采成本价")
    @Excel(name = "外采成本价")
    private BigDecimal extPrice;

    /**
     * 外采税率
     */
    @ApiModelProperty(value = "外采税率")
    @Excel(name = "外采税率")
    private BigDecimal extTaxRate;

    /**
     * 外采利润率
     */
    @ApiModelProperty(value = "外采利润率")
    @Excel(name = "外采利润率")
    private BigDecimal extProfitRate;

    /**
     * 外采建议报价
     */
    @ApiModelProperty(value = "外采建议报价")
    @Excel(name = "外采建议报价")
    private BigDecimal extSugQuote;

    /**
     * 外采对外报价
     */
    @ApiModelProperty(value = "外采对外报价")
    @Excel(name = "外采对外报价")
    private BigDecimal extExtQuote;

    /**
     * 实施成本价
     */
    @ApiModelProperty(value = "实施成本价")
    @Excel(name = "实施成本价")
    private BigDecimal impPrice;

    /**
     * 实施税率
     */
    @ApiModelProperty(value = "实施税率")
    @Excel(name = "实施税率")
    private BigDecimal impTaxRate;

    /**
     * 实施利润率
     */
    @ApiModelProperty(value = "实施利润率")
    @Excel(name = "实施利润率")
    private BigDecimal impProfitRate;

    /**
     * 实施建议报价
     */
    @ApiModelProperty(value = "实施建议报价")
    @Excel(name = "实施建议报价")
    private BigDecimal impSugQuote;

    /**
     * 实施对外报价
     */
    @ApiModelProperty(value = "实施对外报价")
    @Excel(name = "实施对外报价")
    private BigDecimal impExtQuote;
    /**
     * 项目成本合计
     */
    @ApiModelProperty(value = "项目成本合计")
    private BigDecimal projCostTotal;

    /**
     * 项目建议报价合计
     */
    @ApiModelProperty(value = "项目建议报价合计")
    private BigDecimal projSugQuoteTotal;

    /**
     * 项目利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(不包含外采硬件)")
    @Excel(name = "项目利润率(不包含外采硬件)")
    private BigDecimal projProfitRateExcl = BigDecimal.ZERO;

    /**
     * 项目利润率(含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(含外采硬件)")
    @Excel(name = "项目利润率(含外采硬件)")
    private BigDecimal projProfitRateIncl = BigDecimal.ZERO;


    public QuotePresaleInfoDTO(Long id, Long opportunitiesId, String preSaleVersion, BigDecimal prePrice, BigDecimal preTaxRate, BigDecimal preProfitRate,
                               BigDecimal preSugQuote, BigDecimal preExtQuote, BigDecimal devPrice, BigDecimal devTaxRate, BigDecimal devProfitRate,
                               BigDecimal devSugQuote, BigDecimal devExtQuote, BigDecimal prodPrice, BigDecimal prodTaxRate, BigDecimal prodProfitRate,
                               BigDecimal prodSugQuote, BigDecimal prodExtQuote, BigDecimal otherPrice, BigDecimal otherTaxRate, BigDecimal otherProfitRate,
                               BigDecimal otherSugQuote, BigDecimal otherExtQuote, BigDecimal selfPrice, BigDecimal selfTaxRate, BigDecimal selfProfitRate,
                               BigDecimal selfSugQuote, BigDecimal selfExtQuote, BigDecimal extPrice, BigDecimal extTaxRate, BigDecimal extProfitRate,
                               BigDecimal extSugQuote, BigDecimal extExtQuote, BigDecimal impPrice, BigDecimal impTaxRate, BigDecimal impProfitRate,
                               BigDecimal impSugQuote, BigDecimal impExtQuote, BigDecimal projCostTotal, BigDecimal projSugQuoteTotal, BigDecimal projProfitRateExcl,
                               BigDecimal projProfitRateIncl) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.preSaleVersion = preSaleVersion;
        this.prePrice = prePrice;
        this.preTaxRate = preTaxRate;
        this.preProfitRate = preProfitRate;
        this.preSugQuote = preSugQuote;
        this.preExtQuote = preExtQuote;
        this.devPrice = devPrice;
        this.devTaxRate = devTaxRate;
        this.devProfitRate = devProfitRate;
        this.devSugQuote = devSugQuote;
        this.devExtQuote = devExtQuote;
        this.prodPrice = prodPrice;
        this.prodTaxRate = prodTaxRate;
        this.prodProfitRate = prodProfitRate;
        this.prodSugQuote = prodSugQuote;
        this.prodExtQuote = prodExtQuote;
        this.otherPrice = otherPrice;
        this.otherTaxRate = otherTaxRate;
        this.otherProfitRate = otherProfitRate;
        this.otherSugQuote = otherSugQuote;
        this.otherExtQuote = otherExtQuote;
        this.selfPrice = selfPrice;
        this.selfTaxRate = selfTaxRate;
        this.selfProfitRate = selfProfitRate;
        this.selfSugQuote = selfSugQuote;
        this.selfExtQuote = selfExtQuote;
        this.extPrice = extPrice;
        this.extTaxRate = extTaxRate;
        this.extProfitRate = extProfitRate;
        this.extSugQuote = extSugQuote;
        this.extExtQuote = extExtQuote;
        this.impPrice = impPrice;
        this.impTaxRate = impTaxRate;
        this.impProfitRate = impProfitRate;
        this.impSugQuote = impSugQuote;
        this.impExtQuote = impExtQuote;
        this.projCostTotal = projCostTotal;
        this.projSugQuoteTotal = projSugQuoteTotal;
        this.projProfitRateExcl = projProfitRateExcl;
        this.projProfitRateIncl = projProfitRateIncl;
    }
}