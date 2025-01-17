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
 * 报价系统-商机粗略详细报价对象 quote_opportunities_rough_detail
 *
 * @author internal
 * @date 2024-10-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesRoughDetail", description = "报价系统-商机粗略详细报价对象")
@TableName("quote_opportunities_rough_detail")
public class QuoteOpportunitiesRoughDetail extends BaseEntity {
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
     * 商机粗略报价ID
     */
    @ApiModelProperty(value = "商机粗略报价ID")
    @TableField(value = "rough_id")
    @Excel(name = "商机粗略报价ID")
    private Long roughId;

    /**
     * 售前支持报价说明
     */
    @ApiModelProperty(value = "售前支持报价说明")
    @TableField(value = "support_desc")
    @Excel(name = "售前支持报价说明")
    private String supportDesc;

    /**
     * 售前支持报价金额
     */
    @ApiModelProperty(value = "售前支持报价金额")
    @TableField(value = "support_amount")
    @Excel(name = "售前支持报价金额")
    private BigDecimal supportAmount;

    /**
     * 软件小记报价说明
     */
    @ApiModelProperty(value = "软件小记报价说明")
    @TableField(value = "custom_desc")
    @Excel(name = "软件小记报价说明")
    private String customDesc;

    /**
     * 软件小记报价金额
     */
    @ApiModelProperty(value = "软件小记报价金额")
    @TableField(value = "custom_amount")
    @Excel(name = "软件小记报价金额")
    private BigDecimal customAmount;

    /**
     * 产品小记报价说明
     */
    @ApiModelProperty(value = "产品小记报价说明")
    @TableField(value = "product_desc")
    @Excel(name = "产品小记报价说明")
    private String productDesc;

    /**
     * 产品小记报价金额
     */
    @ApiModelProperty(value = "产品小记报价金额")
    @TableField(value = "product_amount")
    @Excel(name = "产品小记报价金额")
    private BigDecimal productAmount;

    /**
     * 自研小记报价说明
     */
    @ApiModelProperty(value = "自研小记报价说明")
    @TableField(value = "self_desc")
    @Excel(name = "自研小记报价说明")
    private String selfDesc;

    /**
     * 自研小记报价金额
     */
    @ApiModelProperty(value = "自研小记报价金额")
    @TableField(value = "self_amount")
    @Excel(name = "自研小记报价金额")
    private BigDecimal selfAmount;

    /**
     * 外采小记报价说明
     */
    @ApiModelProperty(value = "外采小记报价说明")
    @TableField(value = "external_desc")
    @Excel(name = "外采小记报价说明")
    private String externalDesc;

    /**
     * 外采小记报价金额
     */
    @ApiModelProperty(value = "外采小记报价金额")
    @TableField(value = "external_amount")
    @Excel(name = "外采小记报价金额")
    private BigDecimal externalAmount;

    /**
     * 实施小记报价说明
     */
    @ApiModelProperty(value = "实施小记报价说明")
    @TableField(value = "implement_desc")
    @Excel(name = "实施小记报价说明")
    private String implementDesc;

    /**
     * 实施小记报价金额
     */
    @ApiModelProperty(value = "实施小记报价金额")
    @TableField(value = "implement_amount")
    @Excel(name = "实施小记报价金额")
    private BigDecimal implementAmount;

    /**
     * 其他小记报价说明
     */
    @ApiModelProperty(value = "其他小记报价说明")
    @TableField(value = "other_desc")
    @Excel(name = "其他小记报价说明")
    private String otherDesc;

    /**
     * 其他小记报价金额
     */
    @ApiModelProperty(value = "其他小记报价金额")
    @TableField(value = "other_amount")
    @Excel(name = "其他小记报价金额")
    private BigDecimal otherAmount;


    public QuoteOpportunitiesRoughDetail(Long id, Long opportunitiesId, Long roughId, String supportDesc, BigDecimal supportAmount, String customDesc, BigDecimal customAmount, String productDesc, BigDecimal productAmount, String selfDesc, BigDecimal selfAmount, String externalDesc, BigDecimal externalAmount, String implementDesc, BigDecimal implementAmount, String otherDesc, BigDecimal otherAmount) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.roughId = roughId;
        this.supportDesc = supportDesc;
        this.supportAmount = supportAmount;
        this.customDesc = customDesc;
        this.customAmount = customAmount;
        this.productDesc = productDesc;
        this.productAmount = productAmount;
        this.selfDesc = selfDesc;
        this.selfAmount = selfAmount;
        this.externalDesc = externalDesc;
        this.externalAmount = externalAmount;
        this.implementDesc = implementDesc;
        this.implementAmount = implementAmount;
        this.otherDesc = otherDesc;
        this.otherAmount = otherAmount;
    }
}