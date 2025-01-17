package com.internal.quote.dto;

import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商机状态同步结构
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "BusinessOpportunityStatusDTO", description = "商机状态同步结构")
public class BusinessOpportunityStatusDTO extends BaseEntity {
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

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "报备状态ID")
    private String reportStatus;

    @ApiModelProperty(value = "商机状态")
    private String currentState;

    public BusinessOpportunityStatusDTO(String id, String name, String code, String infoDesc, String stage, String customerId, String reportStatus, String currentState) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.infoDesc = infoDesc;
        this.stage = stage;
        this.customerId = customerId;
        this.reportStatus = reportStatus;
        this.currentState = currentState;
    }
}