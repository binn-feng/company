package com.internal.quote.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 报价系统-收款计划对象 quote_payment_plan
 *
 * @author internal
 * @date 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuotePaymentPlan", description = "报价系统-收款计划对象")
@TableName("quote_payment_plan")
public class QuotePaymentPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * CRM收款计划ID
     */
    @ApiModelProperty(value = "CRM收款计划ID")
    @TableField(value = "crm_payment_plan_id")
    private String crmPaymentPlanId;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    @TableField(value = "contract_name")
    @Excel(name = "合同名称")
    private String contractName;

    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号")
    @TableField(value = "contract_number")
    @Excel(name = "合同号")
    private String contractNumber;

    /**
     * 合同类型
     */
    @ApiModelProperty(value = "合同类型")
    @TableField(value = "contract_type")
    @Excel(name = "合同类型")
    private String contractType;

    /**
     * 合同金额
     */
    @ApiModelProperty(value = "合同金额")
    @TableField(value = "contract_amount")
    @Excel(name = "合同金额")
    private BigDecimal contractAmount;

    /**
     * 签订日期
     */
    @ApiModelProperty(value = "签订日期")
    @TableField(value = "signing_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签订日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signingDate;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField(value = "customer_name")
    @Excel(name = "客户名称")
    private String customerName;

    /**
     * 业务员
     */
    @ApiModelProperty(value = "业务员")
    @TableField(value = "salesperson")
    @Excel(name = "业务员")
    private String salesperson;

    /**
     * 收款笔次
     */
    @ApiModelProperty(value = "收款笔次")
    @TableField(value = "payment_installment")
    @Excel(name = "收款笔次")
    private Integer paymentInstallment;

    /**
     * 收款日期
     */
    @ApiModelProperty(value = "收款日期")
    @TableField(value = "payment_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收款日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    /**
     * 收款条件
     */
    @ApiModelProperty(value = "收款条件")
    @TableField(value = "payment_terms")
    @Excel(name = "收款条件")
    private String paymentTerms;

    /**
     * 收款金额
     */
    @ApiModelProperty(value = "收款金额")
    @TableField(value = "payment_amount")
    @Excel(name = "收款金额")
    private BigDecimal paymentAmount;

    /**
     * 回款金额
     */
    @ApiModelProperty(value = "回款金额")
    @TableField(value = "received_amount")
    @Excel(name = "回款金额")
    private BigDecimal receivedAmount;

    /**
     * 应收款
     */
    @ApiModelProperty(value = "应收款")
    @TableField(value = "accounts_receivable")
    @Excel(name = "应收款")
    private BigDecimal accountsReceivable;

    public QuotePaymentPlan(Long id, String crmPaymentPlanId, String contractName, String contractNumber, String contractType, BigDecimal contractAmount,
                            Date signingDate, String customerName, String salesperson, Integer paymentInstallment, Date paymentDate, String paymentTerms,
                            BigDecimal paymentAmount, BigDecimal receivedAmount, BigDecimal accountsReceivable) {
        this.id = id;
        this.crmPaymentPlanId = crmPaymentPlanId;
        this.contractName = contractName;
        this.contractNumber = contractNumber;
        this.contractType = contractType;
        this.contractAmount = contractAmount;
        this.signingDate = signingDate;
        this.customerName = customerName;
        this.salesperson = salesperson;
        this.paymentInstallment = paymentInstallment;
        this.paymentDate = paymentDate;
        this.paymentTerms = paymentTerms;
        this.paymentAmount = paymentAmount;
        this.receivedAmount = receivedAmount;
        this.accountsReceivable = accountsReceivable;
    }
}