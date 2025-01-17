package com.internal.quote.domain;

import com.baomidou.mybatisplus.annotation.*;
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
 * 报价系统-商机报价售前支持信息对象 quote_opportunities_children
 *
 * @author internal
 * @date 2024-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesChildren", description = "报价系统-商机报价售前支持信息对象")
@TableName("quote_opportunities_parent")
public class QuoteOpportunitiesParent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商机名称
     */
    @ApiModelProperty(value = "商机名称")
    @TableField(value = "name")
    @Excel(name = "商机名称")
    private String name;
    /**
     * 对应CRM商机D
     */
    @ApiModelProperty(value = "对应CRM商机D")
    @TableField(value = "crm_id")
    private String crmId;
    /**
     * 对应CRM商机阶段
     */
    @ApiModelProperty(value = "对应CRM商机阶段")
    @TableField(value = "crm_stage")
    private String crmStage;
    /**
     * 对应CRM商状态
     */
    @ApiModelProperty(value = "对应CRM商状态")
    @TableField(value = "crm_state")
    private String crmState;

    /**
     * 产品类别
     */
    @ApiModelProperty(value = "产品类别")
    @TableField(value = "category")
    @Excel(name = "产品类别")
    private String category;

    /**
     * 所属销售ID
     */
    @ApiModelProperty(value = "所属销售ID")
    @TableField(value = "sale_id")
    @Excel(name = "所属销售ID")
    private Long saleId;

    /**
     * 销售名称
     */
    @ApiModelProperty(value = "销售名称")
    @TableField(value = "sale_name")
    @Excel(name = "销售名称")
    private String saleName;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField(value = "customers_name")
    @Excel(name = "客户名称")
    private String customersName;

    /**
     * 商机介绍
     */
    @ApiModelProperty(value = "商机介绍")
    @TableField(value = "introduce")
    @Excel(name = "商机介绍")
    private String introduce;

    /**
     * 商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]
     */
    @ApiModelProperty(value = "商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]")
    @TableField(value = "status")
    @Excel(name = "商机状态", readConverterExp = "商机状态;[丢单:00001304,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]")
    private String status;


    /**
     * crm更新时间(判断一致性)
     */
    @TableField(value = "crm_update_time", fill = FieldFill.INSERT_UPDATE)
    private Date crmUpdateTime;

    /**
     * 丢单类型
     */
    @ApiModelProperty(value = "丢单类型")
    @TableField(value = "lose_type")
    @Excel(name = "丢单类型")
    private String loseType;


    public QuoteOpportunitiesParent(Long id, String name, String crmId, String crmStage, String crmState, String category,
                                    Long saleId, String saleName, String customersName, String introduce, String status, Date crmUpdateTime,
                                    String loseType) {
        this.id = id;
        this.name = name;
        this.crmId = crmId;
        this.crmStage = crmStage;
        this.crmState = crmState;
        this.category = category;
        this.saleId = saleId;
        this.saleName = saleName;
        this.customersName = customersName;
        this.introduce = introduce;
        this.status = status;
        this.crmUpdateTime = crmUpdateTime;
        this.loseType = loseType;
    }
}