package com.jhx.common.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author 钱智慧
 * @date 2017年9月13日 下午2:09:46
 */
public class PositiveDecimalValidator implements ConstraintValidator<PositiveDecimal, Object> {

    private boolean canBeZero;
    private int scale;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return ValidateUtil.checkDecimal(canBeZero, false, (BigDecimal) value, scale, false);
    }

    @Override
    public void initialize(PositiveDecimal constraintAnnotation) {
        canBeZero = constraintAnnotation.canBeZero();
        scale = constraintAnnotation.scale();
    }
}