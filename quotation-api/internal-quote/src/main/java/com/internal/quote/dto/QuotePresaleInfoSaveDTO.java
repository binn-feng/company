package com.internal.quote.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "QuotePresaleInfoSaveDTO", description = "报价系统-商机售前报价信息对象")
public class QuotePresaleInfoSaveDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;
    /**
     * 商机主表ID
     */
    @ApiModelProperty(value = "商机主表ID")
    @Excel(name = "商机主表ID")
    private Long opportunitiesParentId;

    /**
     * 无法报价
     */
    @ApiModelProperty(value = "无法报价")
    @Excel(name = "无法报价")
    private boolean unableQuote = false;
    /**
     * 总金额
     */
    @ApiModelProperty(value = "总金额(无法报价)")
    @Excel(name = "总金额")
    private BigDecimal unableQuoteAmount;

    /**
     * 情况说明(无法报价)
     */
    @ApiModelProperty(value = "情况说明(无法报价)")
    @Excel(name = "情况说明(无法报价)")
    private String description;

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
     * 外采代理成本价
     */
    @ApiModelProperty(value = "外采代理成本价")
    @Excel(name = "外采代理成本价")
    private BigDecimal extProxyPrice = BigDecimal.ZERO;

    /**
     * 外采代理税率
     */
    @ApiModelProperty(value = "外采代理税率")
    @Excel(name = "外采代理税率")
    private BigDecimal extProxyTaxRate = BigDecimal.ZERO;

    /**
     * 外采代理利润率
     */
    @ApiModelProperty(value = "外采代理利润率")
    @Excel(name = "外采代理利润率")
    private BigDecimal extProxyProfitRate = BigDecimal.ZERO;

    /**
     * 外采代理建议报价
     */
    @ApiModelProperty(value = "外采代理建议报价")
    @Excel(name = "外采代理建议报价")
    private BigDecimal extProxySugQuote = BigDecimal.ZERO;

    /**
     * 外采代理对外报价
     */
    @ApiModelProperty(value = "外采代理对外报价")
    @Excel(name = "外采代理对外报价")
    private BigDecimal extProxyExtQuote = BigDecimal.ZERO;

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
     * 项目利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(不包含外采硬件)")
    @Excel(name = "项目利润率(不包含外采硬件)")
    private BigDecimal projProfitRateExcl;

    /**
     * 项目利润率(含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(含外采硬件)")
    @Excel(name = "项目利润率(含外采硬件)")
    private BigDecimal projProfitRateIncl;

    /**
     * 关单或跟进说明
     */
    @ApiModelProperty(value = "关单或跟进说明")
    @Excel(name = "关单或跟进说明")
    private String closeNote;

    /**
     * 成本报价总额
     */
    @ApiModelProperty(value = "成本报价总额")
    @Excel(name = "成本报价总额")
    private BigDecimal projCostTotal;

    /**
     * 销售对外报价总额
     */
    @ApiModelProperty(value = "销售对外报价总额")
    @Excel(name = "销售对外报价总额")
    private BigDecimal projSugQuoteTotal;

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String signType;

    /** 欣象售前支持报价 */
    @ApiModelProperty(value = "欣象售前支持报价")
    @Excel(name = "欣象售前支持报价")
    private BigDecimal xxPreQuote;

    /** 欣象定制开发报价 */
    @ApiModelProperty(value = "欣象定制开发报价")
    @Excel(name = "欣象定制开发报价")
    private BigDecimal xxDevQuote;

    /** 欣象产品平台报价 */
    @ApiModelProperty(value = "欣象产品平台报价")
    @Excel(name = "欣象产品平台报价")
    private BigDecimal xxProdQuote;

    /** 欣象其他报价 */
    @ApiModelProperty(value = "欣象其他报价")
    @Excel(name = "欣象其他报价")
    private BigDecimal xxOtherQuote;

    /** 欣象自研报价 */
    @ApiModelProperty(value = "欣象自研报价")
    @Excel(name = "欣象自研报价")
    private BigDecimal xxSelfQuote;

    /** 欣象外采报价 */
    @ApiModelProperty(value = "欣象外采报价")
    @Excel(name = "欣象外采报价")
    private BigDecimal xxExtQuote;

    /**
     * 欣象外采代理报价
     */
    @ApiModelProperty(value = "欣象外采代理报价")
    @Excel(name = "欣象外采代理报价")
    private BigDecimal xxExtProxyQuote;

    /** 欣象实施报价 */
    @ApiModelProperty(value = "欣象实施报价")
    @Excel(name = "欣象实施报价")
    private BigDecimal xxImpQuote;

    /** 欣象试报价金额(无法报价) */
    @ApiModelProperty(value = "欣象试报价金额(无法报价)")
    @Excel(name = "欣象试报价金额(无法报价)")
    private BigDecimal xxUnableQuoteAmount;

    /**
     * 销售对外报价合计
     */
    @ApiModelProperty(value = "销售对外报价合计")
    @Excel(name = "销售对外报价合计")
    private BigDecimal projExtQuoteTotal;

    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;

    /** 欣象其它费用 */
    @ApiModelProperty(value = "欣象其它费用")
    @Excel(name = "欣象其它费用")
    private BigDecimal xxAdditionalQuote;
}