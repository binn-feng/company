package com.internal.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "ManagerBuGoals", description = "管理系统-目标管理子对象")
@TableName("manager_bu_goals")
public class ManagerBuGoals extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目主表ID
     */
    @ApiModelProperty(value = "项目主表ID")
    @TableField(value = "parent_id")
    @Excel(name = "项目主表ID")
    private Long parentId;

    /**
     * CRM主键ID
     */
    @ApiModelProperty(value = "CRM主键ID")
    @TableField(value = "crm_id")
    @Excel(name = "CRM主键ID")
    private String crmId;

    /**
     * 评估类型
     */
    @ApiModelProperty(value = "评估类型")
    @TableField(value = "assessment")
    @Excel(name = "评估类型")
    private String assessment;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    @TableField(value = "begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @TableField(value = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 比例
     */
    @ApiModelProperty(value = "比例")
    @TableField(value = "proportion")
    @Excel(name = "比例")
    private Long proportion;

    /**
     * 基准值
     */
    @ApiModelProperty(value = "基准值")
    @TableField(value = "base_value")
    @Excel(name = "基准值")
    private BigDecimal baseValue;

    /**
     * 冲刺比例
     */
    @ApiModelProperty(value = "冲刺比例")
    @TableField(value = "sprint_fproportion")
    @Excel(name = "冲刺比例")
    private Long sprintFproportion;

    /**
     * 冲刺值
     */
    @ApiModelProperty(value = "冲刺值")
    @TableField(value = "sprint_value")
    @Excel(name = "冲刺值")
    private BigDecimal sprintValue;

    /**
     * CRM父级ID
     */
    @ApiModelProperty(value = "CRM父级ID")
    @TableField(exist = false)
    @Excel(name = "CRM父级ID")
    private String crmParentId;


    public ManagerBuGoals(Long id, Long parentId, String crmId, String assessment, Date beginDate, Date endDate, Long proportion,
                          BigDecimal baseValue, Long sprintFproportion, BigDecimal sprintValue, String crmParentId) {
        this.id = id;
        this.parentId = parentId;
        this.crmId = crmId;
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