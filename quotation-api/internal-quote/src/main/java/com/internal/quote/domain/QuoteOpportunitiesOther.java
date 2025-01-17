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
 * 报价系统-商机其他小记对象 quote_opportunities_other
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesOther", description = "报价系统-商机其他小记对象")
@TableName("quote_opportunities_other")
public class QuoteOpportunitiesOther extends BaseEntity {
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
     * 序号
     */
    @ApiModelProperty(value = "序号")
    @TableField(value = "serial_number")
    @Excel(name = "序号")
    private String serialNumber;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    @Excel(name = "名称")
    private String name;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @TableField(value = "unit")
    @Excel(name = "单位")
    private String unit;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @TableField(value = "unit_price")
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @TableField(value = "number")
    @Excel(name = "数量")
    private BigDecimal number;

    /**
     * 总价
     */
    @ApiModelProperty(value = "总价")
    @TableField(value = "amount")
    @Excel(name = "总价")
    private BigDecimal amount;


    public QuoteOpportunitiesOther(Long id, Long opportunitiesId, Long opportunitiesDetailId, String serialNumber, String name, String unit, BigDecimal unitPrice, BigDecimal number, BigDecimal amount) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesDetailId = opportunitiesDetailId;
        this.serialNumber = serialNumber;
        this.name = name;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.number = number;
        this.amount = amount;
    }
}