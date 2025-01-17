package com.internal.system.domain;

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
 * 内置邮箱设置对象 system_email
 *
 * @author internal
 * @date 2024-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "SystemEmail",description = "内置邮箱设置对象")
@TableName("system_email")
public class SystemEmail extends BaseEntity
        {
private static final long serialVersionUID=1L;

    /** $column.columnComment */
    @ApiModelProperty(value = "$column.columnComment")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型[管理:MANAGER,报价:QUOTE] */
    @ApiModelProperty(value = "类型[管理:MANAGER,报价:QUOTE]")
    @TableField(value = "type")
    @Excel(name = "类型[管理:MANAGER,报价:QUOTE]")
    private String type;

    /** 邮箱服务器 */
    @ApiModelProperty(value = "邮箱服务器")
    @TableField(value = "host")
    @Excel(name = "邮箱服务器")
    private String host;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    @TableField(value = "username")
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    @TableField(value = "password")
    @Excel(name = "密码")
    private String password;

    /** 当日发送数量 */
    @ApiModelProperty(value = "当日发送数量")
    @TableField(value = "send_num")
    @Excel(name = "当日发送数量")
    private Long sendNum;


    public SystemEmail (Long id,String type,String host,String username,String password,Long sendNum){
                this.id = id;
                this.type = type;
                this.host = host;
                this.username = username;
                this.password = password;
                this.sendNum = sendNum;
    }
}