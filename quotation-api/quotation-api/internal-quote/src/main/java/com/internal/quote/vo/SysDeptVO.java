package com.internal.quote.vo;

import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门表 sys_dept
 * 
 * @author every
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    /** crm部门ID */
    @ApiModelProperty(value = "crm部门ID")
    private String crmDeptId;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String userNames;

    /** 用户crmId */
    @ApiModelProperty(value = "用户crmId")
    private String crmUserIds;


}
