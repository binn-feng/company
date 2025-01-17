package com.internal.quote.vo;

import com.internal.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 报价系统-主表工具VO(用于group by)
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "OpportunitiesGroupVO", description = "报价系统-主表工具VO(用于group by)")
public class OpportunitiesGroupVO {
    private static final long serialVersionUID = 1L;

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
     * 售前数量
     */
    @ApiModelProperty(value = "售前数量")
    @Excel(name = "售前数量")
    private Integer supportCount;
}