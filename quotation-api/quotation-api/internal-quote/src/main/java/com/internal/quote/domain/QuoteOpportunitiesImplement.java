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
 * 报价系统-商机实施小记对象 quote_opportunities_implement
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesImplement", description = "报价系统-商机实施小记对象")
@TableName("quote_opportunities_implement")
public class QuoteOpportunitiesImplement extends BaseEntity {
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
     * 本地标准成本
     */
    @ApiModelProperty(value = "本地标准成本")
    @TableField(value = "local_cost")
    @Excel(name = "本地标准成本")
    private BigDecimal localCost;

    /**
     * 本地人天
     */
    @ApiModelProperty(value = "本地人天")
    @TableField(value = "local_day")
    @Excel(name = "本地人天")
    private BigDecimal localDay;

    /**
     * 本地小计（元）
     */
    @ApiModelProperty(value = "本地小计（元）")
    @TableField(value = "local_subtotal")
    @Excel(name = "本地小计", readConverterExp = "元=")
    private BigDecimal localSubtotal;

    /**
     * 外地标准成本
     */
    @ApiModelProperty(value = "外地标准成本")
    @TableField(value = "remote_cost")
    @Excel(name = "外地标准成本")
    private BigDecimal remoteCost;

    /**
     * 外地人天
     */
    @ApiModelProperty(value = "外地人天")
    @TableField(value = "remote_day")
    @Excel(name = "外地人天")
    private BigDecimal remoteDay;

    /**
     * 外地小计（元）
     */
    @ApiModelProperty(value = "外地小计（元）")
    @TableField(value = "remote_subtotal")
    @Excel(name = "外地小计", readConverterExp = "元=")
    private BigDecimal remoteSubtotal;

    /**
     * 人天收费合计(元)
     */
    @ApiModelProperty(value = "人天收费合计(元)")
    @TableField(value = "person_day_amount")
    @Excel(name = "人天收费合计(元)")
    private BigDecimal personDayAmount;

    /**
     * 区间
     */
    @ApiModelProperty(value = "区间")
    @TableField(value = "interval_range")
    @Excel(name = "区间")
    private String intervalRange;

    /**
     * 区间内次数
     */
    @ApiModelProperty(value = "区间内次数")
    @TableField(value = "occurrences")
    @Excel(name = "区间内次数")
    private Long occurrences;

    /**
     * 打包价格(元)
     */
    @ApiModelProperty(value = "打包价格(元)")
    @TableField(value = "package_price")
    @Excel(name = "打包价格(元)")
    private BigDecimal packagePrice;

    /**
     * 实施成本合计（元）
     */
    @ApiModelProperty(value = "实施成本合计（元）")
    @TableField(value = "total_cost")
    @Excel(name = "实施成本合计", readConverterExp = "元=")
    private BigDecimal totalCost;

    public QuoteOpportunitiesImplement(Long id, Long opportunitiesId, Long opportunitiesDetailId, BigDecimal localCost, BigDecimal localDay, BigDecimal localSubtotal, BigDecimal remoteCost, BigDecimal remoteDay, BigDecimal remoteSubtotal, BigDecimal personDayAmount, String intervalRange, Long occurrences, BigDecimal packagePrice, BigDecimal totalCost) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesDetailId = opportunitiesDetailId;
        this.localCost = localCost;
        this.localDay = localDay;
        this.localSubtotal = localSubtotal;
        this.remoteCost = remoteCost;
        this.remoteDay = remoteDay;
        this.remoteSubtotal = remoteSubtotal;
        this.personDayAmount = personDayAmount;
        this.intervalRange = intervalRange;
        this.occurrences = occurrences;
        this.packagePrice = packagePrice;
        this.totalCost = totalCost;
    }
}