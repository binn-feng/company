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
 * 报价系统-销售合同对象 quote_sales_contract
 *
 * @author internal
 * @date 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteSalesContract", description = "报价系统-销售合同对象")
@TableName("quote_sales_contract")
public class QuoteSalesContract extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * CRM销售合同ID
     */
    @ApiModelProperty(value = "CRM销售合同ID")
    @TableField(value = "crm_contract_id")
    @Excel(name = "CRM销售合同ID")
    private String crmContractId;

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
     * 商机主题
     */
    @ApiModelProperty(value = "商机主题")
    @TableField(value = "opportunity_subject")
    @Excel(name = "商机主题")
    private String opportunitySubject;

    /**
     * 标准金额
     */
    @ApiModelProperty(value = "标准金额")
    @TableField(value = "standard_amount")
    @Excel(name = "标准金额")
    private BigDecimal standardAmount;

    /**
     * 合同折扣 (%)
     */
    @ApiModelProperty(value = "合同折扣 (%)")
    @TableField(value = "contract_discount")
    @Excel(name = "合同折扣 (%)")
    private BigDecimal contractDiscount;

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


    public QuoteSalesContract(Long id, String crmContractId, String contractName, String contractNumber, String contractType,
                              BigDecimal contractAmount, Date signingDate, String customerName, String opportunitySubject, BigDecimal standardAmount,
                              BigDecimal contractDiscount, BigDecimal receivedAmount, BigDecimal accountsReceivable) {
        this.id = id;
        this.crmContractId = crmContractId;
        this.contractName = contractName;
        this.contractNumber = contractNumber;
        this.contractType = contractType;
        this.contractAmount = contractAmount;
        this.signingDate = signingDate;
        this.customerName = customerName;
        this.opportunitySubject = opportunitySubject;
        this.standardAmount = standardAmount;
        this.contractDiscount = contractDiscount;
        this.receivedAmount = receivedAmount;
        this.accountsReceivable = accountsReceivable;
    }
}