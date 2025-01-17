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
 * 报价系统-商机详细报价对象 quote_opportunities_detail
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesDetail", description = "报价系统-商机详细报价对象")
@TableName("quote_opportunities_detail")
public class QuoteOpportunitiesDetail extends BaseEntity {
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
     * 评估说明
     */
    @ApiModelProperty(value = "评估说明")
    @TableField(value = "valuation_desc")
    @Excel(name = "评估说明")
    private String valuationDesc;

    /**
     * 评估金额
     */
    @ApiModelProperty(value = "评估金额")
    @TableField(value = "amount")
    @Excel(name = "评估金额")
    private BigDecimal amount;

    /**
     * 评估人
     */
    @ApiModelProperty(value = "评估人")
    @TableField(exist = false)
    @Excel(name = "评估人")
    private String evaluator;

    /**
     * 版本状态[0暂存;1启用;2生效中]
     */
    @ApiModelProperty(value = "版本状态[0暂存;1启用;2生效中]")
    @TableField(value = "status")
    @Excel(name = "版本状态", readConverterExp = "0=暂存,1=启用")
    private String status;

    /**
     * 售前成本备注
     */
    @ApiModelProperty(value = "售前成本备注")
    @TableField(value = "support_remark")
    @Excel(name = "售前成本备注")
    private String supportRemark;

}