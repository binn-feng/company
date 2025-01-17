package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报价系统-商机产品平台小记对象 quote_opportunities_product
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesProductVO", description = "报价系统-商机产品平台小记")
public class QuoteOpportunitiesProductVO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 详情列表
     */
    @ApiModelProperty(value = "详情列表")
    private List<QuoteOpportunitiesProduct> quoteOpportunitiesProductList;

    /**
     * 总价
     */
    @ApiModelProperty(value = "总价")
    @Excel(name = "总价")
    private BigDecimal totalCost;


    public QuoteOpportunitiesProductVO(List<QuoteOpportunitiesProduct> quoteOpportunitiesProductList, BigDecimal totalCost) {
        this.quoteOpportunitiesProductList = quoteOpportunitiesProductList;
        this.totalCost = totalCost;
    }
}