package com.bright.commoncode.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * DictData
 *
 * @author YuanZhouhui
 */
@Data
public class DictData {

    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    @JsonIgnore
    private String dictType;

    /**
     * 字典排序
     */
    @JsonIgnore
    private Integer dictSort;
}
