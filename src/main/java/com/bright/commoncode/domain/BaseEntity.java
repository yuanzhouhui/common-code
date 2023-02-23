package com.bright.commoncode.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * BaseEntity
 *
 * @author YuanZhouhui
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Date createdTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonIgnore
    private Date updatedTime;

    /**
     * 乐观锁
     */
    @JsonIgnore
    @Version
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer revision;

    /**
     * 软删除标志
     */
    @JsonIgnore
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Integer deleted;

}
