package com.jhx.common.util.db.migrate.annotation;

import java.lang.annotation.*;

/**
 * 用于加索引
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IndexColumn {
    /**
     * 索引名称, 联合索引可以使用相同的名称标识
     * @return
     */
    String value() default "";

    /**
     * 是否唯一约束
     * @return
     */
    boolean unique() default false;
}
