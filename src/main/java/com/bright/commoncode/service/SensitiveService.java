package com.bright.commoncode.service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 *
 * @author YuanZhouhui
 */
public interface SensitiveService {

    /**
     * 是否脱敏
     *
     * @return 是否脱敏
     */
    boolean isSensitive();
}
