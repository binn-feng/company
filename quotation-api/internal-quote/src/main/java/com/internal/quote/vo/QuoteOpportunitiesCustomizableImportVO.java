package com.internal.quote.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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

import java.math.BigDecimal;

/**
 * 报价系统-功能清单
 *
 * @author internal
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteOpportunitiesCustomizableImportVO", description = "报价系统-功能清单")
public class QuoteOpportunitiesCustomizableImportVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
    @ExcelIgnore()
    private Long id;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    @ExcelProperty(value = "模块")
    private String moduleName;

    /**
     * 子模块功能
     */
    @ApiModelProperty(value = "子模块功能")
    @ExcelProperty(value = "子模块功能")
    private String subModuleName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述/备注")
    private String description;


}