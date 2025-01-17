package com.internal.quote.vo;

import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesCustomizable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报价系统-商机定制开发小记对象 quote_opportunities_customizable
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesCustomizableVO", description = "报价系统-商机定制开发小记")
public class QuoteOpportunitiesCustomizableVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 详细信息
     */
    @ApiModelProperty(value = "详细信息")
    private List<QuoteOpportunitiesCustomizable> quoteOpportunitiesCustomizableList;

    /**
     * 需求评估内部工作量
     */
    @Excel(name = "需求评估内部工作量")
    private BigDecimal internal1 = BigDecimal.ZERO;
    /**
     * 需求评估本地外派工作量
     */
    @Excel(name = "需求评估本地外派工作量")
    private BigDecimal demandLocalWorkload = BigDecimal.ZERO;
    /**
     * 需求评估外地外派工作量
     */
    @Excel(name = "需求评估外地外派工作量")
    private BigDecimal demandExternalWorkload = BigDecimal.ZERO;
    /**
     * 开发评估内部工作量
     */
    @Excel(name = "开发评估内部工作量")
    private BigDecimal internal2 = BigDecimal.ZERO;
    /**
     * 开发评估本地外派工作量
     */
    @Excel(name = "开发评估本地外派工作量")
    private BigDecimal devLocalWorkload = BigDecimal.ZERO;
    /**
     * 开发评估外地外派工作量
     */
    @Excel(name = "开发评估外地外派工作量")
    private BigDecimal devExternalWorkload = BigDecimal.ZERO;
    /**
     * 测试评估内部工作量
     */
    @Excel(name = "测试评估内部工作量")
    private BigDecimal internal3 = BigDecimal.ZERO;
    /**
     * 测试评估本地外派工作量
     */
    @Excel(name = "测试评估本地外派工作量")
    private BigDecimal testLocalWorkload = BigDecimal.ZERO;
    /**
     * 测试评估外地外派工作量
     */
    @Excel(name = "测试评估外地外派工作量")
    private BigDecimal testExternalWorkload = BigDecimal.ZERO;
    /**
     * UI评估内部工作量
     */
    @Excel(name = "UI评估内部工作量")
    private BigDecimal internal4 = BigDecimal.ZERO;
    /**
     * UI评估本地外派工作量
     */
    @Excel(name = "UI评估本地外派工作量")
    private BigDecimal uiLocalWorkload = BigDecimal.ZERO;
    /**
     * UI评估外地外派工作量
     */
    @Excel(name = "UI评估外地外派工作量")
    private BigDecimal uiExternalWorkload = BigDecimal.ZERO;
    /**
     * 项目管理内部工作量
     */
    @Excel(name = "项目管理内部工作量")
    private BigDecimal internal5 = BigDecimal.ZERO;
    /**
     * 项目管理本地外派工作量
     */
    @Excel(name = "项目管理本地外派工作量")
    private BigDecimal pmLocalWorkload = BigDecimal.ZERO;
    /**
     * 项目管理外地外派工作量
     */
    @Excel(name = "项目管理外地外派工作量")
    private BigDecimal pmExternalWorkload = BigDecimal.ZERO;
    /**
     * 软件成本总计（元）
     */
    @Excel(name = "软件成本总计（元）")
    private BigDecimal totalSoftwareCosts = BigDecimal.ZERO;

    public QuoteOpportunitiesCustomizableVO(List<QuoteOpportunitiesCustomizable> quoteOpportunitiesCustomizableList,
                                            BigDecimal internal1, BigDecimal demandLocalWorkload, BigDecimal demandExternalWorkload, BigDecimal internal2,
                                            BigDecimal devLocalWorkload, BigDecimal devExternalWorkload, BigDecimal internal3, BigDecimal testLocalWorkload,
                                            BigDecimal testExternalWorkload, BigDecimal internal4, BigDecimal uiLocalWorkload, BigDecimal uiExternalWorkload,
                                            BigDecimal internal5, BigDecimal pmLocalWorkload, BigDecimal pmExternalWorkload, BigDecimal totalSoftwareCosts) {
        this.quoteOpportunitiesCustomizableList = quoteOpportunitiesCustomizableList;
        this.internal1 = internal1;
        this.demandLocalWorkload = demandLocalWorkload;
        this.demandExternalWorkload = demandExternalWorkload;
        this.internal2 = internal2;
        this.devLocalWorkload = devLocalWorkload;
        this.devExternalWorkload = devExternalWorkload;
        this.internal3 = internal3;
        this.testLocalWorkload = testLocalWorkload;
        this.testExternalWorkload = testExternalWorkload;
        this.internal4 = internal4;
        this.uiLocalWorkload = uiLocalWorkload;
        this.uiExternalWorkload = uiExternalWorkload;
        this.internal5 = internal5;
        this.pmLocalWorkload = pmLocalWorkload;
        this.pmExternalWorkload = pmExternalWorkload;
        this.totalSoftwareCosts = totalSoftwareCosts;
    }
}