package com.jhx.common.util.db.migrate.function;

import com.jhx.common.util.db.migrate.model.TableModel;

import java.lang.annotation.Annotation;

/**
 * 表注解处理方法
 *
 * @author t.ch
 * @date 2018-03-10 15:46
 */
@FunctionalInterface
public interface TableAnnotationHandler {
    void accept(Annotation annotation, TableModel table);
}
