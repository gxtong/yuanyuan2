package com.jhx.common.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 进行有效比例验证：正数，且小数位数不超过4位的数字，小于等于1才是有效比例，
 * canBeZero为true时包含0，false时不包含0，默认false
 * canOverOne为true时可以大于1，默认false
 * 适用类型：BigDecimal
 * @author lhw
 * 2017年10月28日
 */
@Constraint(validatedBy = PositiveRateValidator.class)
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)  
public @interface PositiveRate {

    String message() default "{com.jhx.common.util.validate.PositiveRate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean canBeZero() default false;
    boolean canOverOne() default false;
}
