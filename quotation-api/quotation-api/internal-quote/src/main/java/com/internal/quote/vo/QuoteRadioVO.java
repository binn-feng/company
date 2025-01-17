package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价系统-报价部门占比对象 quote_radio
 *
 * @author internal
 * @date 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteRadioVO",description = "报价系统-报价部门占比对象VO")
public class QuoteRadioVO extends BaseEntity
{
    private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    private Long id;

    /** 商机售前支持ID */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;

    /** 成本报价id */
    @ApiModelProperty(value = "成本报价id")
    @Excel(name = "成本报价id")
    private Long costId;

    /** 部门ID */
    @ApiModelProperty(value = "部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /** 占比 */
    @ApiModelProperty(value = "占比")
    @Excel(name = "占比")
    private BigDecimal radio;

    /** 对应CRM售前支持ID */
    @ApiModelProperty(value = "对应CRM售前支持ID")
    @Excel(name = "对应CRM售前支持ID")
    private String supportCrmId;

    /**
     * 售前支持人员ID
     */
    @ApiModelProperty(value = "售前支持人员ID")
    @Excel(name = "售前支持人员ID")
    private String supportPerson;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String userNames;
}