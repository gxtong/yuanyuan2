package com.jhx.common.util.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若该注解用在了类型A上，指定的注解value为B.class，则B的属性上的Display注解会自动加到A的属性上（如果二者相应的属性名一样的话）
 * @author 钱智慧
 * date 7/2/18 6:32 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SameDisplayAs {
    Class<?> value() ;
}
