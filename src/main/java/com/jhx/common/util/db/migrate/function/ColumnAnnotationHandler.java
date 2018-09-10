package com.jhx.common.util.db.migrate.function;

import com.jhx.common.util.db.migrate.model.ColumnModel;

import java.lang.annotation.Annotation;

/**
 * 字段注解处理方法
 *
 * @author t.ch
 * @date 2018-03-10 14:43
 */
@FunctionalInterface
public interface ColumnAnnotationHandler {
    void accept(Annotation annotation, ColumnModel column);
}
