package com.internal.quote.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 报价系统-待签合同VO
 *
 * @author internal
 * @date 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "QuoteSignInfoVO",description = "报价系统-待签合同VO")
@TableName("quote_sign_info")
public class QuoteSignInfoVO extends BaseEntity
        {
private static final long serialVersionUID=1L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /** 商机售前支持ID */
    @ApiModelProperty(value = "商机售前支持ID")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesId;

    /** 商机主表ID */
    @ApiModelProperty(value = "商机主表ID")
    @Excel(name = "商机主表ID")
    private Long opportunitiesParentId;

    /** 合同类型(欣象代理:1,北光直签:2) */
    @ApiModelProperty(value = "合同类型(欣象代理:1,北光直签:2)")
    @Excel(name = "合同类型(欣象代理:1,北光直签:2)")
    private String contractType;

    /** 状态[0暂存;未生效;2生效中] */
    @ApiModelProperty(value = "状态[0暂存;未生效;2生效中]")
    @Excel(name = "状态[0暂存;未生效;2生效中]")
    private String status;

    /** 申请签约说明 */
    @ApiModelProperty(value = "申请签约说明")
    @Excel(name = "申请签约说明")
    private String signDesc;

    /** 销售报价id */
    /*@ApiModelProperty(value = "销售报价id")
    @Excel(name = "销售报价id")
    private Long presaleId;*/

    /** 签约状态-欣象vs客户 */
    @ApiModelProperty(value = "签约状态-欣象vs客户")
    @Excel(name = "签约状态-欣象vs客户")
    private Boolean xxVsCust;

    /** 签约状态-欣象vs北光 */
    @ApiModelProperty(value = "签约状态-欣象vs北光")
    @Excel(name = "签约状态-欣象vs北光")
    private Boolean xxVsNl;

    /** 签约状态-北光vs客户 */
    @ApiModelProperty(value = "签约状态-北光vs客户")
    @Excel(name = "签约状态-北光vs客户")
    private Boolean nlVsCust;


}