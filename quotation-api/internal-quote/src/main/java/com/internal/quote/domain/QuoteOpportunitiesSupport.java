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

/**
 * 报价系统-商机售前支持详情对象 quote_opportunities_support_detail
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesSupport", description = "报价系统-商机售前支持对象")
@TableName("quote_opportunities_support")
public class QuoteOpportunitiesSupport extends BaseEntity {
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
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @TableField(value = "opportunities_detail_id")
    @Excel(name = "商机ID")
    private Long opportunitiesDetailId;

    /**
     * 售前支持类型;[已投入:INVESTED,预计后期投入:LATE_INPUTS]
     */
    @ApiModelProperty(value = "售前支持类型;[已投入:INVESTED,预计后期投入:LATE_INPUTS] ")
    @TableField(value = "type")
    @Excel(name = "售前支持类型;[已投入:INVESTED,预计后期投入:LATE_INPUTS] ")
    private String type;

    /**
     * 已投入内容（人月）
     */
    @ApiModelProperty(value = "内容（人月）")
    @TableField(value = "content")
    @Excel(name = "内容", readConverterExp = "人=月")
    private String content;

    /**
     * 已投入公司内部
     */
    @ApiModelProperty(value = "公司内部")
    @TableField(value = "within")
    @Excel(name = "公司内部")
    private BigDecimal within;

    /**
     * 单位类型[人天:HUMAN_NATURE,人月:HUMAN_MONTH]
     */
    @ApiModelProperty(value = "单位类型[人天:HUMAN_NATURE,人月:HUMAN_MONTH]")
    @TableField(value = "unit")
    @Excel(name = "单位")
    private String unit;

    /**
     * 已投入本地外派
     */
    @ApiModelProperty(value = "本地外派")
    @TableField(value = "local")
    @Excel(name = "本地外派")
    private BigDecimal local;

    /**
     * 已投入外地外派
     */
    @ApiModelProperty(value = "外地外派")
    @TableField(value = "remote")
    @Excel(name = "外地外派")
    private BigDecimal remote;

    public QuoteOpportunitiesSupport(Long id, Long opportunitiesId, Long opportunitiesDetailId, String type, String content, BigDecimal within, String unit, BigDecimal local, BigDecimal remote) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesDetailId = opportunitiesDetailId;
        this.type = type;
        this.content = content;
        this.within = within;
        this.unit = unit;
        this.local = local;
        this.remote = remote;
    }
}