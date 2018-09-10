

package com.jhx.common.util.validate;

        import javax.validation.Constraint;
        import javax.validation.Payload;
        import java.lang.annotation.ElementType;
        import java.lang.annotation.Retention;
        import java.lang.annotation.RetentionPolicy;
        import java.lang.annotation.Target;

/**
 * 进行有效非负小数验证，
 * canBeZero为true时包含0，false时不包含0，默认false
 * scale：最大小数位数，默认是2
 * 只适用于BigDecimal
 * @author 钱智慧
 * @date 2017年9月13日 下午2:08:45
 */
@Constraint(validatedBy = PositiveDecimalValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveDecimal {

    String message() default "{com.jhx.common.util.validate.PositiveDecimal.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * desc canBeZero为true时包含0，false时不包含0，默认false
     * @author 钱智慧
     * date 4/14/18 1:33 PM
     **/
    boolean canBeZero() default false;

    /**
     * desc 最大小数位数，默认是2
     * @author 钱智慧
     * date 4/14/18 1:33 PM
     **/
    int scale() default 2;
}


