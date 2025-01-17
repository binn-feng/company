package com.internal.quote.vo;

import com.internal.common.annotation.Excel;
import com.internal.quote.domain.QuoteOpportunitiesImplement;
import com.internal.quote.dto.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class QuoteOpportunitiesDetailVO {
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
     * 评估版本
     */
    @ApiModelProperty(value = "评估版本")
    @Excel(name = "评估版本")
    private String version;
    /**
     * 评估版本(拼接)
     */
    @ApiModelProperty(value = "评估版本(拼接)")
    @Excel(name = "评估版本(拼接)")
    private String valuationVersion;

    /**
     * 评估说明
     */
    @ApiModelProperty(value = "评估说明")
    @Excel(name = "评估说明")
    private String valuationDesc;

    /**
     * 评估时间
     */
    @ApiModelProperty(value = "评估时间")
    @Excel(name = "评估时间")
    private String time;

    /**
     * 评估人
     */
    @ApiModelProperty(value = "评估人")
    @Excel(name = "评估人")
    private String person;

    /**
     * 评估金额
     */
    @ApiModelProperty(value = "评估金额")
    @Excel(name = "评估金额")
    @NotNull(message = "评估金额不能为空")
    private BigDecimal amount;

    /**
     * 售前支持小记
     */
    @ApiModelProperty(value = "售前支持小记")
    private QuoteOpportunitiesSupportVO quoteOpportunitiesSupportVO;
    /**
     * 定制开发小记
     */
    @ApiModelProperty(value = "定制开发小记")
    private QuoteOpportunitiesCustomizableVO quoteOpportunitiesCustomizableVo;
    /**
     * 产品平台小记
     */
    @ApiModelProperty(value = "产品平台小记")
    private QuoteOpportunitiesProductVO quoteOpportunitiesProductVO;
    /**
     * 自研硬件小记
     */
    @ApiModelProperty(value = "自研硬件小记")
    private QuoteOpportunitiesSelfVO quoteOpportunitiesSelfVO;
    /**
     * 外采硬件小记
     */
    @ApiModelProperty(value = "外采硬件小记")
    private QuoteOpportunitiesExternalVO quoteOpportunitiesExternalVO;
    /**
     * 实施小记
     */
    @ApiModelProperty(value = "实施小记")
    private QuoteOpportunitiesImplement quoteOpportunitiesImplement;
    /**
     * 其他小记
     */
    @ApiModelProperty(value = "其他小记")
    private QuoteOpportunitiesOtherVO quoteOpportunitiesOtherVO;

    /**
     * 售前成本备注
     */
    @ApiModelProperty(value = "售前成本备注")
    private String supportRemark;

    /** 报价部门占比列表 */
    @ApiModelProperty(value = "报价部门占比列表")
    private List<QuoteRadioVO> radioList;
}