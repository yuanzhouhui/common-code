package com.bright.commoncode.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CommonConstant
 *
 * @author YuanZhouhui
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstant {
    /**
     * 0
     */
    public static final String ZERO = "0";
    /**
     * 1
     */
    public static final String ONE = "1";
    /**
     * 2
     */
    public static final String TWO = "2";
    /**
     * 3
     */
    public static final String THREE = "3";

    /**
     * 4
     */
    public static final String FOUR = "4";

    /**
     * 5
     */
    public static final String FIVE = "5";

    /**
     * 6
     */
    public static final String SIX = "6";

    /**
     * 7
     */
    public static final String SEVEN = "7";

    public static final long DEFAULT_EXPORT_SHEET_SIZE = 10000L;

    /**
     * 上传文件夹
     */
    public static final String FILE_BASE_FOLDER = "/uploadFile/";

    /**
     * 上传文件保存基础路径
     */
    public static final String FILE_BASE_PATH = System.getProperty("user.dir") + FILE_BASE_FOLDER;


    /**
     * 当前记录起始索引 默认值
     */
    public static final long DEFAULT_EXPORT_PAGE_NUM = 1;
    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final long DEFAULT_EXPORT_PAGE_SIZE = 100000;

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 分隔符
     */
    public static final String YES_NO = "yes_no";

    /**
     * 验证码有效期（分钟）
     */
    public static long CAPTCHA_EXPIRATION = 2;
}
