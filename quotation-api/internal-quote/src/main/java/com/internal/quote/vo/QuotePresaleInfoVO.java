package com.internal.quote.vo;

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
public class QuotePresaleInfoVO extends BaseEntity {
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
    private BigDecimal prePrice = BigDecimal.ZERO;

    /**
     * 售前支持税率
     */
    @ApiModelProperty(value = "售前支持税率")
    @Excel(name = "售前支持税率")
    private BigDecimal preTaxRate = BigDecimal.ZERO;

    /**
     * 售前支持利润率
     */
    @ApiModelProperty(value = "售前支持利润率")
    @Excel(name = "售前支持利润率")
    private BigDecimal preProfitRate = BigDecimal.ZERO;
    /**
     * 售前支持最终利润率
     */
    @ApiModelProperty(value = "售前支持最终利润率")
    @Excel(name = "售前支持最终利润率")
    private BigDecimal preActualProfitRate = BigDecimal.ZERO;

    /**
     * 售前支持建议报价
     */
    @ApiModelProperty(value = "售前支持建议报价")
    @Excel(name = "售前支持建议报价")
    private BigDecimal preSugQuote = BigDecimal.ZERO;

    /**
     * 售前支持对外报价
     */
    @ApiModelProperty(value = "售前支持对外报价")
    @Excel(name = "售前支持对外报价")
    private BigDecimal preExtQuote = BigDecimal.ZERO;

    /**
     * 定制开发成本价
     */
    @ApiModelProperty(value = "定制开发成本价")
    @Excel(name = "定制开发成本价")
    private BigDecimal devPrice = BigDecimal.ZERO;

    /**
     * 定制开发税率
     */
    @ApiModelProperty(value = "定制开发税率")
    @Excel(name = "定制开发税率")
    private BigDecimal devTaxRate = BigDecimal.ZERO;

    /**
     * 定制开发利润率
     */
    @ApiModelProperty(value = "定制开发利润率")
    @Excel(name = "定制开发利润率")
    private BigDecimal devProfitRate = BigDecimal.ZERO;
    /**
     * 定制开发最终利润率
     */
    @ApiModelProperty(value = "定制开发最终利润率")
    @Excel(name = "定制开发最终利润率")
    private BigDecimal devActualProfitRate = BigDecimal.ZERO;

    /**
     * 定制开发建议报价
     */
    @ApiModelProperty(value = "定制开发建议报价")
    @Excel(name = "定制开发建议报价")
    private BigDecimal devSugQuote = BigDecimal.ZERO;

    /**
     * 定制开发对外报价
     */
    @ApiModelProperty(value = "定制开发对外报价")
    @Excel(name = "定制开发对外报价")
    private BigDecimal devExtQuote = BigDecimal.ZERO;

    /**
     * 产品平台成本价
     */
    @ApiModelProperty(value = "产品平台成本价")
    @Excel(name = "产品平台成本价")
    private BigDecimal prodPrice = BigDecimal.ZERO;

    /**
     * 产品平台税率
     */
    @ApiModelProperty(value = "产品平台税率")
    @Excel(name = "产品平台税率")
    private BigDecimal prodTaxRate = BigDecimal.ZERO;

    /**
     * 产品平台利润率
     */
    @ApiModelProperty(value = "产品平台利润率")
    @Excel(name = "产品平台利润率")
    private BigDecimal prodProfitRate = BigDecimal.ZERO;
    /**
     * 产品平台最终利润率
     */
    @ApiModelProperty(value = "产品平台最终利润率")
    @Excel(name = "产品平台最终利润率")
    private BigDecimal prodActualProfitRate = BigDecimal.ZERO;

    /**
     * 产品平台建议报价
     */
    @ApiModelProperty(value = "产品平台建议报价")
    @Excel(name = "产品平台建议报价")
    private BigDecimal prodSugQuote = BigDecimal.ZERO;

