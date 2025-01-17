package com.internal.quote.dto;

import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 只有ID等基础字段的基础DTO
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "BaseDTO", description = "只有ID等基础字段的基础DTO")
public class BaseDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 报价id
     */
    @ApiModelProperty(value = "报价id")
    @Excel(name = "报价id")
    private Long opportunitiesId;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesParentId;

    /**
     * 对应CRM售前支持ID
     */
    @ApiModelProperty(value = "对应CRM售前支持ID")
    @Excel(name = "对应CRM售前支持ID")
    private String supportCrmId;

    /**
     * 成本报价Id(只有详细用到了)
     */
    @ApiModelProperty(value = "成本报价Id(粗略或详细)")
    private Long costId = null;


}