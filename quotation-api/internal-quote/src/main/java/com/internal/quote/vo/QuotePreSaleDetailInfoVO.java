package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价系统-商机售前报价详细信息对象 QuotePreSaleDetailInfo
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuotePreSaleDetailInfoVO", description = "报价系统-商机售前报价详细信息对象")
public class QuotePreSaleDetailInfoVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 商机名称
     */
    @ApiModelProperty(value = "商机名称")
    @Excel(name = "商机名称")
    private String name;
    /**
     * 产品类别
     */
    @ApiModelProperty(value = "产品类别")
    @Excel(name = "产品类别")
    private String category;
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
     * 售前名称
     */
    @ApiModelProperty(value = "售前名称")
    @Excel(name = "售前名称")
    private String preSaleName;
    /**
     * 报价版本
     */
    @ApiModelProperty(value = "报价版本")
    @Excel(name = "报价版本")
    private String quoteVersion;
    /**
     * 粗略报价金额
     */
    @ApiModelProperty(value = "报价金额")
    @Excel(name = "报价金额")
    private BigDecimal quoteAmount;
    /**
     * 粗略报价说明
     */
    @ApiModelProperty(value = "报价说明")
    @Excel(name = "报价说明")
    private String quoteDesc;
    /**
     * 销售报价详细信息
     */
    @ApiModelProperty(value = "销售报价详细信息")
    @TableField(exist = false)
    private QuotePresaleInfoVO quotesPresaleInfo;

    public QuotePreSaleDetailInfoVO(Long id, String name, String category, String saleName, String customersName, String preSaleName,
                                    String quoteVersion, BigDecimal quoteAmount, String quoteDesc, QuotePresaleInfoVO quotesPresaleInfo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.saleName = saleName;
        this.customersName = customersName;
        this.preSaleName = preSaleName;
        this.quoteVersion = quoteVersion;
        this.quoteAmount = quoteAmount;
        this.quoteDesc = quoteDesc;
        this.quotesPresaleInfo = quotesPresaleInfo;
    }
}
