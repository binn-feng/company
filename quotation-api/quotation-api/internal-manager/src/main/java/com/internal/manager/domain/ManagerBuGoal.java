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

import java.util.Date;

/**
 * 管理系统-目标管理对象 manager_bu_goal
 *
 * @author internal
 * @date 2024-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "ManagerBuGoal", description = "管理系统-目标管理对象")
@TableName("manager_bu_goal")
public class ManagerBuGoal extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * CRM主键ID
     */
    @ApiModelProperty(value = "CRM主键ID")
    @TableField(value = "crm_id")
    @Excel(name = "CRM主键ID")
    private String crmId;

    /**
     * 目标年度
     */
    @ApiModelProperty(value = "目标年度")
    @TableField(value = "target_year")
    @Excel(name = "目标年度")
    private String targetYear;

    /**
     * 考核周期
     */
    @ApiModelProperty(value = "考核周期")
    @TableField(value = "type")
    @Excel(name = "考核周期")
    private String type;

    /**
     * 收入指标
     */
    @ApiModelProperty(value = "收入指标")
    @TableField(value = "indicator_types")
    @Excel(name = "收入指标")
    private String indicatorTypes;

    /**
     * 目标维度
     */
    @ApiModelProperty(value = "目标维度")
    @TableField(value = "corp_id")
    @Excel(name = "目标维度")
    private String corpId;

    /**
     * 维度类型
     */
    @ApiModelProperty(value = "维度类型")
    @TableField(value = "examine_type")
    @Excel(name = "维度类型")
    private String examineType;

    /**
     * 目标责任人
     */
    @ApiModelProperty(value = "目标责任人")
    @TableField(value = "response_person")
    @Excel(name = "目标责任人")
    private String responsePerson;

    /**
     * 基础指标
     */
    @ApiModelProperty(value = "基础指标")
    @TableField(value = "goal_value")
    @Excel(name = "基础指标")
    private String goalValue;

    /**
     * 冲刺指标
     */
    @ApiModelProperty(value = "冲刺指标")
    @TableField(value = "sport_goal")
    @Excel(name = "冲刺指标")
    private String sportGoal;

    /**
     * 业绩类型
     */
    @ApiModelProperty(value = "业绩类型")
    @TableField(value = "achievement_type")
    @Excel(name = "业绩类型")
    private String achievementType;

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


    public ManagerBuGoal(Long id, String crmId, String targetYear, String type, String indicatorTypes, String corpId, String examineType, String responsePerson, String goalValue, String sportGoal, String achievementType, Date beginDate, Date endDate) {
        this.id = id;
        this.crmId = crmId;
        this.targetYear = targetYear;
        this.type = type;
        this.indicatorTypes = indicatorTypes;
        this.corpId = corpId;
        this.examineType = examineType;
        this.responsePerson = responsePerson;
        this.goalValue = goalValue;
        this.sportGoal = sportGoal;
        this.achievementType = achievementType;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}