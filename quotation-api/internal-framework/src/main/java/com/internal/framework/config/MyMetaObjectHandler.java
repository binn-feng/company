package com.internal.framework.config;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.internal.common.core.domain.entity.SysUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Nl
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String userId = getCurrentUserId(); // 获取当前登录用户ID

        this.strictInsertFill(metaObject, "createBy", String.class, userId);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userId = getCurrentUserId(); // 获取当前登录用户ID

        this.strictUpdateFill(metaObject, "updateBy", String.class, userId);
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SysUser sysUser = BeanUtil.toBean(authentication.getPrincipal(), SysUser.class);// 假设用户名是用户ID
            return String.valueOf(sysUser.getUserId());
        }
        return null;
    }
}