    /**
     * 产品平台对外报价
     */
    @ApiModelProperty(value = "产品平台对外报价")
    @Excel(name = "产品平台对外报价")
    private BigDecimal prodExtQuote = BigDecimal.ZERO;

    /**
     * 其他成本价
     */
    @ApiModelProperty(value = "其他成本价")
    @Excel(name = "其他成本价")
    private BigDecimal otherPrice = BigDecimal.ZERO;

    /**
     * 其他税率
     */
    @ApiModelProperty(value = "其他税率")
    @Excel(name = "其他税率")
    private BigDecimal otherTaxRate = BigDecimal.ZERO;

    /**
     * 其他利润率
     */
    @ApiModelProperty(value = "其他利润率")
    @Excel(name = "其他利润率")
    private BigDecimal otherProfitRate = BigDecimal.ZERO;
    /**
     * 其他最终利润率
     */
    @ApiModelProperty(value = "其他最终利润率")
    @Excel(name = "其他最终利润率")
    private BigDecimal otherActualProfitRate = BigDecimal.ZERO;

    /**
     * 其他建议报价
     */
    @ApiModelProperty(value = "其他建议报价")
    @Excel(name = "其他建议报价")
    private BigDecimal otherSugQuote = BigDecimal.ZERO;

    /**
     * 其他对外报价
     */
    @ApiModelProperty(value = "其他对外报价")
    @Excel(name = "其他对外报价")
    private BigDecimal otherExtQuote = BigDecimal.ZERO;

    /**
     * 自研成本价
     */
    @ApiModelProperty(value = "自研成本价")
    @Excel(name = "自研成本价")
    private BigDecimal selfPrice = BigDecimal.ZERO;

    /**
     * 自研税率
     */
    @ApiModelProperty(value = "自研税率")
    @Excel(name = "自研税率")
    private BigDecimal selfTaxRate = BigDecimal.ZERO;

    /**
     * 自研利润率
     */
    @ApiModelProperty(value = "自研利润率")
    @Excel(name = "自研利润率")
    private BigDecimal selfProfitRate = BigDecimal.ZERO;
    /**
     * 自研最终利润率
     */
    @ApiModelProperty(value = "自研最终利润率")
    @Excel(name = "自研最终利润率")
    private BigDecimal selfActualProfitRate = BigDecimal.ZERO;

    /**
     * 自研建议报价
     */
    @ApiModelProperty(value = "自研建议报价")
    @Excel(name = "自研建议报价")
    private BigDecimal selfSugQuote = BigDecimal.ZERO;

    /**
     * 自研对外报价
     */
    @ApiModelProperty(value = "自研对外报价")
    @Excel(name = "自研对外报价")
    private BigDecimal selfExtQuote = BigDecimal.ZERO;

    /**
     * 外采成本价
     */
    @ApiModelProperty(value = "外采成本价")
    @Excel(name = "外采成本价")
    private BigDecimal extPrice = BigDecimal.ZERO;

    /**
     * 外采税率
     */
    @ApiModelProperty(value = "外采税率")
    @Excel(name = "外采税率")
    private BigDecimal extTaxRate = BigDecimal.ZERO;

    /**
     * 外采利润率
     */
    @ApiModelProperty(value = "外采利润率")
    @Excel(name = "外采利润率")
    private BigDecimal extProfitRate = BigDecimal.ZERO;
    /**
     * 外采最终利润率
     */
    @ApiModelProperty(value = "外采最终利润率")
    @Excel(name = "外采最终利润率")
    private BigDecimal extActualProfitRate = BigDecimal.ZERO;

    /**
     * 外采建议报价
     */
    @ApiModelProperty(value = "外采建议报价")
    @Excel(name = "外采建议报价")
    private BigDecimal extSugQuote = BigDecimal.ZERO;

    /**
     * 外采对外报价
     */
    @ApiModelProperty(value = "外采对外报价")
    @Excel(name = "外采对外报价")
    private BigDecimal extExtQuote = BigDecimal.ZERO;


