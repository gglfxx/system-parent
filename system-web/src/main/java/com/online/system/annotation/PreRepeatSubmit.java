package com.online.system.annotation;

import java.lang.annotation.*;

/**
 * 防止重复提交
 */

@Target(ElementType.METHOD) // 说明了Annotation所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME) // 用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
@Documented //
public @interface PreRepeatSubmit {
    /**
     * 设置请求锁定时间
     *
     * @return
     */
    int lockTime() default 5;
}
