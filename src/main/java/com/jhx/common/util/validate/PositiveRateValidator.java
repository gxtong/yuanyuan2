package com.jhx.common.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author lhw 2017年10月28日
 */
public class PositiveRateValidator implements ConstraintValidator<PositiveRate, Object> {

    private boolean canBeZero;
    private boolean canOverOne;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return ValidateUtil.checkDecimal(canBeZero, false, (BigDecimal) value, 4, !canOverOne);
    }

    @Override
    public void initialize(PositiveRate constraintAnnotation) {
        canBeZero = constraintAnnotation.canBeZero();
        canOverOne = constraintAnnotation.canOverOne();
    }
}
