package com.internal.quote.domain;

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
import java.util.List;

/**
 * 报价系统-商机粗略报价对象 quote_opportunities_rough
 *
 * @author internal
 * @date 2024-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesRough", description = "报价系统-商机粗略报价对象")
@TableName("quote_opportunities_rough")
public class QuoteOpportunitiesRough extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
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
     * 评估版本
     */
    @ApiModelProperty(value = "评估版本")
    @TableField(value = "valuation_version")
    @Excel(name = "评估版本")
    private String valuationVersion;

    /**
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    @TableField(value = "valuation_desc")
    @Excel(name = "报价说明")
    private String valuationDesc;

    /**
     * 报价金额
     */
    @ApiModelProperty(value = "报价金额")
    @TableField(value = "amount")
    @Excel(name = "报价金额")
    private BigDecimal amount;

    /**
     * 版本状态[0暂存;1启用;2生效中]
     */
    @ApiModelProperty(value = "版本状态[0暂存;1启用;2生效中]")
    @TableField(value = "status")
    @Excel(name = "版本状态[0暂存;1启用;2生效中]", readConverterExp = "0暂存;1启用;2生效中")
    private String status;

    /**
     * 详细报价信息
     */
    @ApiModelProperty(value = "详细报价信息")
    @Excel(name = "详细报价信息")
    @TableField(exist = false)
    private List<QuoteOpportunitiesRoughDetail> quoteOpportunitiesRoughDetails;


    public QuoteOpportunitiesRough(Long id, Long opportunitiesId, Long opportunitiesParentId, String valuationVersion,
                                   String valuationDesc, BigDecimal amount, String status, List<QuoteOpportunitiesRoughDetail> quoteOpportunitiesRoughDetails) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesParentId = opportunitiesParentId;
        this.valuationVersion = valuationVersion;
        this.valuationDesc = valuationDesc;
        this.amount = amount;
        this.status = status;
        this.quoteOpportunitiesRoughDetails = quoteOpportunitiesRoughDetails;
    }
}