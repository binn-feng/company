package com.internal.manager.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理系统-软著管理对象QueryDTO manage_soft
 *
 * @author internal
 * @date 2024-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "ManageSoftQueryDTO",description = "管理系统-软著管理对象QueryDTO")
public class ManageSoftQueryDTO extends BaseEntity {
private static final long serialVersionUID=1L;

    /** 软著类别[1:欣象软著,2:北光软著] */
    @ApiModelProperty(value = "软著类别[1:欣象软著,2:北光软著]")
    @Excel(name = "软著类别[1:欣象软著,2:北光软著]")
    private String category;

    /** 软著简称 */
    @ApiModelProperty(value = "软著简称")
    @Excel(name = "软著简称")
    private String shortName;

    /** 软著名称 */
    @ApiModelProperty(value = "软著名称")
    @Excel(name = "软著名称")
    private String name;

    /** 版本号 */
    @ApiModelProperty(value = "版本号")
    @Excel(name = "版本号")
    private String versionNumber;

    /** 登记号 */
    @ApiModelProperty(value = "登记号")
    @Excel(name = "登记号")
    private String registrationMark;

    /** 状态[1:可用,2:不可用] */
    @ApiModelProperty(value = "状态[1:可用,2:不可用]")
    @Excel(name = "状态[1:可用,2:不可用]")
    private String status;
}