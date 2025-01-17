package com.internal.quote.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价系统-商机粗略报价对象 quote_opportunities_rough
 *
 * @author internal
 * @date 2024-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesRoughQuery", description = "报价系统-商机粗略查询")
public class QuoteOpportunitiesRoughQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 是否获取最新版本
     */
    @ApiModelProperty(value = "是否获取最新版本")
    private Boolean latest;

    /**
     * 数据隔离
     */
    @ApiModelProperty(value = "数据隔离")
    private Boolean isolation = false;

}