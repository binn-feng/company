package com.internal.quote.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价系统-商机售前报价信息对象 quote_presale_info
 *
 * @author internal
 * @date 2024-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuotePresaleInfo", description = "报价系统-商机售前报价信息对象")
@TableName("quote_presale_info")
public class QuotePresaleInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @TableField(value = "opportunities_id")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;

    /**
     * 商机主表ID
     */
    @ApiModelProperty(value = "商机主表ID")
    @TableField(value = "opportunities_parent_id")
    @Excel(name = "商机主表ID")
    private Long opportunitiesParentId;

    /**
     * 售前报价版本
     */
    @ApiModelProperty(value = "售前报价版本")
    @TableField(value = "pre_sale_version")
    @Excel(name = "售前报价版本")
    private String preSaleVersion;

    /**
     * 售前成本价
     */
    @ApiModelProperty(value = "售前成本价")
    @TableField(value = "pre_price")
    @Excel(name = "售前成本价")
    private BigDecimal prePrice;

    /**
     * 售前支持税率
     */
    @ApiModelProperty(value = "售前支持税率")
    @TableField(value = "pre_tax_rate")
    @Excel(name = "售前支持税率")
    private BigDecimal preTaxRate;

    /**
     * 售前支持利润率
     */
    @ApiModelProperty(value = "售前支持利润率")
    @TableField(value = "pre_profit_rate")
    @Excel(name = "售前支持利润率")
    private BigDecimal preProfitRate;

    /**
     * 售前支持建议报价
     */
    @ApiModelProperty(value = "售前支持建议报价")
    @TableField(value = "pre_sug_quote")
    @Excel(name = "售前支持建议报价")
    private BigDecimal preSugQuote;

    /**
     * 售前支持对外报价
     */
    @ApiModelProperty(value = "售前支持对外报价")
    @TableField(value = "pre_ext_quote")
    @Excel(name = "售前支持对外报价")
    private BigDecimal preExtQuote;

    /**
     * 定制开发成本价
     */
    @ApiModelProperty(value = "定制开发成本价")
    @TableField(value = "dev_price")
    @Excel(name = "定制开发成本价")
    private BigDecimal devPrice;

    /**
     * 定制开发税率
     */
    @ApiModelProperty(value = "定制开发税率")
    @TableField(value = "dev_tax_rate")
    @Excel(name = "定制开发税率")
    private BigDecimal devTaxRate;

    /**
     * 定制开发利润率
     */
    @ApiModelProperty(value = "定制开发利润率")
    @TableField(value = "dev_profit_rate")
    @Excel(name = "定制开发利润率")
    private BigDecimal devProfitRate;

    /**
     * 定制开发建议报价
     */
    @ApiModelProperty(value = "定制开发建议报价")
    @TableField(value = "dev_sug_quote")
    @Excel(name = "定制开发建议报价")
    private BigDecimal devSugQuote;

    /**
     * 定制开发对外报价
     */
    @ApiModelProperty(value = "定制开发对外报价")
    @TableField(value = "dev_ext_quote")
    @Excel(name = "定制开发对外报价")
    private BigDecimal devExtQuote;

    /**
     * 产品平台成本价
     */
    @ApiModelProperty(value = "产品平台成本价")
    @TableField(value = "prod_price")
    @Excel(name = "产品平台成本价")
    private BigDecimal prodPrice;

    /**
     * 产品平台税率
     */
    @ApiModelProperty(value = "产品平台税率")
    @TableField(value = "prod_tax_rate")
    @Excel(name = "产品平台税率")
    private BigDecimal prodTaxRate;

    /**
     * 产品平台利润率
     */
    @ApiModelProperty(value = "产品平台利润率")
    @TableField(value = "prod_profit_rate")
    @Excel(name = "产品平台利润率")
    private BigDecimal prodProfitRate;

    /**
     * 产品平台建议报价
     */
    @ApiModelProperty(value = "产品平台建议报价")
    @TableField(value = "prod_sug_quote")
    @Excel(name = "产品平台建议报价")
    private BigDecimal prodSugQuote;

    /**
     * 产品平台对外报价
     */
    @ApiModelProperty(value = "产品平台对外报价")
    @TableField(value = "prod_ext_quote")
    @Excel(name = "产品平台对外报价")
    private BigDecimal prodExtQuote;

    /**
     * 其他成本价
     */
    @ApiModelProperty(value = "其他成本价")
    @TableField(value = "other_price")
    @Excel(name = "其他成本价")
    private BigDecimal otherPrice;

    /**
     * 其他税率
     */
    @ApiModelProperty(value = "其他税率")
    @TableField(value = "other_tax_rate")
    @Excel(name = "其他税率")
    private BigDecimal otherTaxRate;

    /**
     * 其他利润率
     */
    @ApiModelProperty(value = "其他利润率")
    @TableField(value = "other_profit_rate")
    @Excel(name = "其他利润率")
    private BigDecimal otherProfitRate;

    /**
     * 其他建议报价
     */
    @ApiModelProperty(value = "其他建议报价")
    @TableField(value = "other_sug_quote")
    @Excel(name = "其他建议报价")
    private BigDecimal otherSugQuote;

    /**
     * 其他对外报价
     */
    @ApiModelProperty(value = "其他对外报价")
    @TableField(value = "other_ext_quote")
    @Excel(name = "其他对外报价")
    private BigDecimal otherExtQuote;

    /**
     * 自研成本价
     */
    @ApiModelProperty(value = "自研成本价")
    @TableField(value = "self_price")
    @Excel(name = "自研成本价")
    private BigDecimal selfPrice;

    /**
     * 自研税率
     */
    @ApiModelProperty(value = "自研税率")
    @TableField(value = "self_tax_rate")
    @Excel(name = "自研税率")
    private BigDecimal selfTaxRate;

    /**
     * 自研利润率
     */
    @ApiModelProperty(value = "自研利润率")
    @TableField(value = "self_profit_rate")
    @Excel(name = "自研利润率")
    private BigDecimal selfProfitRate;

    /**
     * 自研建议报价
     */
    @ApiModelProperty(value = "自研建议报价")
    @TableField(value = "self_sug_quote")
    @Excel(name = "自研建议报价")
    private BigDecimal selfSugQuote;

    /**
     * 自研对外报价
     */
    @ApiModelProperty(value = "自研对外报价")
    @TableField(value = "self_ext_quote")
    @Excel(name = "自研对外报价")
    private BigDecimal selfExtQuote;

    /**
     * 外采成本价
     */
    @ApiModelProperty(value = "外采成本价")
    @TableField(value = "ext_price")
    @Excel(name = "外采成本价")
    private BigDecimal extPrice;

    /**
     * 外采税率
     */
    @ApiModelProperty(value = "外采税率")
    @TableField(value = "ext_tax_rate")
    @Excel(name = "外采税率")
    private BigDecimal extTaxRate;

    /**
     * 外采利润率
     */
    @ApiModelProperty(value = "外采利润率")
    @TableField(value = "ext_profit_rate")
    @Excel(name = "外采利润率")
    private BigDecimal extProfitRate;

    /**
     * 外采建议报价
     */
    @ApiModelProperty(value = "外采建议报价")
    @TableField(value = "ext_sug_quote")
    @Excel(name = "外采建议报价")
    private BigDecimal extSugQuote;

    /**
     * 外采对外报价
     */
    @ApiModelProperty(value = "外采对外报价")
    @TableField(value = "ext_ext_quote")
    @Excel(name = "外采对外报价")
    private BigDecimal extExtQuote;

    /**
     * 外采代理成本价
     */
    @ApiModelProperty(value = "外采代理成本价")
    @TableField(value = "ext_proxy_price")
    @Excel(name = "外采代理成本价")
    private BigDecimal extProxyPrice;

    /**
     * 外采代理税率
     */
    @ApiModelProperty(value = "外采代理税率")
    @TableField(value = "ext_proxy_tax_rate")
    @Excel(name = "外采代理税率")
    private BigDecimal extProxyTaxRate;

    /**
     * 外采代理利润率
     */
    @ApiModelProperty(value = "外采代理利润率")
    @TableField(value = "ext_proxy_profit_rate")
    @Excel(name = "外采代理利润率")
    private BigDecimal extProxyProfitRate;

    /**
     * 外采代理建议报价
     */
    @ApiModelProperty(value = "外采代理建议报价")
    @TableField(value = "ext_proxy_sug_quote")
    @Excel(name = "外采代理建议报价")
    private BigDecimal extProxySugQuote;

    /**
     * 外采代理对外报价
     */
    @ApiModelProperty(value = "外采代理对外报价")
    @TableField(value = "ext_proxy_ext_quote")
    @Excel(name = "外采代理对外报价")
    private BigDecimal extProxyExtQuote;

    /**
     * 实施成本价
     */
    @ApiModelProperty(value = "实施成本价")
    @TableField(value = "imp_price")
    @Excel(name = "实施成本价")
    private BigDecimal impPrice;

    /**
     * 实施税率
     */
    @ApiModelProperty(value = "实施税率")
    @TableField(value = "imp_tax_rate")
    @Excel(name = "实施税率")
    private BigDecimal impTaxRate;

    /**
     * 实施利润率
     */
    @ApiModelProperty(value = "实施利润率")
    @TableField(value = "imp_profit_rate")
    @Excel(name = "实施利润率")
    private BigDecimal impProfitRate;

    /**
     * 实施建议报价
     */
    @ApiModelProperty(value = "实施建议报价")
    @TableField(value = "imp_sug_quote")
    @Excel(name = "实施建议报价")
    private BigDecimal impSugQuote;

    /**
     * 实施对外报价
     */
    @ApiModelProperty(value = "实施对外报价")
    @TableField(value = "imp_ext_quote")
    @Excel(name = "实施对外报价")
    private BigDecimal impExtQuote;

    /**
     * 项目利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(不包含外采硬件)")
    @TableField(value = "proj_profit_rate_excl")
    @Excel(name = "项目利润率(不包含外采硬件)")
    private BigDecimal projProfitRateExcl;

    /**
     * 项目利润率(含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(含外采硬件)")
    @TableField(value = "proj_profit_rate_incl")
    @Excel(name = "项目利润率(含外采硬件)")
    private BigDecimal projProfitRateIncl;

    /**
     * 是否继续跟单
     */
    @ApiModelProperty(value = "是否继续跟单")
    @TableField(value = "follow_up")
    @Excel(name = "是否继续跟单")
    private Integer followUp;

    /**
     * 关单或跟进说明
     */
    @ApiModelProperty(value = "关单或跟进说明")
    @TableField(value = "close_note")
    @Excel(name = "关单或跟进说明")
    private String closeNote;

    /**
     * 版本状态[0暂存;未生效;2生效中]
     */
    @ApiModelProperty(value = "版本状态[0暂存;未生效;2生效中]")
    @TableField(value = "status")
    @Excel(name = "版本状态[0暂存;未生效;2生效中]")
    private String status;

    /**
     * 审批理由
     */
    @ApiModelProperty(value = "审批理由")
    @TableField(value = "sale_audit_desc")
    @Excel(name = "审批理由")
    private String saleAuditDesc;

    /**
     * 试报价金额(无法报价)
     */
    @ApiModelProperty(value = "试报价金额(无法报价)")
    @TableField(value = "unable_quote_amount")
    @Excel(name = "试报价金额(无法报价)")
    private BigDecimal unableQuoteAmount;

    /**
     * 情况说明(无法报价)
     */
    @ApiModelProperty(value = "情况说明(无法报价)")
    @TableField(value = "description")
    @Excel(name = "情况说明(无法报价)")
    private String description;

    /**
     * 合同类型(欣象代理:1,北光直签:2)
     */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @TableField(value = "sign_type")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String signType;

    /**
     * 欣象售前支持报价
     */
    @ApiModelProperty(value = "欣象售前支持报价")
    @TableField(value = "xx_pre_quote")
    @Excel(name = "欣象售前支持报价")
    private BigDecimal xxPreQuote;

    /**
     * 欣象定制开发报价
     */
    @ApiModelProperty(value = "欣象定制开发报价")
    @TableField(value = "xx_dev_quote")
    @Excel(name = "欣象定制开发报价")
    private BigDecimal xxDevQuote;

    /**
     * 欣象产品平台报价
     */
    @ApiModelProperty(value = "欣象产品平台报价")
    @TableField(value = "xx_prod_quote")
    @Excel(name = "欣象产品平台报价")
    private BigDecimal xxProdQuote;

    /**
     * 欣象其他报价
     */
    @ApiModelProperty(value = "欣象其他报价")
    @TableField(value = "xx_other_quote")
    @Excel(name = "欣象其他报价")
    private BigDecimal xxOtherQuote;

    /**
     * 欣象自研报价
     */
    @ApiModelProperty(value = "欣象自研报价")
    @TableField(value = "xx_self_quote")
    @Excel(name = "欣象自研报价")
    private BigDecimal xxSelfQuote;

    /**
     * 欣象外采报价
     */
    @ApiModelProperty(value = "欣象外采报价")
    @TableField(value = "xx_ext_quote")
    @Excel(name = "欣象外采报价")
    private BigDecimal xxExtQuote;

    /**
     * 欣象外采代理报价
     */
    @ApiModelProperty(value = "欣象外采代理报价")
    @TableField(value = "xx_ext_proxy_quote")
    @Excel(name = "欣象外采代理报价")
    private BigDecimal xxExtProxyQuote;

    /**
     * 欣象实施报价
     */
    @ApiModelProperty(value = "欣象实施报价")
    @TableField(value = "xx_imp_quote")
    @Excel(name = "欣象实施报价")
    private BigDecimal xxImpQuote;

    /**
     * 欣象试报价金额(无法报价)
     */
    @ApiModelProperty(value = "欣象试报价金额(无法报价)")
    @TableField(value = "xx_unable_quote_amount")
    @Excel(name = "欣象试报价金额(无法报价)")
    private BigDecimal xxUnableQuoteAmount;
    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @TableField(value = "type")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;

    /**
     * 成本报价id
     */
    @ApiModelProperty(value = "成本报价id")
    @TableField(value = "cost_id")
    @Excel(name = "成本报价id")
    private Long costId;

    /** 欣象其它费用 */
    @ApiModelProperty(value = "欣象其它费用")
    @Excel(name = "欣象其它费用")
    @TableField(value = "xx_additional_quote")
    private BigDecimal xxAdditionalQuote;

}