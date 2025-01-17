package com.internal.quote.dto;

import com.internal.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 报价系统-商机详细报价对象 quote_opportunities_detail
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesDetailSaveDTO", description = "报价系统-商机详细报价对象")
public class QuoteOpportunitiesDetailSaveDTO {
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
    @NotNull(message = "商机ID不能为空")
    private Long opportunitiesId;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesParentId;

    /**
     * 评估版本
     */
    @ApiModelProperty(value = "评估版本")
    @Excel(name = "评估版本")
    private String valuationVersion;

    /**
     * 评估说明
     */
    @ApiModelProperty(value = "评估说明")
    @Excel(name = "评估说明")
    private String valuationDesc;

    /**
     * 评估金额
     */
    @ApiModelProperty(value = "评估金额")
    @Excel(name = "评估金额")
    @NotNull(message = "评估金额不能为空")
    @Digits(integer = 10,fraction = 8,message = "最大支持10位整数和8位小数")
    private BigDecimal amount;

    /**
     * 售前成本
     */
    @ApiModelProperty(value = "售前")
    @Excel(name = "售前成本")
    private BigDecimal preTotal = BigDecimal.ZERO;

    /**
     * 售前支持小记
     */
    @ApiModelProperty(value = "售前支持小记")
    private List<QuoteOpportunitiesSupportSaveDTO> quoteOpportunitiesSupportList;

    /**
     * 定制开发
     */
    @ApiModelProperty(value = "定制开发")
    @Excel(name = "定制开发")
    private BigDecimal devTotal = BigDecimal.ZERO;

    /**
     * 定制开发小记
     */
    @ApiModelProperty(value = "定制开发小记")
    private List<QuoteOpportunitiesCustomizableSaveDTO> quoteOpportunitiesCustomizableList;

    /**
     * 产品平台
     */
    @ApiModelProperty(value = "产品平台")
    @Excel(name = "产品平台")
    private BigDecimal prodTotal = BigDecimal.ZERO;

    /**
     * 产品平台小记
     */
    @ApiModelProperty(value = "产品平台小记")
    private List<QuoteOpportunitiesProductSaveDTO> quoteOpportunitiesProductList;

    /**
     * 自研硬件
     */
    @ApiModelProperty(value = "自研硬件")
    @Excel(name = "自研硬件")
    private BigDecimal selfTotal = BigDecimal.ZERO;

    /**
     * 自研硬件小记
     */
    @ApiModelProperty(value = "自研硬件小记")
    private List<QuoteOpportunitiesSelfSaveDTO> quoteOpportunitiesSelfList;

    /**
     * 外采硬件
     */
    @ApiModelProperty(value = "外采硬件")
    @Excel(name = "外采硬件")
    private BigDecimal extTotal = BigDecimal.ZERO;

    /**
     * 外采硬件小记
     */
    @ApiModelProperty(value = "外采硬件小记")
    private List<QuoteOpportunitiesExternalSaveDTO> quoteOpportunitiesExternalList;

    /**
     * 实施
     */
    @ApiModelProperty(value = "实施")
    @Excel(name = "实施")
    private BigDecimal impTotal = BigDecimal.ZERO;

    /**
     * 实施小记
     */
    @ApiModelProperty(value = "实施小记")
    private QuoteOpportunitiesImplementSaveDTO quoteOpportunitiesImplementList;

    /**
     * 其他
     */
    @ApiModelProperty(value = "其他")
    @Excel(name = "其他")
    private BigDecimal otherTotal = BigDecimal.ZERO;

    /**
     * 其他小记
     */
    @ApiModelProperty(value = "其他小记")
    private List<QuoteOpportunitiesOtherSaveDTO> quoteOpportunitiesOtherList;


    /**
     * 版本状态（0暂存 1启用）
     */
    @ApiModelProperty(value = "版本状态（0暂存 1启用）")
    @Excel(name = "版本状态", readConverterExp = "0=暂存,1=启用")
    private String status;

    /**
     * 售前成本备注
     */
    @ApiModelProperty(value = "售前成本备注")
    private String supportRemark;

    /** 报价部门占比列表 */
    @ApiModelProperty(value = "报价部门占比列表")
    private List<QuoteRadioDTO> radioList;


}