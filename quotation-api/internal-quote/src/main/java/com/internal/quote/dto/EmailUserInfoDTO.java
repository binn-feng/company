package com.internal.quote.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zdliu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel(value = "EmailUserInfoDTO", description = "邮件用户相关信息")
public class EmailUserInfoDTO {

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("用户名")
    private String nickName;

    @ApiModelProperty("是否显示关闭按钮")
    private boolean isDefault;

    @ApiModelProperty("用户id")
    private String userId;



}