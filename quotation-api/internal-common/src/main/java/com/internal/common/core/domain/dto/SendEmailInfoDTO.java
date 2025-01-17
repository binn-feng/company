package com.internal.common.core.domain.dto;

import com.internal.common.annotation.TemplateField;
import com.internal.common.core.domain.BaseEntity;
import com.internal.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * 邮箱发送信息
 *
 * @author internal
 * @date 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "SendEmailInfoDTO", description = "邮箱发送信息")
public class SendEmailInfoDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 发送人信息
     */
    @ApiModelProperty(value = "发送人信息")
    private SysUser sysUser;

    /**
     * 模版转换信息
     */
    @ApiModelProperty(value = "模版转换信息")
    private EmailModelInfoConvertDTO emailModelInfoConvertDTO;

    /**
     * 主题转换信息
     */
    @ApiModelProperty(value = "主题转换信息")
    private EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO;

    /**
     * 抄送人列表
     */
    @ApiModelProperty(value = "抄送人列表")
    private List<SysUser> copyUser = new LinkedList<>();

    public SendEmailInfoDTO(SysUser sysUser, EmailModelInfoConvertDTO emailModelInfoConvertDTO, EmailSubjectInfoConvertDTO emailSubjectInfoConvertDTO, List<SysUser> copyUser) {
        this.sysUser = sysUser;
        this.emailModelInfoConvertDTO = emailModelInfoConvertDTO;
        this.emailSubjectInfoConvertDTO = emailSubjectInfoConvertDTO;
        this.copyUser = copyUser;
    }
}