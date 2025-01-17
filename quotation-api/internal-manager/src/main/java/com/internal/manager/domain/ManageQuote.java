package com.internal.manager.domain;

import java.math.BigDecimal;

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

/**
 * 管理系统-报价设置对象 manage_quote
 *
 * @author internal
 * @date 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "ManageQuote",description = "管理系统-报价设置对象")
@TableName("manage_quote")
public class ManageQuote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER] */
    @ApiModelProperty(value = "类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER ]")
    @TableField(value = "type")
    @Excel(name = "类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER ]")
    private String type;

    /** 默认税率 */
    @ApiModelProperty(value = "默认税率")
    @TableField(value = "duty_rate")
    @Excel(name = "默认税率")
    private BigDecimal dutyRate;

    /** 默认利润率 */
    @ApiModelProperty(value = "默认利润率")
    @TableField(value = "profitability")
    @Excel(name = "默认利润率")
    private BigDecimal profitability;


    public ManageQuote (Long id, String type, BigDecimal dutyRate, BigDecimal profitability) {
            this.id = id;
            this.type = type;
            this.dutyRate = dutyRate;
            this.profitability = profitability;
        }
}
