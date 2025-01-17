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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

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
@ApiModel(value = "QuoteOpportunitiesImplementSaveDTO", description = "报价系统-商机实施小记对象")
public class QuoteOpportunitiesImplementSaveDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesDetailId;

    /**
     * 本地标准成本
     */
    @ApiModelProperty(value = "本地标准成本")
    @NotNull(message = "本地标准成本不能为空")
    @Excel(name = "本地标准成本")
    private BigDecimal localCost = BigDecimal.ZERO;

    /**
     * 本地人天
     */
    @ApiModelProperty(value = "本地人天")
    @NotNull(message = "本地人天不能为空")
    @Excel(name = "本地人天")
    private BigDecimal localDay = BigDecimal.ZERO;

    /**
     * 本地小计（元）
     */
    @ApiModelProperty(value = "本地小计（元）")
    @NotNull(message = "本地小计不能为空")
    @Excel(name = "本地小计", readConverterExp = "元=")
    private BigDecimal localSubtotal = BigDecimal.ZERO;

    /**
     * 外地标准成本
     */
    @ApiModelProperty(value = "外地标准成本")
    @NotNull(message = "外地标准成本不能为空")
    @Excel(name = "外地标准成本")
    private BigDecimal remoteCost = BigDecimal.ZERO;

    /**
     * 外地人天
     */
    @ApiModelProperty(value = "外地人天")
    @NotNull(message = "外地人天不能为空")
    @Excel(name = "外地人天")
    private BigDecimal remoteDay = BigDecimal.ZERO;

    /**
     * 外地小计（元）
     */
    @ApiModelProperty(value = "外地小计（元）")
    @NotNull(message = "外地小计不能为空")
    @Excel(name = "外地小计", readConverterExp = "元=")
    private BigDecimal remoteSubtotal = BigDecimal.ZERO;

    /**
     * 人天收费合计(元)
     */
    @ApiModelProperty(value = "人天收费合计(元)")
    @TableField(value = "person_day_amount")
    @Excel(name = "人天收费合计(元)")
    private BigDecimal personDayAmount = BigDecimal.ZERO;

    /**
     * 区间
     */
    @ApiModelProperty(value = "区间")
    @NotEmpty(message = "区间不能为空")
    @Excel(name = "区间")
    private String range;

    /**
     * 区间内次数
     */
    @ApiModelProperty(value = "区间内次数")
    @NotNull(message = "区间内次数不能为空")
    @Excel(name = "区间内次数")
    private Long occurrences;

    /**
     * 打包价格(元)
     */
    @ApiModelProperty(value = "打包价格(元)")
    @NotNull(message = "打包价格不能为空")
    @Excel(name = "打包价格(元)")
    private BigDecimal packagePrice;

    /**
     * 实施成本合计（元）
     */
    @ApiModelProperty(value = "实施成本合计（元）")
    @NotNull(message = "实施成本合计不能为空")
    @Excel(name = "实施成本合计", readConverterExp = "元=")
    private BigDecimal totalCost;


    public QuoteOpportunitiesImplementSaveDTO(Long opportunitiesDetailId, BigDecimal localCost, BigDecimal localDay, BigDecimal localSubtotal,
                                              BigDecimal remoteCost, BigDecimal remoteDay, BigDecimal remoteSubtotal, BigDecimal personDayAmount, String range, Long occurrences, BigDecimal packagePrice, BigDecimal totalCost) {
        this.opportunitiesDetailId = opportunitiesDetailId;
        this.localCost = localCost;
        this.localDay = localDay;
        this.localSubtotal = localSubtotal;
        this.remoteCost = remoteCost;
        this.remoteDay = remoteDay;
        this.remoteSubtotal = remoteSubtotal;
        this.personDayAmount = personDayAmount;
        this.range = range;
        this.occurrences = occurrences;
        this.packagePrice = packagePrice;
        this.totalCost = totalCost;
    }
}