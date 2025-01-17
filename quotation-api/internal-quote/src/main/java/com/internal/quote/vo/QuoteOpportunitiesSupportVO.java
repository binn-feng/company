package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
@ApiModel(value = "QuoteOpportunitiesSupportVO", description = "报价系统-商机售前支持对象")
public class QuoteOpportunitiesSupportVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 详细信息
     */
    @ApiModelProperty(value = "详细信息")
    private List<QuoteOpportunitiesSupport> quoteOpportunitiesSupportList;

    /**
     * 售前支持工时累计(人月)
     */
    @ApiModelProperty(value = "售前支持工时累计(人月)")
    private BigDecimal totalWorkHours;

    @ApiModelProperty(value = "公司内部（人月）")
    private BigDecimal totalInternal;

    @ApiModelProperty(value = "本地外派（人月）")
    private BigDecimal totalLocal;

    @ApiModelProperty(value = "外地外派（人月）")
    private BigDecimal totalExternal;

    /**
     * 售前支持成本总计
     */
    @ApiModelProperty(value = "售前支持成本总计")
    private BigDecimal totalCost;

    /**
     * 公司内部（按照25000元/人月计算）
     */
    @ApiModelProperty(value = "公司内部（按照25000元/人月计算）	")
    private BigDecimal withinCost;

    /**
     * 本地外派成本（按照1500元/人天计算）
     */
    @ApiModelProperty(value = "本地外派成本（按照1500元/人天计算）")
    private BigDecimal localCost;

    /**
     * 外地外派成本（按照2500元/人天计算）
     */
    @ApiModelProperty(value = "外地外派成本（按照2500元/人天计算）")
    private BigDecimal remoteCost;



    public QuoteOpportunitiesSupportVO(List<QuoteOpportunitiesSupport> quoteOpportunitiesSupportList, BigDecimal totalWorkHours,
                                       BigDecimal totalInternal, BigDecimal totalLocal, BigDecimal totalExternal, BigDecimal totalCost,
                                       BigDecimal withinCost, BigDecimal localCost, BigDecimal remoteCost) {
        this.quoteOpportunitiesSupportList = quoteOpportunitiesSupportList;
        this.totalWorkHours = totalWorkHours;
        this.totalInternal = totalInternal;
        this.totalLocal = totalLocal;
        this.totalExternal = totalExternal;
        this.totalCost = totalCost;
        this.withinCost = withinCost;
        this.localCost = localCost;
        this.remoteCost = remoteCost;
    }
}