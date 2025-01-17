package com.internal.quote.dto;

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
 * 商机同步结构
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "BusinessOpportunityDTO", description = "商机同步结构")
public class BusinessOpportunityDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商机ID")
    private String id;

    @ApiModelProperty(value = "商机名称")
    private String name;

    @ApiModelProperty(value = "商机编码")
    private String code;

    @ApiModelProperty(value = "商机介绍")
    private String infoDesc;

    @ApiModelProperty(value = "商机阶段")
    private String stage;

    @ApiModelProperty(value = "商机阶段名称")
    private String stageName;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "归属人员ID")
    private String userId;

    @ApiModelProperty(value = "归属人员名称")
    private String userName;

    @ApiModelProperty(value = "售前支持ID")
    private String supportId;

    @ApiModelProperty(value = "售前支持名称")
    private String supportType;

    @ApiModelProperty(value = "产品大类名称")
    private String productName;

    @ApiModelProperty(value = "报备状态ID")
    private String reportStatus;

    @ApiModelProperty(value = "商机状态")
    private String currentState;

    @ApiModelProperty(value = "售前ID")
    private String vdef2;

    @ApiModelProperty(value = "成本售前支持区分")
    private String vdef3;

    @ApiModelProperty(value = "售前支持创建人")
    private String supportCreateBy;

    @ApiModelProperty(value = "售前支持创建时间")
    private Date supportCreateTime;

    @ApiModelProperty(value = "售前支持更新人")
    private String supportUpdateBy;

    @ApiModelProperty(value = "售前支持更新时间")
    private Date supportUpdateTime;

    public BusinessOpportunityDTO(String id, String name, String code, String infoDesc, String stage, String stageName, String customerId,
                                  String customerName, String userId, String userName, String supportId, String supportType, String productName,
                                  String reportStatus, String currentState, String vdef2, String vdef3, String supportCreateBy, Date supportCreateTime,
                                  String supportUpdateBy, Date supportUpdateTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.infoDesc = infoDesc;
        this.stage = stage;
        this.stageName = stageName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.userName = userName;
        this.supportId = supportId;
        this.supportType = supportType;
        this.productName = productName;
        this.reportStatus = reportStatus;
        this.currentState = currentState;
        this.vdef2 = vdef2;
        this.vdef3 = vdef3;
        this.supportCreateBy = supportCreateBy;
        this.supportCreateTime = supportCreateTime;
        this.supportUpdateBy = supportUpdateBy;
        this.supportUpdateTime = supportUpdateTime;
    }
}