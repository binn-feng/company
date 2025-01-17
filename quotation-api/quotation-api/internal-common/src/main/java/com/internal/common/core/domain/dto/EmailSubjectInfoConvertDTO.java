package com.internal.common.core.domain.dto;

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
@ApiModel(value = "EmailSubjectInfoConvertDTO", description = "邮箱主题占位符转换信息")
public class EmailSubjectInfoConvertDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商机主题
     */
    @ApiModelProperty(value = "商机主题")
    @TemplateField(value = "商机主题")
    private String businessSubject;

    /**
     * 当前日期
     */
    @ApiModelProperty(value = "日期")
    @TemplateField(value = "日期")
    private String currentDate;

    /**
     * 售前部门
     */
    @ApiModelProperty(value = "售前部门")
    @TemplateField(value = "售前部门")
    private String preSaleDepartment;

    /**
     * 报价类型
     */
    @ApiModelProperty(value = "报价类型")
    @TemplateField(value = "报价类型")
    private String quoteType;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    @TemplateField(value = "版本号")
    private String version;

    /**
     * 销售报价审批结果
     */
    @ApiModelProperty(value = "销售报价审批结果")
    @TemplateField(value = "销售报价审批结果")
    private String approvalResult;

    /**
     * 签约申请审批结果
     */
    @ApiModelProperty(value = "签约申请审批结果")
    @TemplateField(value = "签约申请审批结果")
    private String signApprovalResult;

    public EmailSubjectInfoConvertDTO(String businessSubject, String currentDate, String preSaleDepartment, String quoteType, String version,
                                      String approvalResult, String signApprovalResult) {
        this.businessSubject = businessSubject;
        this.currentDate = currentDate;
        this.preSaleDepartment = preSaleDepartment;
        this.quoteType = quoteType;
        this.version = version;
        this.approvalResult = approvalResult;
        this.signApprovalResult = signApprovalResult;
    }
}