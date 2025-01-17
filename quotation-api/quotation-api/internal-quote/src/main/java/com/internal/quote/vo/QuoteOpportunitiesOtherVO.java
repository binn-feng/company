package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesOther;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
@ApiModel(value = "QuoteOpportunitiesOtherVO", description = "报价系统-商机其他小记对象")
public class QuoteOpportunitiesOtherVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商机其他信息
     */
    @ApiModelProperty(value = "商机其他信息")
    private List<QuoteOpportunitiesOther> quoteOpportunitiesOtherList;

    /**
     * 总价
     */
    @ApiModelProperty(value = "总价")
    @TableField(value = "amount")
    @Excel(name = "总价")
    private BigDecimal totalCost;

    public QuoteOpportunitiesOtherVO(List<QuoteOpportunitiesOther> quoteOpportunitiesOtherList, BigDecimal totalCost) {
        this.quoteOpportunitiesOtherList = quoteOpportunitiesOtherList;
        this.totalCost = totalCost;
    }
}