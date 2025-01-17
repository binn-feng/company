package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesSelf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报价系统-商机自研硬件小记对象 quote_opportunities_self
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesSelfVO", description = "报价系统-商机自研硬件小记")
public class QuoteOpportunitiesSelfVO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 详细信息
     */
    @ApiModelProperty(value = "详细信息")
    private List<QuoteOpportunitiesSelf> quoteOpportunitiesSelfList;

    /**
     * 总价
     */
    @ApiModelProperty(value = "总价")
    @TableField(value = "amount")
    @Excel(name = "总价")
    private BigDecimal totalCost;

    public QuoteOpportunitiesSelfVO(List<QuoteOpportunitiesSelf> quoteOpportunitiesSelfList, BigDecimal totalCost) {
        this.quoteOpportunitiesSelfList = quoteOpportunitiesSelfList;
        this.totalCost = totalCost;
    }
}