package com.jhx.common.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 进行有效正整数或者非负整数验证，
 * canBeZero为true时包含0，false时不包含0，默认false
 * 可以用在int、String等类型字段上
* @author 钱智慧
* @date 2017年9月13日 下午2:08:45
*/
@Constraint(validatedBy = PositiveIntValidator.class)
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)  
public @interface PositiveInt {

    String message() default "{com.jhx.common.util.validate.PositiveInt.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    boolean canBeZero() default false;
}
