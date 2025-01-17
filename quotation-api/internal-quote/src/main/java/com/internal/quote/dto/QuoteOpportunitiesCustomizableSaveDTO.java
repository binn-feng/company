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
 * 报价系统-商机定制开发小记对象 quote_opportunities_customizable
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesCustomizableSaveDTO", description = "报价系统-商机定制开发小记")
public class QuoteOpportunitiesCustomizableSaveDTO  {
    private static final long serialVersionUID = 1L;


    /**
     * 商机详情ID
     */
    @ApiModelProperty(value = "商机详情ID")
    @Excel(name = "商机详情ID")
    private Long opportunitiesDetailId;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    @NotEmpty(message = "模块名称不能为空")
    @Excel(name = "模块名称")
    private String moduleName;

    /**
     * 子模块功能
     */
    @ApiModelProperty(value = "子模块功能")
    @NotEmpty(message = "子模块功能不能为空")
    @Excel(name = "子模块功能")
    private String subModuleName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @NotEmpty(message = "描述不能为空")
    @Excel(name = "描述")
    private String description;

    /**
     * 需求评估-公司内部-工作量
     */
    @ApiModelProperty(value = "需求评估-公司内部-工作量")
    @NotNull(message = "需求评估-公司内部-工作量不能为空")
    @Excel(name = "需求评估-公司内部-工作量")
    private BigDecimal demandInternalWorkload = BigDecimal.ZERO;

    /**
     * 需求评估-公司内部-单位
     */
    @ApiModelProperty(value = "需求评估-公司内部-单位")
    @NotEmpty(message = "需求评估-公司内部-单位不能为空")
    @Excel(name = "需求评估-公司内部-单位")
    private String demandInternalUnit;

    /**
     * 需求评估-外派工作量-本地
     */
    @ApiModelProperty(value = "需求评估-外派工作量-本地")
    @NotNull(message = "需求评估-外派工作量-本地不能为空")
    @Excel(name = "需求评估-外派工作量-本地")
    private BigDecimal demandLocalWorkload = BigDecimal.ZERO;

    /**
     * 需求评估-外派工作量-外地
     */
    @ApiModelProperty(value = "需求评估-外派工作量-外地")
    @NotNull(message = "需求评估-外派工作量-外地不能为空")
    @Excel(name = "需求评估-外派工作量-外地")
    private BigDecimal demandExternalWorkload = BigDecimal.ZERO;

    /**
     * 需求评估-外派工作量-单位
     */
    @ApiModelProperty(value = "需求评估-外派工作量-单位")
    @NotEmpty(message = "需求评估-外派工作量-单位不能为空")
    @Excel(name = "需求评估-外派工作量-单位")
    private String demandExternalUnit;

    /**
     * 开发评估-公司内部-工作量
     */
    @ApiModelProperty(value = "开发评估-公司内部-工作量")
    @NotNull(message = "开发评估-公司内部-工作量不能为空")
    @Excel(name = "开发评估-公司内部-工作量")
    private BigDecimal devInternalWorkload = BigDecimal.ZERO;

    /**
     * 开发评估-公司内部-单位
     */
    @ApiModelProperty(value = "开发评估-公司内部-单位")
    @NotEmpty(message = "开发评估-公司内部-单位不能为空")
    @Excel(name = "开发评估-公司内部-单位")
    private String devInternalUnit;

    /**
     * 开发评估-外派工作量-本地
     */
    @ApiModelProperty(value = "开发评估-外派工作量-本地")
    @NotNull(message = "开发评估-外派工作量-本地不能为空")
    @Excel(name = "开发评估-外派工作量-本地")
    private BigDecimal devLocalWorkload = BigDecimal.ZERO;

    /**
     * 开发评估-外派工作量-外地
     */
    @ApiModelProperty(value = "开发评估-外派工作量-外地")
    @NotNull(message = "开发评估-外派工作量-外地不能为空")
    @Excel(name = "开发评估-外派工作量-外地")
    private BigDecimal devExternalWorkload = BigDecimal.ZERO;

    /**
     * 开发评估-外派工作量-单位
     */
    @ApiModelProperty(value = "开发评估-外派工作量-单位")
    @NotEmpty(message = "开发评估-外派工作量-单位不能为空")
    @Excel(name = "开发评估-外派工作量-单位")
    private String devExternalUnit;

    /**
     * 测试评估-公司内部-工作量
     */
    @ApiModelProperty(value = "测试评估-公司内部-工作量")
    @NotNull(message = "测试评估-公司内部-工作量不能为空")
    @Excel(name = "测试评估-公司内部-工作量")
    private BigDecimal testInternalWorkload = BigDecimal.ZERO;

    /**
     * 测试评估-公司内部-单位
     */
    @ApiModelProperty(value = "测试评估-公司内部-单位")
    @NotEmpty(message = "测试评估-公司内部-单位不能为")
    @Excel(name = "测试评估-公司内部-单位")
    private String testInternalUnit;

