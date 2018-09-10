package com.jhx.common.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 验证MultiPartFile是否为空
 * author 钱智慧
 * date 2018/2/7 16:28
 */
@Constraint(validatedBy = FileNotNullValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileNotNull {
    String message() default "{com.jhx.common.util.validate.FileNotNull.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
