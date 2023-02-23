package com.bright.commoncode.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bright.commoncode.domain.BaseEntity;
import com.bright.commoncode.enums.ErrorCodeEnum;
import com.bright.commoncode.exception.CoreException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 新增或修改自动填充
 *
 * @author YuanZhouhui
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = ObjectUtil.isNotNull(baseEntity.getCreatedTime())
                        ? baseEntity.getCreatedTime() : new Date();
                baseEntity.setCreatedTime(current);
                baseEntity.setUpdatedTime(current);
                String username = StringUtils.isNotBlank(baseEntity.getCreatedBy())
                        ? baseEntity.getCreatedBy() : getLoginUsername();
                // 当前已登录 且 创建人为空 则填充
                baseEntity.setCreatedBy(username);
                // 当前已登录 且 更新人为空 则填充
                baseEntity.setUpdatedBy(username);
            }
        } catch (Exception e) {
            throw new CoreException(ErrorCodeEnum.USER_NOT_LOGIN, "自动注入异常 => " + e.getMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = new Date();
                // 更新时间填充(不管为不为空)
                baseEntity.setUpdatedTime(current);
                String username = getLoginUsername();
                // 当前已登录 更新人填充(不管为不为空)
                if (StringUtils.isNotBlank(username)) {
                    baseEntity.setUpdatedBy(username);
                }
            }
        } catch (Exception e) {
            throw new CoreException(ErrorCodeEnum.USER_NOT_LOGIN, "自动注入异常 => " + e.getMessage());
        }
    }

    /**
     * 获取登录用户名
     */
    private String getLoginUsername() {
        // 获取当前登录用户信息
        //return SecurityUtil.getCurrentUserName();
        return "";
    }
}
