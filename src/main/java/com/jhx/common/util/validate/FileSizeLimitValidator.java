package com.jhx.common.util.validate;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 钱智慧
 * @date 2017年9月13日 下午2:09:46
 */
public class FileSizeLimitValidator implements ConstraintValidator<FileSizeLimit, Object> {
    private float max;

    @Override
    public void initialize(FileSizeLimit constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean b = false;

        MultipartFile file = (MultipartFile) value;
        if (file.getSize() > max * 1024 * 1024) {
            b = false;
        } else {
            b = true;
        }

        return b;
    }
}