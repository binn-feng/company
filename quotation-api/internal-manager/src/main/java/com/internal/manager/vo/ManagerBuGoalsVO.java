package com.internal.manager.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 管理系统-目标管理子对象 manager_bu_goals
 *
 * @author internal
 * @date 2024-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "ManagerBuGoalsVO", description = "目标管理返回子对象")
public class ManagerBuGoalsVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 项目主表ID
     */
    @ApiModelProperty(value = "项目主表ID")
    @Excel(name = "项目主表ID")
    private Long parentId;

    /**
     * CRM主键ID
     */
    @ApiModelProperty(value = "CRM主键ID")
    @Excel(name = "CRM主键ID")
    private String crmId;


    /**
     * 目标责任人
     */
    @ApiModelProperty(value = "目标责任人")
    @Excel(name = "目标责任人")
    private String responsePerson;
    /**
     * 评估类型
     */
    @ApiModelProperty(value = "评估类型")
    @Excel(name = "评估类型")
    private String assessment;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 比例
     */
    @ApiModelProperty(value = "比例")
    @Excel(name = "比例")
    private Long proportion;

    /**
     * 基准值
     */
    @ApiModelProperty(value = "基准值")
    @Excel(name = "基准值")
    private BigDecimal baseValue;

    /**
     * 冲刺比例
     */
    @ApiModelProperty(value = "冲刺比例")
    @Excel(name = "冲刺比例")
    private Long sprintFproportion;

    /**
     * 冲刺值
     */
    @ApiModelProperty(value = "冲刺值")
    @Excel(name = "冲刺值")
    private BigDecimal sprintValue;

    /**
     * CRM父级ID
     */
    @ApiModelProperty(value = "CRM父级ID")
    @Excel(name = "CRM父级ID")
    private String crmParentId;


    public ManagerBuGoalsVO(Long id, Long parentId, String crmId, String responsePerson, String assessment, Date beginDate,
                            Date endDate, Long proportion, BigDecimal baseValue, Long sprintFproportion, BigDecimal sprintValue, String crmParentId) {
        this.id = id;
        this.parentId = parentId;
        this.crmId = crmId;
        this.responsePerson = responsePerson;
        this.assessment = assessment;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.proportion = proportion;
        this.baseValue = baseValue;
        this.sprintFproportion = sprintFproportion;
        this.sprintValue = sprintValue;
        this.crmParentId = crmParentId;
    }
}