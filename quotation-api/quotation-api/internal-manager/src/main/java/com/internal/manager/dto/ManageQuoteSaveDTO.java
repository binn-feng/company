package com.internal.manager.dto;

import com.internal.common.core.domain.BaseEntity;
import com.internal.manager.domain.ManageQuote;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 管理系统-报价设置对象 manage_quote
 *
 * @author internal
 * @date 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value = "ManageQuoteDTO",description = "管理系统-报价设置对象")
public class ManageQuoteSaveDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "保存list")
    private List<ManageQuote> managedQuoteList;

}
