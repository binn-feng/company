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

import java.math.BigDecimal;

/**
 * 报价系统-商机报价信息对象 quote_opportunities
 *
 * @author internal
 * @date 2024-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesSaveDTO", description = "报价系统-商机报价信息对象")
public class QuoteOpportunitiesSaveDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
     * 产品类别
     */
    @ApiModelProperty(value = "产品类别")
    @Excel(name = "产品类别")
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
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    @Excel(name = "报价说明")
    private String quoteDesc;
    /**
     * 原因说明
     */
    @ApiModelProperty(value = "丢单说明")
    @Excel(name = "丢单说明")
    private String reasonDesc;

    /**
     * 报价金额
     */
    @ApiModelProperty(value = "报价金额")
    @Excel(name = "报价金额")
    private BigDecimal amount;


    public QuoteOpportunitiesSaveDTO(String name, String type, String category, Long saleId, String saleName,
                                     String customersName, String appendix, String introduce, String quoteDesc, String reasonDesc, BigDecimal amount) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.saleId = saleId;
        this.saleName = saleName;
        this.customersName = customersName;
        this.appendix = appendix;
        this.introduce = introduce;
        this.quoteDesc = quoteDesc;
        this.reasonDesc = reasonDesc;
        this.amount = amount;
    }
}