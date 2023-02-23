package com.bright.commoncode.excel.annotation;


/**
 * excel 列单元格合并(合并列相同项)
 * <p>
 * 需搭配 {@link com.bright.commoncode.excel.core.CellMergeStrategy} 策略使用
 *
 * @author YuanZhouhui
 */
public @interface CellMerge {
    /**
     * col index
     */
    int index() default -1;
}
