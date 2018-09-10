package com.jhx.common.util.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若该注解用在了类型A上，指定的注解value为B.class，则B的属性上的注解会自动加到A的属性上，具体规则是：
 * 1、若二者的属性名和类型都一样，则B属性上的所有注解都会加到A上，A属性上就算有自己的注解也没有效果
 * 2、若二者的属性名一样，但类型不一样，则B属性的Display注解会加到A上，A属性就算加了自己的Display注解也没有效果，A属性上其他注解不受影响
 * 3、其他情况下，不会有注解复制，即A属性上所有自己的注解不受影响
 * @author 钱智慧
 * date 7/2/18 6:32 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SameAs {
    Class<?> value() ;
}
