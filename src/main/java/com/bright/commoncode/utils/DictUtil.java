package com.bright.commoncode.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.bright.commoncode.constant.CommonConstant;
import com.bright.commoncode.service.DictService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * DictUtil
 *
 * @author YuanZhouhui
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DictUtil {


    /**
     * 通过字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, CommonConstant.SEPARATOR);
    }

    /**
     * 通过字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, CommonConstant.SEPARATOR);
    }

    /**
     * 通过字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator) {
        return SpringUtil.getBean(DictService.class).getDictValue(dictType, dictLabel, separator);
    }

    /**
     * 通过字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator) {
        return SpringUtil.getBean(DictService.class).getDictLabel(dictType, dictValue, separator);
    }
}
