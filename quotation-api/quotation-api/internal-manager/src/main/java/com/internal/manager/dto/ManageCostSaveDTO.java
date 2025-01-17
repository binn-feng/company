package com.internal.manager.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import com.internal.manager.domain.ManageCost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理系统-成本设置对象 manage_cost
 *
 * @author internal
 * @date 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "ManageCostSaveDTO",description = "管理系统-成本设置对象")
public class ManageCostSaveDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "保存list")
    private List<ManageCost> manageCostList;

}
