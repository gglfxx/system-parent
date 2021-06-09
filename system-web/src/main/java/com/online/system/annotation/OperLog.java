package com.online.system.annotation;

import java.lang.annotation.*;

/**
 * 自定义日志
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLog {
    String module() default ""; // 操作模块
    String type() default "";  // 操作类型
    String desc() default "";  // 操作说明
}