    /**
     * 测试评估-外派工作量-本地
     */
    @ApiModelProperty(value = "测试评估-外派工作量-本地")
    @NotNull(message = "测试评估-外派工作量-本地不能为空")
    @Excel(name = "测试评估-外派工作量-本地")
    private BigDecimal testLocalWorkload = BigDecimal.ZERO;

    /**
     * 测试评估-外派工作量-外地
     */
    @ApiModelProperty(value = "测试评估-外派工作量-外地")
    @NotNull(message = "测试评估-外派工作量-外地不能为空")
    @Excel(name = "测试评估-外派工作量-外地")
    private BigDecimal testExternalWorkload = BigDecimal.ZERO;

    /**
     * 测试评估-外派工作量-单位
     */
    @ApiModelProperty(value = "测试评估-外派工作量-单位")
    @NotEmpty(message = "测试评估-外派工作量-单位不能为空")
    @Excel(name = "测试评估-外派工作量-单位")
    private String testExternalUnit;

    /**
     * UI评估-公司内部-工作量
     */
    @ApiModelProperty(value = "UI评估-公司内部-工作量")
    @NotNull(message = "UI评估-公司内部-工作量不能为空")
    @Excel(name = "UI评估-公司内部-工作量")
    private BigDecimal uiInternalWorkload = BigDecimal.ZERO;

    /**
     * UI评估-公司内部-单位
     */
    @ApiModelProperty(value = "UI评估-公司内部-单位")
    @NotEmpty(message = "UI评估-公司内部-单位不能为空")
    @Excel(name = "UI评估-公司内部-单位")
    private String uiInternalUnit;

    /**
     * UI评估-外派工作量-本地
     */
    @ApiModelProperty(value = "UI评估-外派工作量-本地")
    @NotNull(message = "UI评估-外派工作量-本地不能为空")
    @Excel(name = "UI评估-外派工作量-本地")
    private BigDecimal uiLocalWorkload = BigDecimal.ZERO;

    /**
     * UI评估-外派工作量-外地
     */
    @ApiModelProperty(value = "UI评估-外派工作量-外地")
    @NotNull(message = "UI评估-外派工作量-外地不能为空")
    @Excel(name = "UI评估-外派工作量-外地")
    private BigDecimal uiExternalWorkload = BigDecimal.ZERO;

    /**
     * UI评估-外派工作量-单位
     */
    @ApiModelProperty(value = "UI评估-外派工作量-单位")
    @NotEmpty(message = "UI评估-外派工作量-单位不能为空")
    @Excel(name = "UI评估-外派工作量-单位")
    private String uiExternalUnit;

    /**
     * 项目管理-公司内部-工作量
     */
    @ApiModelProperty(value = "项目管理-公司内部-工作量")
    @NotNull(message = "项目管理-公司内部-工作量不能为空")
    @Excel(name = "项目管理-公司内部-工作量")
    private BigDecimal pmInternalWorkload = BigDecimal.ZERO;

    /**
     * 项目管理-公司内部-单位
     */
    @ApiModelProperty(value = "项目管理-公司内部-单位")
    @NotEmpty(message = "项目管理")
    @Excel(name = "项目管理-公司内部-单位")
    private String pmInternalUnit;

    /**
     * 项目管理-外派工作量-本地
     */
    @ApiModelProperty(value = "项目管理-外派工作量-本地")
    @NotNull(message = "项目管理-外派工作量-本地不能为空")
    @Excel(name = "项目管理-外派工作量-本地")
    private BigDecimal pmLocalWorkload = BigDecimal.ZERO;

    /**
     * 项目管理-外派工作量-外地
     */
    @ApiModelProperty(value = "项目管理-外派工作量-外地")
    @NotNull(message = "UI评估-外派工作量-外地不能为空")
    @Excel(name = "项目管理-外派工作量-外地")
    private BigDecimal pmExternalWorkload = BigDecimal.ZERO;

    /**
     * 项目管理-外派工作量-单位
     */
    @ApiModelProperty(value = "项目管理-外派工作量-单位")
    @NotEmpty(message = "UI评估-外派工作量-单位不能为空")
    @Excel(name = "项目管理-外派工作量-单位")
    private String pmExternalUnit;


    public QuoteOpportunitiesCustomizableSaveDTO(Long opportunitiesDetailId, String moduleName, String subModuleName,
                                                 String description, BigDecimal demandInternalWorkload, String demandInternalUnit,
                                                 BigDecimal demandLocalWorkload, BigDecimal demandExternalWorkload, String demandExternalUnit,
                                                 BigDecimal devInternalWorkload, String devInternalUnit, BigDecimal devLocalWorkload, BigDecimal devExternalWorkload, String devExternalUnit, BigDecimal testInternalWorkload, String testInternalUnit,
                                                 BigDecimal testLocalWorkload, BigDecimal testExternalWorkload, String testExternalUnit, BigDecimal uiInternalWorkload,
                                                 String uiInternalUnit, BigDecimal uiLocalWorkload, BigDecimal uiExternalWorkload, String uiExternalUnit,
                                                 BigDecimal pmInternalWorkload, String pmInternalUnit, BigDecimal pmLocalWorkload, BigDecimal pmExternalWorkload, String pmExternalUnit) {
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