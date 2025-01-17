package com.internal.quote.vo;

import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报价系统-商机粗略报价对象VO quote_opportunities_rough
 *
 * @author internal
 * @date 2024-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesRoughVO", description = "报价系统-商机粗略报价对象VO")
public class QuoteOpportunitiesRoughVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;

    /**
     * 商机主表ID
     */
    @ApiModelProperty(value = "商机主表ID")
    @Excel(name = "商机主表ID")
    private Long opportunitiesParentId;

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
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    @Excel(name = "报价说明")
    private String valuationDesc;

    /**
     * 报价金额
     */
    @ApiModelProperty(value = "报价金额")
    @Excel(name = "报价金额")
    private BigDecimal amount;

    /**
     * 版本状态[0暂存;1启用;2生效中]
     */
    @ApiModelProperty(value = "版本状态[0暂存;1启用;2生效中]")
    @Excel(name = "版本状态[0暂存;1启用;2生效中]", readConverterExp = "0暂存;1启用;2生效中")
    private String status;

    /**
     * 详细报价信息
     */
    @ApiModelProperty(value = "详细报价信息")
    @Excel(name = "详细报价信息")
    private List<QuoteOpportunitiesRoughDetail> quoteOpportunitiesRoughDetails;

    /** 报价部门占比列表 */
    @ApiModelProperty(value = "报价部门占比列表")
    private List<QuoteRadioVO> radioList;

}