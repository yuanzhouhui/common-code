package com.bright.commoncode.service;


import com.bright.commoncode.base.DictData;

import java.util.List;

/**
 * @author YuanZhouhui
 */
public interface DictService {

    /**
     * 根据字典类型查询字典信息
     *
     * @param dictType 字典类型
     * @return 字典信息
     */
    List<DictData> selectDict(String dictType);

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictLabel 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    String getDictValue(String dictType, String dictLabel, String separator);

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictValue 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    String getDictLabel(String dictType, String dictValue, String separator);
}
