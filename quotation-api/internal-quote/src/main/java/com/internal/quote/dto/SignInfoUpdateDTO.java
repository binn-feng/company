package com.internal.quote.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 签约状态更新DTO
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "SignInfoUpdateDTO", description = "签约状态更新DTO")
public class SignInfoUpdateDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String contractType;

    /** 签约状态-欣象vs客户 */
    @ApiModelProperty(value = "签约状态-欣象vs客户")
    private Boolean xxVsCust;

    /** 签约状态-欣象vs北光 */
    @ApiModelProperty(value = "签约状态-欣象vs北光")
    private Boolean xxVsNl;

    /** 签约状态-北光vs客户 */
    @ApiModelProperty(value = "签约状态-北光vs客户")
    private Boolean nlVsCust;

}