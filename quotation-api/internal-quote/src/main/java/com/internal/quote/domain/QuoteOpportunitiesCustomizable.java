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
 * 报价系统-商机定制开发小记对象 quote_opportunities_customizable
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesCustomizable", description = "报价系统-商机定制开发小记对象")
@TableName("quote_opportunities_customizable")
public class QuoteOpportunitiesCustomizable extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
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
     * 商机详情ID
     */
    @ApiModelProperty(value = "商机详情ID")
    @TableField(value = "opportunities_detail_id")
    @Excel(name = "商机详情ID")
    private Long opportunitiesDetailId;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    @TableField(value = "module_name")
    @Excel(name = "模块名称")
    private String moduleName;

    /**
     * 子模块功能
     */
    @ApiModelProperty(value = "子模块功能")
    @TableField(value = "sub_module_name")
    @Excel(name = "子模块功能")
    private String subModuleName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    @Excel(name = "描述")
    private String description;

    /**
     * 需求评估-公司内部-工作量
     */
    @ApiModelProperty(value = "需求评估-公司内部-工作量")
    @TableField(value = "demand_internal_workload")
    @Excel(name = "需求评估-公司内部-工作量")
    private BigDecimal demandInternalWorkload;

    /**
     * 需求评估-公司内部-单位
     */
    @ApiModelProperty(value = "需求评估-公司内部-单位")
    @TableField(value = "demand_internal_unit")
    @Excel(name = "需求评估-公司内部-单位")
    private String demandInternalUnit;

    /**
     * 需求评估-外派工作量-本地
     */
    @ApiModelProperty(value = "需求评估-外派工作量-本地")
    @TableField(value = "demand_local_workload")
    @Excel(name = "需求评估-外派工作量-本地")
    private BigDecimal demandLocalWorkload;

    /**
     * 需求评估-外派工作量-外地
     */
    @ApiModelProperty(value = "需求评估-外派工作量-外地")
    @TableField(value = "demand_external_workload")
    @Excel(name = "需求评估-外派工作量-外地")
    private BigDecimal demandExternalWorkload;

    /**
     * 需求评估-外派工作量-单位
     */
    @ApiModelProperty(value = "需求评估-外派工作量-单位")
    @TableField(value = "demand_external_unit")
    @Excel(name = "需求评估-外派工作量-单位")
    private String demandExternalUnit;

    /**
     * 开发评估-公司内部-工作量
     */
    @ApiModelProperty(value = "开发评估-公司内部-工作量")
    @TableField(value = "dev_internal_workload")
    @Excel(name = "开发评估-公司内部-工作量")
    private BigDecimal devInternalWorkload;

    /**
     * 开发评估-公司内部-单位
     */
    @ApiModelProperty(value = "开发评估-公司内部-单位")
    @TableField(value = "dev_internal_unit")
    @Excel(name = "开发评估-公司内部-单位")
    private String devInternalUnit;

    /**
     * 开发评估-外派工作量-本地
     */
    @ApiModelProperty(value = "开发评估-外派工作量-本地")
    @TableField(value = "dev_local_workload")
    @Excel(name = "开发评估-外派工作量-本地")
    private BigDecimal devLocalWorkload;

    /**
     * 开发评估-外派工作量-外地
     */
    @ApiModelProperty(value = "开发评估-外派工作量-外地")
    @TableField(value = "dev_external_workload")
    @Excel(name = "开发评估-外派工作量-外地")
    private BigDecimal devExternalWorkload;

    /**
     * 开发评估-外派工作量-单位
     */
    @ApiModelProperty(value = "开发评估-外派工作量-单位")
    @TableField(value = "dev_external_unit")
    @Excel(name = "开发评估-外派工作量-单位")
    private String devExternalUnit;

    /**
     * 测试评估-公司内部-工作量
     */
    @ApiModelProperty(value = "测试评估-公司内部-工作量")
    @TableField(value = "test_internal_workload")
    @Excel(name = "测试评估-公司内部-工作量")
    private BigDecimal testInternalWorkload;

    /**
     * 测试评估-公司内部-单位
     */
    @ApiModelProperty(value = "测试评估-公司内部-单位")
    @TableField(value = "test_internal_unit")
    @Excel(name = "测试评估-公司内部-单位")
    private String testInternalUnit;

    /**
     * 测试评估-外派工作量-本地
     */
    @ApiModelProperty(value = "测试评估-外派工作量-本地")
    @TableField(value = "test_local_workload")
    @Excel(name = "测试评估-外派工作量-本地")
    private BigDecimal testLocalWorkload;

    /**
     * 测试评估-外派工作量-外地
     */
    @ApiModelProperty(value = "测试评估-外派工作量-外地")
    @TableField(value = "test_external_workload")
    @Excel(name = "测试评估-外派工作量-外地")
    private BigDecimal testExternalWorkload;

    /**
     * 测试评估-外派工作量-单位
     */
    @ApiModelProperty(value = "测试评估-外派工作量-单位")
    @TableField(value = "test_external_unit")
    @Excel(name = "测试评估-外派工作量-单位")
    private String testExternalUnit;

    /**
     * UI评估-公司内部-工作量
     */
    @ApiModelProperty(value = "UI评估-公司内部-工作量")
    @TableField(value = "ui_internal_workload")
    @Excel(name = "UI评估-公司内部-工作量")
    private BigDecimal uiInternalWorkload;

    /**
     * UI评估-公司内部-单位
     */
    @ApiModelProperty(value = "UI评估-公司内部-单位")
    @TableField(value = "ui_internal_unit")
    @Excel(name = "UI评估-公司内部-单位")
    private String uiInternalUnit;

    /**
     * UI评估-外派工作量-本地
     */
    @ApiModelProperty(value = "UI评估-外派工作量-本地")
    @TableField(value = "ui_local_workload")
    @Excel(name = "UI评估-外派工作量-本地")
    private BigDecimal uiLocalWorkload;

    /**
     * UI评估-外派工作量-外地
     */
    @ApiModelProperty(value = "UI评估-外派工作量-外地")
    @TableField(value = "ui_external_workload")
    @Excel(name = "UI评估-外派工作量-外地")
    private BigDecimal uiExternalWorkload;

    /**
     * UI评估-外派工作量-单位
     */
    @ApiModelProperty(value = "UI评估-外派工作量-单位")
    @TableField(value = "ui_external_unit")
    @Excel(name = "UI评估-外派工作量-单位")
    private String uiExternalUnit;

    /**
     * 项目管理-公司内部-工作量
     */
    @ApiModelProperty(value = "项目管理-公司内部-工作量")
    @TableField(value = "pm_internal_workload")
    @Excel(name = "项目管理-公司内部-工作量")
    private BigDecimal pmInternalWorkload;

    /**
     * 项目管理-公司内部-单位
     */
    @ApiModelProperty(value = "项目管理-公司内部-单位")
    @TableField(value = "pm_internal_unit")
    @Excel(name = "项目管理-公司内部-单位")
    private String pmInternalUnit;

    /**
     * 项目管理-外派工作量-本地
     */
    @ApiModelProperty(value = "项目管理-外派工作量-本地")
    @TableField(value = "pm_local_workload")
    @Excel(name = "项目管理-外派工作量-本地")
    private BigDecimal pmLocalWorkload;

    /**
     * 项目管理-外派工作量-外地
     */
    @ApiModelProperty(value = "项目管理-外派工作量-外地")
    @TableField(value = "pm_external_workload")
    @Excel(name = "项目管理-外派工作量-外地")
    private BigDecimal pmExternalWorkload;

    /**
     * 项目管理-外派工作量-单位
     */
    @ApiModelProperty(value = "项目管理-外派工作量-单位")
    @TableField(value = "pm_external_unit")
    @Excel(name = "项目管理-外派工作量-单位")
    private String pmExternalUnit;


    public QuoteOpportunitiesCustomizable(Long id, Long opportunitiesId, Long opportunitiesDetailId, String moduleName, String subModuleName, String description, BigDecimal demandInternalWorkload, String demandInternalUnit, BigDecimal demandLocalWorkload, BigDecimal demandExternalWorkload, String demandExternalUnit, BigDecimal devInternalWorkload, String devInternalUnit, BigDecimal devLocalWorkload, BigDecimal devExternalWorkload, String devExternalUnit, BigDecimal testInternalWorkload, String testInternalUnit, BigDecimal testLocalWorkload, BigDecimal testExternalWorkload, String testExternalUnit, BigDecimal uiInternalWorkload, String uiInternalUnit, BigDecimal uiLocalWorkload, BigDecimal uiExternalWorkload, String uiExternalUnit, BigDecimal pmInternalWorkload, String pmInternalUnit, BigDecimal pmLocalWorkload, BigDecimal pmExternalWorkload, String pmExternalUnit) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesDetailId = opportunitiesDetailId;
        this.moduleName = moduleName;
        this.subModuleName = subModuleName;
        this.description = description;
        this.demandInternalWorkload = demandInternalWorkload;
        this.demandInternalUnit = demandInternalUnit;
        this.demandLocalWorkload = demandLocalWorkload;
        this.demandExternalWorkload = demandExternalWorkload;
        this.demandExternalUnit = demandExternalUnit;
        this.devInternalWorkload = devInternalWorkload;
        this.devInternalUnit = devInternalUnit;
        this.devLocalWorkload = devLocalWorkload;
        this.devExternalWorkload = devExternalWorkload;
        this.devExternalUnit = devExternalUnit;
        this.testInternalWorkload = testInternalWorkload;
        this.testInternalUnit = testInternalUnit;
        this.testLocalWorkload = testLocalWorkload;
        this.testExternalWorkload = testExternalWorkload;
        this.testExternalUnit = testExternalUnit;
        this.uiInternalWorkload = uiInternalWorkload;
        this.uiInternalUnit = uiInternalUnit;
        this.uiLocalWorkload = uiLocalWorkload;
        this.uiExternalWorkload = uiExternalWorkload;
        this.uiExternalUnit = uiExternalUnit;
        this.pmInternalWorkload = pmInternalWorkload;
        this.pmInternalUnit = pmInternalUnit;
        this.pmLocalWorkload = pmLocalWorkload;
        this.pmExternalWorkload = pmExternalWorkload;
        this.pmExternalUnit = pmExternalUnit;
    }
}