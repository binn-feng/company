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
 * CRM商机更新参数
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@ApiModel(value = "BusinessOpportunityUpdateDTO", description = "CRM商机更新参数")
public class BusinessOpportunityUpdateDTO{
    private static final long serialVersionUID = 1L;


    /**
     * CRM系统商机ID
     */
    @ApiModelProperty(value = "CRM系统商机ID")
    private String id;

    /**
     * CRM商机阶段
     */
    @ApiModelProperty(value = "CRM商机阶段")
    private String stage;

    /**
     * CRM商机状态
     */
    @ApiModelProperty(value = "CRM商机状态")
    private String currentState;

    public BusinessOpportunityUpdateDTO(String id, String stage, String currentState) {
        this.id = id;
        this.stage = stage;
        this.currentState = currentState;
    }
}