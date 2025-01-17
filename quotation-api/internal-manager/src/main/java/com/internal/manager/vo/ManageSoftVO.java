package com.internal.manager.vo;

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
 * 管理系统-软著管理对象VO manage_soft
 *
 * @author internal
 * @date 2024-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "ManageSoftVO",description = "管理系统-软著管理对象VO")
public class ManageSoftVO extends BaseEntity {
private static final long serialVersionUID=1L;

    /** ID */
    @ApiModelProperty(value = "ID")
    private Long id;

    /** 软著类别[1:欣象软著,2:北光软著] */
    @ApiModelProperty(value = "软著类别[1:欣象软著,2:北光软著]")
    @Excel(name = "软著类别",readConverterExp = "1=欣象软著,2=北光软著")
    private String category;

    /** 软著简称 */
    @ApiModelProperty(value = "软著简称")
    @Excel(name = "软著简称")
    private String shortName;

    /** 软著名称 */
    @ApiModelProperty(value = "软著名称")
    @Excel(name = "软著名称")
    private String name;

    /** 对应软著id */
    @ApiModelProperty(value = "对应软著id")
    private Long linkId;

    /** 对应软著名 */
    @ApiModelProperty(value = "对应软著名(如果本数据是北光，就对应欣象)")
    @Excel(name = "对应软著名")
    private String linkName;

    /** 版本号 */
    @ApiModelProperty(value = "版本号")
    @Excel(name = "版本号")
    private String versionNumber;

    /** 登记号 */
    @ApiModelProperty(value = "登记号")
    @Excel(name = "登记号")
    private String registrationMark;

    /** 登记批准日期 */
    @ApiModelProperty(value = "登记批准日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "登记批准日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date regTime;

    /** 首次发表日期 */
    @ApiModelProperty(value = "首次发表日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "首次发表日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pushTime;

    /** 状态[1:可用,2:不可用] */
    @ApiModelProperty(value = "状态[1:可用,2:不可用]")
    @Excel(name = "状态",readConverterExp = "1=可用,2=不可用")
    private String status;

    /** 对应主产品名 */
    @ApiModelProperty(value = "对应主产品名")
    @Excel(name = "对应主产品名")
    private String productName;
    /** 产品名+状态 */
    @ApiModelProperty(value = "产品名+状态")
    private String productNameAndStatus;
    /**
     * 对应主产品id
     */
    @ApiModelProperty(value = "对应主产品id")
    private Long productId;
}