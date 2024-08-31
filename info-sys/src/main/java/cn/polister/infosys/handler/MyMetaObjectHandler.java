package cn.polister.infosys.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long userId;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            //e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            userId = null;
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
        if (userId != null) {
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}