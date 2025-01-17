package com.internal.manager.dto;

import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理系统-产品管理对象查询用DTO
 *
 * @author internal
 * @date 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "ManageProductQueryDTO",description = "管理系统-产品管理对象查询用DTO")
public class ManageProductQueryDTO extends BaseEntity
{
    /** 产品类别 */
    @ApiModelProperty(value = "产品类别")
    private String category;

    /** 产品类别List */
    @ApiModelProperty(value = "产品类别List")
    private List<String> categoryList;

    /** 产品名称 */
    @ApiModelProperty(value = "产品名称")
    private String name;

    /** 主产品名 */
    @ApiModelProperty(value = "主产品名")
    private String parentName;

    /** 产品状态[1:可用,2:不可用] */
    @ApiModelProperty(value = "产品状态[1:可用,2:不可用]")
    private String status;

    /** 产品状态列表 */
    @ApiModelProperty(value = "产品状态列表")
    private List<String> statusList;

    /** 主产品id */
    @ApiModelProperty(value = "主产品id")
    private Long parentId;

    /**
     * 是否使用权限
     */
    @ApiModelProperty(value = "是否使用权限")
    private Boolean isAuth;


}