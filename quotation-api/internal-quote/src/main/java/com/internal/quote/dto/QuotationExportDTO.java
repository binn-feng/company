package com.internal.quote.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.math.BigDecimal;

/**
 * 报价系统-工作流邮箱设置对象 quote_email_setting
 *
 * @author internal
 * @date 2024-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteEmailSettingSaveDTO", description = "报价系统-工作流邮箱设置对象")
public class QuotationExportDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 是否北光报价
     */
    @ApiModelProperty(value = "是否北光报价")
    private Boolean isBeiGuangQuote;

    /**
     * 成本报价
     */
    @ApiModelProperty(value = "成本报价")
    private Long opportunitiesId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 商机主题
     */
    @ApiModelProperty(value = "商机主题")
    private String subject;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String date;

    /**
     * 销售名称
     */
    @ApiModelProperty(value = "销售名称")
    private String salesName;

    /**
     * 销售电话
     */
    @ApiModelProperty(value = "销售电话")
    private String salesPhone;

    /**
     * 软件开发报价
     */
    @ApiModelProperty(value = "软件开发报价")
    @NumberFormat(value = "#,##0.00")  // 设置为货币格式，并保留2位小数
    private BigDecimal softwareDevelopmentQuote;

    /**
     * 硬件费用报价
     */
    @ApiModelProperty(value = "硬件费用报价")
    @NumberFormat(value = "#,##0.00")  // 设置为货币格式，并保留2位小数
    private BigDecimal hardwareCostQuote;

    /**
     * 实施费用报价
     */
    @ApiModelProperty(value = "实施费用报价")
    @NumberFormat(value = "#,##0.00")  // 设置为货币格式，并保留2位小数
    private BigDecimal implementationCostQuote;

    /**
     * 	其他费用
     */
    @ApiModelProperty(value = "其他费用")
    @NumberFormat(value = "#,##0.00")  // 设置为货币格式，并保留2位小数
    private BigDecimal xxAdditionalQuote;

    /**
     * 总报价
     */
    @ApiModelProperty(value = "总报价")
    @NumberFormat(value = "#,##0.00")  // 设置为货币格式，并保留2位小数
    private BigDecimal totalQuote;
}