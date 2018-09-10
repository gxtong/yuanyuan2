package com.jhx.common.util.db.migrate.annotation;

import java.lang.annotation.*;

/**
 * 迁移注解, 用于应用更改
 *
 * @author t.ch
 * @date 2018-03-19 08:39
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Migrate {

    /**
     * 指明旧字段或旧表名, 从而可以做到更改字段名、表名时不丢失旧数据
     */
    String oldName() default "";
}
