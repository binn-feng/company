package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 签约申请版本
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "OpportunitiesSignVersionVO", description = "签约申请版本")
public class OpportunitiesSignVersionVO {
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

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String contractType;

    /**
     * 版本
     */
    /*@ApiModelProperty(value = "版本")
    private String version;*/

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


}