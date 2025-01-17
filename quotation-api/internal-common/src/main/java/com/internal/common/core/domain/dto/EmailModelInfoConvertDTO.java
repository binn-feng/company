package com.internal.common.core.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.internal.common.annotation.Excel;
import com.internal.common.annotation.TemplateField;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 邮箱占位符转换信息
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "EmailModelInfoConvertDTO", description = "邮箱模版占位符转换信息")
public class EmailModelInfoConvertDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商机主题
     */
    @ApiModelProperty(value = "商机主题")
    @TemplateField(value = "商机主题")
    private String businessSubject;


    /**
     * 报价系统链接
     */
    @ApiModelProperty(value = "报价系统链接")
    @TemplateField(value = "报价系统链接")
    private String quoteSystemLink;

    /**
     * 售前部门
     */
    @ApiModelProperty(value = "售前部门")
    @TemplateField(value = "售前部门")
    private String preSaleDepartment;

    /**
     * 成本总额
     */
    @ApiModelProperty(value = "成本总额")
    @TemplateField(value = "成本总额")
    private String costTotal = "无";

    /**
     * 所属销售
     */
    @ApiModelProperty(value = "所属销售")
    @TemplateField(value = "所属销售")
    private String preSaleUser;

    /**
     * 所属售前
     */
    @ApiModelProperty(value = "所属售前")
    @TemplateField(value = "所属售前")
    private String preSaleUserName;

    /**
     * 销售对外报价
     */
    @ApiModelProperty(value = "销售对外报价")
    @TemplateField(value = "销售对外报价")
    private String saleQuote = "无";

    /**
     * 项目报价利润率
     */
    @ApiModelProperty(value = "项目报价利润率")
    @TemplateField(value = "项目报价利润率")
    private String profitRate = "无";

    /**
     * 继续跟进原因
     */
    @ApiModelProperty(value = "继续跟进原因")
    @TemplateField(value = "继续跟进原因")
    private String continueFollowReason;

    /**
     * 销售报价审批结果
     */
    @ApiModelProperty(value = "销售报价审批结果")
    @TemplateField(value = "销售报价审批结果")
    private String approvalResult = "无";

    /**
     * 北光合同总金额
     */
    @ApiModelProperty(value = "北光合同总金额")
    @TemplateField(value = "北光合同总金额")
    private String northTotalAmount;

    /**
     * 北光合同金额不含硬件部分
     */
    @ApiModelProperty(value = "北光合同金额不含硬件部分")
    @TemplateField(value = "北光合同金额不含硬件部分")
    private String northQuoteAmount;

    /**
     * 成本利润率
     */
    @ApiModelProperty(value = "成本利润率")
    @TemplateField(value = "成本利润率")
    private String costProfitRate;

    /**
     * 总成本利润率
     */
    @ApiModelProperty(value = "总成本利润率")
    @TemplateField(value = "总成本利润率")
    private String totalCostProfitRate;

    /**
     * 继续签约原因
     */
    @ApiModelProperty(value = "继续签约原因")
    @TemplateField(value = "继续签约原因")
    private String continueSignReason = "无";

    /**
     * 签约申请审批结果
     */
    @ApiModelProperty(value = "签约申请审批结果")
    @TemplateField(value = "签约申请审批结果")
    private String signApprovalResult = "无";

    /**
     * 报价申请结果
     */
    @ApiModelProperty(value = "报价申请结果")
    @TemplateField(value = "报价申请结果")
    private String quoteApprovalResult = "无";

    /**
     * 申请签约金额
     */
    @ApiModelProperty(value = "申请签约金额")
    @TemplateField(value = "申请签约金额")
    private String applySignAmount = "无";

    /**
     * 项目签约利润率
     */
    @ApiModelProperty(value = "项目签约利润率")
    @TemplateField(value = "项目签约利润率")
    private String signProfitRate = "无";
    /**
     * 原因说明
     */
    @ApiModelProperty(value = "丢单原因")
    @TemplateField(value = "丢单原因")
    @Excel(name = "丢单原因")
    private String loseReasonDesc = "无";

    /**
     * 签约申请详情图片
     */
    @ApiModelProperty(value = "签约申请详情图片")
    @TemplateField(value = "签约申请详情图片")
    @Excel(name = "签约申请详情图片")
    private String signApprovalDetailPic;

    public EmailModelInfoConvertDTO(String businessSubject, String quoteSystemLink, String preSaleDepartment, String costTotal, String preSaleUser,
                                    String preSaleUserName, String saleQuote, String profitRate, String continueFollowReason, String approvalResult,
                                    String northTotalAmount, String northQuoteAmount, String costProfitRate, String totalCostProfitRate, String continueSignReason,
                                    String signApprovalResult, String quoteApprovalResult, String applySignAmount, String signProfitRate,String loseReasonDesc,
                                    String signApprovalDetailPic) {
        this.businessSubject = businessSubject;
        this.quoteSystemLink = quoteSystemLink;
        this.preSaleDepartment = preSaleDepartment;
        this.costTotal = costTotal;
        this.preSaleUser = preSaleUser;
        this.preSaleUserName = preSaleUserName;
        this.saleQuote = saleQuote;
        this.profitRate = profitRate;
        this.continueFollowReason = continueFollowReason;
        this.approvalResult = approvalResult;
        this.northTotalAmount = northTotalAmount;
        this.northQuoteAmount = northQuoteAmount;
        this.costProfitRate = costProfitRate;
        this.totalCostProfitRate = totalCostProfitRate;
        this.continueSignReason = continueSignReason;
        this.signApprovalResult = signApprovalResult;
        this.quoteApprovalResult = quoteApprovalResult;
        this.applySignAmount = applySignAmount;
        this.signProfitRate = signProfitRate;
        this.loseReasonDesc = loseReasonDesc;
        this.signApprovalDetailPic = signApprovalDetailPic;
    }
}