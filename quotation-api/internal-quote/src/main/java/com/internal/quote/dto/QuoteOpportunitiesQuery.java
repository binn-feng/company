package com.internal.quote.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价系统-商机报价信息对象 quote_opportunities
 *
 * @author internal
 * @date 2024-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@ApiModel(value = "QuoteOpportunitiesQuery", description = "报价系统-商机报价信息对象")
public class QuoteOpportunitiesQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商机名称
     */
    @ApiModelProperty(value = "商机名称")
    @Excel(name = "商机名称")
    private String name;

    /**
     * 报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]
     */
    @ApiModelProperty(value = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    @Excel(name = "报价类型;[详细报价:COST,粗略报价:ROUGH,无法报价:INCAPABLE]")
    private List<String> typeList;

    /**
     * 报价状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）
     */
    @ApiModelProperty(value = "报价状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    @Excel(name = "报价状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9）")
    private List<String> statusList;

    /**
     * 丢单类型
     */
    @ApiModelProperty(value = "丢单类型")
    private String loseType;

    /**
     * 丢单类型
     */
    @ApiModelProperty(value = "丢单类型列表")
    private List<String> loseTypeList;

    /**
     * 商机状态列表
     */
    @ApiModelProperty(value = "商机状态列表")
    @Excel(name = "商机状态列表", readConverterExp = "商机状态列表")
    private List<String> parentStatusList;

    /**
     * 用于查询
     */
    @ApiModelProperty(value = "用于查询")
    @Excel(name = "用于查询")
    private String query;

    /**
     * 报价方式[0:默认,1:自己报价,2:其他人报价]
     */
    @ApiModelProperty(value = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    @Excel(name = "报价方式[0:默认,1:自己报价,2:其他人报价]")
    private String quoteMethod;

    /**
     * 售前支持人员ID
     */
    @ApiModelProperty(value = "售前支持人员ID")
    @Excel(name = "售前支持人员ID")
    private List<String> supportPerson = new ArrayList<>();

    /**
     * 所属销售
     */
    @ApiModelProperty(value = "所属销售")
    @Excel(name = "所属销售")
    private List<String> salePerson = new ArrayList<>();

    /**
     * 启动权限认证
     */
    @ApiModelProperty(value = "启动权限认证")
    @Excel(name = "启动权限认证")
    private Boolean auth;

    /**
     * 销售审核记录
     */
    @ApiModelProperty(value = "销售审核记录")
    @TableField(value = "sale_audit_log")
    private String saleAuditLog;

    /**
     * 签约审核记录
     */
    @ApiModelProperty(value = "签约审核记录")
    @TableField(value = "sign_audit_log")
    private String signAuditLog;

}