    /**
     * 外采代理成本价
     */
    @ApiModelProperty(value = "外采代理成本价")
    @Excel(name = "外采代理成本价")
    private BigDecimal extProxyPrice;

    /**
     * 外采代理税率
     */
    @ApiModelProperty(value = "外采代理税率")
    @Excel(name = "外采代理税率")
    private BigDecimal extProxyTaxRate;

    /**
     * 外采代理利润率
     */
    @ApiModelProperty(value = "外采代理利润率")
    @Excel(name = "外采代理利润率")
    private BigDecimal extProxyProfitRate;

    /**
     * 外采代理建议报价
     */
    @ApiModelProperty(value = "外采代理建议报价")
    @Excel(name = "外采代理建议报价")
    private BigDecimal extProxySugQuote;

    /**
     * 外采代理对外报价
     */
    @ApiModelProperty(value = "外采代理对外报价")
    @Excel(name = "外采代理对外报价")
    private BigDecimal extProxyExtQuote;

    /**
     * 实施成本价
     */
    @ApiModelProperty(value = "实施成本价")
    @Excel(name = "实施成本价")
    private BigDecimal impPrice = BigDecimal.ZERO;

    /**
     * 实施税率
     */
    @ApiModelProperty(value = "实施税率")
    @Excel(name = "实施税率")
    private BigDecimal impTaxRate = BigDecimal.ZERO;

    /**
     * 实施利润率
     */
    @ApiModelProperty(value = "实施利润率")
    @Excel(name = "实施利润率")
    private BigDecimal impProfitRate = BigDecimal.ZERO;
    /**
     * 实施最终利润率
     */
    @ApiModelProperty(value = "实施最终利润率")
    @Excel(name = "实施最终利润率")
    private BigDecimal impActualProfitRate = BigDecimal.ZERO;

    /**
     * 实施建议报价
     */
    @ApiModelProperty(value = "实施建议报价")
    @Excel(name = "实施建议报价")
    private BigDecimal impSugQuote = BigDecimal.ZERO;

    /**
     * 实施对外报价
     */
    @ApiModelProperty(value = "实施对外报价")
    @Excel(name = "实施对外报价")
    private BigDecimal impExtQuote = BigDecimal.ZERO;

    /**
     * 软件费用小计-成本
     */
    @ApiModelProperty(value = "软件费用小计-成本")
    private BigDecimal softWareCostTotal = BigDecimal.ZERO;
    /**
     * 软件费用小计-建议报价
     */
    @ApiModelProperty(value = "软件费用小计-建议报价")
    private BigDecimal softWareSugQuoteTotal = BigDecimal.ZERO;
    /**
     * 软件费用小计-对外报价
     */
    @ApiModelProperty(value = "软件费用小计-对外报价")
    private BigDecimal softWareExtQuoteTotal = BigDecimal.ZERO;
    /**
     * 硬件费用小计-成本
     */
    @ApiModelProperty(value = "硬件费用小计-成本")
    private BigDecimal hardWareCostTotal = BigDecimal.ZERO;
    /**
     * 硬件费用小计-建议报价
     */
    @ApiModelProperty(value = "硬件费用小计-建议报价")
    private BigDecimal hardWareSugQuoteTotal = BigDecimal.ZERO;
    /**
     * 硬件费用小计-对外报价
     */
    @ApiModelProperty(value = "硬件费用小计-对外报价")
    private BigDecimal hardWareExtQuoteTotal = BigDecimal.ZERO;

    /**
     * 项目成本合计
     */
    @ApiModelProperty(value = "项目成本合计")
    private BigDecimal projCostTotal = BigDecimal.ZERO;

