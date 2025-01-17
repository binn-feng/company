package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售报价版本
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "OpportunitiesSalesVersionVO", description = "销售报价版本")
public class OpportunitiesSalesVersionVO {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    private Long opportunitiesId;

    /**
     * 售前报价版本
     */
    @ApiModelProperty(value = "销售报价版本")
    private String preSaleVersion;

    /**
     * 报价金额(不包含外采硬件)
     */
    @ApiModelProperty(value = "报价金额(不包含外采硬件)")
    private BigDecimal amountExcl;
    /**
     * 报价金额(包含外采硬件)
     */
    @ApiModelProperty(value = "报价金额(包含外采硬件)")
    private BigDecimal amountIncl;

    /**
     * 报价日期
     */
    @ApiModelProperty(value = "报价日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 项目利润率(不包含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(不包含外采硬件)")
    private BigDecimal projProfitRateExcl = BigDecimal.ZERO;

    /**
     * 项目利润率(含外采硬件)
     */
    @ApiModelProperty(value = "项目利润率(含外采硬件)")
    private BigDecimal projProfitRateIncl = BigDecimal.ZERO;

    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private String type;

}