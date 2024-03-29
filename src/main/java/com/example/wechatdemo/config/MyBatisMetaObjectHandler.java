package com.example.wechatdemo.config;

import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyBatisMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);

        User user = UserTokenInterceptor.userThreadLocal.get();
        if (user != null) {
            if (metaObject.hasGetter("createByName") && metaObject.getValue("createByName") == null) {
                this.setFieldValByName("createByName", user.getUserName(), metaObject);
            }
            this.setFieldValByName("updateByName", user.getUserName(), metaObject);
            if (metaObject.hasGetter("createById") && metaObject.getValue("createById") == null) {
                this.setFieldValByName("createById", user.getUserId(), metaObject);
            }
            this.setFieldValByName("updateById", user.getUserId(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);

        User user = UserTokenInterceptor.userThreadLocal.get();
        if (user != null) {
            this.setFieldValByName("updateByName", user.getUserName(), metaObject);
            this.setFieldValByName("updateById", user.getUserId(), metaObject);
        }
    }
}
