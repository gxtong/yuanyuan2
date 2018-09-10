package com.jhx.common.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author 钱智慧
 * date 2018/2/7 16:55
 */
@Constraint(validatedBy = FileSizeLimitValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSizeLimit {

    //默认4M的限制
    float max() default 4;

    String message() default "{com.jhx.common.util.validate.FileSizeLimit.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
