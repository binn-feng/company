package com.internal.manager.domain;

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
 * 管理系统-成本设置对象 manage_cost
 *
 * @author internal
 * @date 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "ManageCost",description = "管理系统-成本设置对象")
@TableName("manage_cost")
public class ManageCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER] */
    @ApiModelProperty(value = "类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER]")
    @TableField(value = "type")
    @Excel(name = "类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER]")
    private String type;

    /** 公司内部-元人天 */
    @ApiModelProperty(value = "公司内部-元人天")
    @TableField(value = "insider_days")
    @Excel(name = "公司内部-元人天")
    private BigDecimal insiderDays;

    /** 公司内部-元人月 */
    @ApiModelProperty(value = "公司内部-元人月")
    @TableField(value = "insider_month")
    @Excel(name = "公司内部-元人月")
    private BigDecimal insiderMonth;

    /** 本地驻场-元人天 */
    @ApiModelProperty(value = "本地驻场-元人天")
    @TableField(value = "local_presence_days")
    @Excel(name = "本地驻场-元人天")
    private BigDecimal localPresenceDays;

    /** 本地驻场-元人月 */
    @ApiModelProperty(value = "本地驻场-元人月")
    @TableField(value = "local_presence_month")
    @Excel(name = "本地驻场-元人月")
    private BigDecimal localPresenceMonth;

    /** 异地驻场-元人天 */
    @ApiModelProperty(value = "异地驻场-元人天")
    @TableField(value = "remote_presence_days")
    @Excel(name = "异地驻场-元人天")
    private BigDecimal remotePresenceDays;

    /** 异地驻场-元人月 */
    @ApiModelProperty(value = "异地驻场-元人月")
    @TableField(value = "remote_presence_month")
    @Excel(name = "异地驻场-元人月")
    private BigDecimal remotePresenceMonth;


    public ManageCost (Long id, String type, BigDecimal insiderDays, BigDecimal insiderMonth, BigDecimal localPresenceDays, BigDecimal localPresenceMonth, BigDecimal remotePresenceDays, BigDecimal remotePresenceMonth) {
            this.id = id;
            this.type = type;
            this.insiderDays = insiderDays;
            this.insiderMonth = insiderMonth;
            this.localPresenceDays = localPresenceDays;
            this.localPresenceMonth = localPresenceMonth;
            this.remotePresenceDays = remotePresenceDays;
            this.remotePresenceMonth = remotePresenceMonth;
        }
}
