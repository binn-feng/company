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
@ApiModel(value = "EmailUserInfoConvertDTO", description = "邮箱发送抄送人占位符转换信息")
public class EmailUserInfoConvertDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
    private String preSaleDepartment;

    public EmailUserInfoConvertDTO(String preSaleUser, String preSaleDepartment) {
        this.preSaleUser = preSaleUser;
        this.preSaleDepartment = preSaleDepartment;
    }
}