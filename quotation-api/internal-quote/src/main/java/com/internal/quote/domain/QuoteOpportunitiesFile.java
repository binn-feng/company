package com.internal.quote.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.common.annotation.Excel;
import com.internal.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商机附件对象 quote_opportunities_file
 *
 * @author internal
 * @date 2024-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Builder
@ApiModel(value = "QuoteOpportunitiesFile", description = "商机附件对象")
@TableName("quote_opportunities_file")
public class QuoteOpportunitiesFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商机ID
     */
    @ApiModelProperty(value = "商机ID")
    @TableField(value = "opportunities_id")
    @Excel(name = "商机ID")
    private Long opportunitiesId;

    /**
     * 商机售前支持ID
     */
    @ApiModelProperty(value = "商机售前支持ID")
    @TableField(value = "opportunities_children_id")
    @Excel(name = "商机售前支持ID")
    private Long opportunitiesParentId;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    @TableField(value = "file_type")
    @Excel(name = "文件类型")
    private String fileType;

    /**
     * 文件相对地址
     */
    @ApiModelProperty(value = "文件相对地址")
    @TableField(value = "path")
    @Excel(name = "文件相对地址")
    private String path;

    /**
     * 文件访问地址
     */
    @ApiModelProperty(value = "文件访问地址")
    @TableField(value = "url")
    @Excel(name = "文件访问地址")
    private String url;

    /**
     * 唯一文件名
     */
    @ApiModelProperty(value = "唯一文件名")
    @TableField(value = "unique_file_name")
    @Excel(name = "唯一文件名")
    private String uniqueFileName;

    /**
     * 原始文件名
     */
    @ApiModelProperty(value = "原始文件名")
    @TableField(value = "original_file_name")
    @Excel(name = "原始文件名")
    private String originalFileName;

    /**
     * 后缀
     */
    @ApiModelProperty(value = "后缀")
    @TableField(value = "suffix")
    @Excel(name = "后缀")
    private String suffix;


    public QuoteOpportunitiesFile(Long id, Long opportunitiesId, Long opportunitiesParentId, String fileType, String path,
                                  String url, String uniqueFileName, String originalFileName, String suffix) {
        this.id = id;
        this.opportunitiesId = opportunitiesId;
        this.opportunitiesParentId = opportunitiesParentId;
        this.fileType = fileType;
        this.path = path;
        this.url = url;
        this.uniqueFileName = uniqueFileName;
        this.originalFileName = originalFileName;
        this.suffix = suffix;
    }
}