    /**
     * 项目建议报价合计
     */
    @ApiModelProperty(value = "项目建议报价合计")
    private BigDecimal projSugQuoteTotal = BigDecimal.ZERO;
    /**
     * 项目对外报价合计
     */
    @ApiModelProperty(value = "项目对外报价合计")
    private BigDecimal projExtQuoteTotal = BigDecimal.ZERO;

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
    /**
     * 审核理由
     */
    @ApiModelProperty(value = "审核理由")
    @TableField(value = "sale_audit_desc")
    private String saleAuditDesc;
    /**
     * 总金额
     */
    @ApiModelProperty(value = "总金额(无法报价)")
    @Excel(name = "总金额")
    private BigDecimal unableQuoteAmount = BigDecimal.ZERO;
    /**
     * 情况说明(无法报价)
     */
    @ApiModelProperty(value = "情况说明(无法报价)")
    @Excel(name = "情况说明(无法报价)")
    private String description;

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String signType;

    /** 欣象售前支持报价 */
    @ApiModelProperty(value = "欣象售前支持报价")
    @Excel(name = "欣象售前支持报价")
    private BigDecimal xxPreQuote = BigDecimal.ZERO;

    /** 欣象定制开发报价 */
    @ApiModelProperty(value = "欣象定制开发报价")
    @Excel(name = "欣象定制开发报价")
    private BigDecimal xxDevQuote = BigDecimal.ZERO;

    /** 欣象产品平台报价 */
    @ApiModelProperty(value = "欣象产品平台报价")
    @Excel(name = "欣象产品平台报价")
    private BigDecimal xxProdQuote = BigDecimal.ZERO;

    /** 欣象其他报价 */
    @ApiModelProperty(value = "欣象其他报价")
    @Excel(name = "欣象其他报价")
    private BigDecimal xxOtherQuote = BigDecimal.ZERO;

    /** 欣象自研报价 */
    @ApiModelProperty(value = "欣象自研报价")
    @Excel(name = "欣象自研报价")
    private BigDecimal xxSelfQuote = BigDecimal.ZERO;

    /** 欣象外采报价 */
    @ApiModelProperty(value = "欣象外采报价")
    @Excel(name = "欣象外采报价")
    private BigDecimal xxExtQuote = BigDecimal.ZERO;
    /**
     * 欣象外采代理报价
     */
    @ApiModelProperty(value = "欣象外采代理报价")
    @Excel(name = "欣象外采代理报价")
    private BigDecimal xxExtProxyQuote = BigDecimal.ZERO;

    /** 欣象实施报价 */
    @ApiModelProperty(value = "欣象实施报价")
    @Excel(name = "欣象实施报价")
    private BigDecimal xxImpQuote = BigDecimal.ZERO;

    /** 欣象试报价金额(无法报价) */
    @ApiModelProperty(value = "欣象试报价金额(无法报价)")
    @Excel(name = "欣象试报价金额(无法报价)")
    private BigDecimal xxUnableQuoteAmount = BigDecimal.ZERO;

    /** 欣象软件报价小计 */
    @ApiModelProperty(value = "欣象软件报价小计")
    @Excel(name = "欣象软件报价小计")
    private BigDecimal xxSoftWareQuoteTotal = BigDecimal.ZERO;

    /** 欣象硬件报价小计 */
    @ApiModelProperty(value = "欣象硬件报价小计")
    @Excel(name = "欣象硬件报价小计")
    private BigDecimal xxHardWareQuoteTotal = BigDecimal.ZERO;

    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;

    /**
     * 成本报价版本(报价类型+报价版本号)
     */
    @ApiModelProperty(value = "成本报价版本(报价类型+报价版本号)")
    private String currentVersion;

    /**
     * 粗略报价说明
     */
    @ApiModelProperty(value = "粗略报价说明")
    private String roughDesc;

    /**
     * 成本报价id
     */
    @ApiModelProperty(value = "成本报价id")
    @Excel(name = "成本报价id")
    private Long costId;

    /**
     * 关单或跟进说明
     */
    @ApiModelProperty(value = "关单或跟进说明")
    @Excel(name = "关单或跟进说明")
    private String closeNote;

    /** 欣象其它费用 */
    @ApiModelProperty(value = "欣象其它费用")
    @Excel(name = "欣象其它费用")
    private BigDecimal xxAdditionalQuote;
}