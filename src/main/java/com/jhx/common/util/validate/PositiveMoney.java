package com.jhx.common.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 进行有效金额验证：正数，且小数位数不超过2位的数字，才是有效金额，
 * canBeZero为true时包含0，false时不包含0，默认false
 * 可以用在float、double、String等类型字段上
 * @author 钱智慧
 * @date 2017年9月13日 下午2:08:45
 */
@Constraint(validatedBy = PositiveMoneyValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveMoney{

    String message() default "{com.jhx.common.util.validate.PositiveMoney.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean canBeZero() default false;
}
