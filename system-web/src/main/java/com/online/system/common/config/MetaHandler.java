package com.online.system.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.online.system.common.utils.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MetaHandler implements MetaObjectHandler {

    /**
     * 新增数据执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = SecurityUtil.getUsername();
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createUser", username, metaObject);
    }

    /**
     * 更新数据执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String username = SecurityUtil.getUsername();
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateUser",username, metaObject);
    }
}
