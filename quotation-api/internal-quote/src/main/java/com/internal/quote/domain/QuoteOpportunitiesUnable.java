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

/**
 * 报价系统-无法报价对象 quote_opportunities_unable
 *
 * @author internal
 * @date 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesUnable",description = "报价系统-无法报价对象")
@TableName("quote_opportunities_unable")
public class QuoteOpportunitiesUnable extends BaseEntity
        {
private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 商机售前支持ID */
    @ApiModelProperty(value = "商机售前支持ID")
    @TableField(value = "opportunities_id")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;

    /** 版本 */
    @ApiModelProperty(value = "版本")
    @TableField(value = "valuation_version")
    @Excel(name = "版本")
    private String valuationVersion;

    /** 报价说明 */
    @ApiModelProperty(value = "报价说明")
    @TableField(value = "valuation_desc")
    @Excel(name = "报价说明")
    private String valuationDesc;

    /** 版本状态[0暂存;1未生效;2生效中] */
    @ApiModelProperty(value = "版本状态[0暂存;1未生效;2生效中]")
    @TableField(value = "status")
    @Excel(name = "版本状态[0暂存;1未生效;2生效中]")
    private String status;


}