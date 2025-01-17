package com.internal.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.*;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.internal.common.annotation.Excel;
import com.internal.common.annotation.Excel.ColumnType;
import com.internal.common.annotation.Excel.Type;
import com.internal.common.annotation.Excels;
import com.internal.common.core.domain.BaseEntity;
import com.internal.common.xss.Xss;

/**
 * 用户对象 sys_user
 * 
 * @author every
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    @TableId(value = "user_id",type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * crmUserId
     */
    @Excel(name = "crm用户ID")
    @TableField(value = "crm_user_id")
    private String crmUserId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Type.IMPORT)
    @TableField(value = "dept_id")
    private Long deptId;

    /** CRM部门ID */
    @Excel(name = "CRM部门ID")
    @TableField(exist = false)
    private String crmDeptId;

    /** 用户账号 */
    @Excel(name = "登录名称")
    @TableField(value = "user_name")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    @TableField(value = "nick_name")
    private String nickName;
    /** 用户类型 */
    @Excel(name = "用户名称")
    @TableField(value = "user_type")
    private String userType;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @TableField(value = "email")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    @TableField(value = "phonenumber")
    private String phonenumber;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @TableField(value = "sex")
    private String sex;

    /** 用户头像 */
    @Excel(name = "用户头像")
    @TableField(value = "avatar")
    private String avatar;

    /** 密码 */
    @TableField(value = "password")
    private String password;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    @TableField(value = "status")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Excel(name = "删除标志", readConverterExp = "0=存在,2=删除")
    @TableField(value = "del_flag")
    private String delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    @TableField(value = "login_ip")
    private String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @TableField(value = "login_date")
    private Date loginDate;

    /** 部门对象 */
    @Excels({
        @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    @TableField(exist = false)
    private SysDept dept;

    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRole> roles;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    /** 岗位组 */
    @TableField(exist = false)
    private Long[] postIds;

    /** 角色ID */
    @TableField(exist = false)
    private Long roleId;
    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }



    public boolean isAdmin()
    {
        return isAdmin(this.roles);
    }

    public static boolean isAdmin(List<SysRole> roles)
    {
        if(CollUtil.isNotEmpty(roles)){
            return roles.stream().anyMatch(item -> item.getRoleId().equals(1L));
        }
        return Boolean.FALSE;
    }
    public static boolean isAdmin(Long userId)
    {
        return userId != null && (1L == userId || 100L == userId || 101L == userId);
    }

}
