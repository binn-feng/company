package com.internal.quote.dto;

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
 * 审批用类
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "ApprovalDTO", description = "审批用类")
public class ApprovalDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesParentId;

    /**
     * 审批理由
     */
    @ApiModelProperty(value = "审批理由")
    private String auditDesc;

    /**
     * 北光报价总金额
     */
    @ApiModelProperty(value = "北光报价总金额")
    @Excel(name = "北光报价总金额")
    private BigDecimal northAmount;

    /**
     * 预计总成本
     */
    @ApiModelProperty(value = "预计总成本")
    @Excel(name = "预计总成本")
    private BigDecimal totalCost;

    /**
     * 成本利润率
     */
    @ApiModelProperty(value = "成本利润率")
    @Excel(name = "成本利润率")
    private BigDecimal costProfitRate;

    public ApprovalDTO(Long id, Long opportunitiesId, Long opportunitiesParentId, String auditDesc, BigDecimal northAmount, BigDecimal totalCost, BigDecimal costProfitRate) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesParentId = opportunitiesParentId;
        this.auditDesc = auditDesc;
        this.northAmount = northAmount;
        this.totalCost = totalCost;
        this.costProfitRate = costProfitRate;
    }
}