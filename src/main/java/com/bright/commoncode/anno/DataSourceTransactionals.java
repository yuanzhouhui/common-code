package com.bright.commoncode.anno;

import java.lang.annotation.*;

/**
 * @author yuanzhouhui
 * @description 多数据源事务注解
 * @date 2022-05-26 18:27
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataSourceTransactionals {
	/**
	 * 事务管理器数组
	 */
	String[] transactionManagers();
}
