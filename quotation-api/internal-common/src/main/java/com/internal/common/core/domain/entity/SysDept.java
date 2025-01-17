package com.internal.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.internal.common.core.domain.BaseEntity;

/**
 * 部门表 sys_dept
 * 
 * @author every
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @TableId(value = "dept_id",type = IdType.ASSIGN_ID)
    private Long deptId;

    /** crm部门ID */
    @TableField(value = "crm_dept_id")
    private String crmDeptId;

    /** 父部门ID */
    @TableField(value = "parent_id")
    private Long parentId;

    /** 祖级列表 */
    @TableField(value = "ancestors")
    private String ancestors;

    /** 部门名称 */
    @TableField(value = "dept_name")
    private String deptName;

    /** 显示顺序 */
    @TableField(value = "order_num")
    private Integer orderNum;

    /** 负责人 */
    @TableField(value = "leader")
    private String leader;

    /** 联系电话 */
    @TableField(value = "phone")
    private String phone;

    /** 邮箱 */
    @TableField(value = "email")
    private String email;

    /** 部门状态:0正常,1停用 */
    @TableField(value = "status")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    private String delFlag;

    /** 父部门名称 */
    @TableField(exist = false)
    private String parentName;
    
    /** 子部门 */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<SysDept>();

}
