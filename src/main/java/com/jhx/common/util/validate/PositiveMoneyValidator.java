package com.jhx.common.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author 钱智慧
 * @date 2017年9月13日 下午2:09:46
 */
public class PositiveMoneyValidator implements ConstraintValidator<PositiveMoney, Object> {

    private boolean canBeZero;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return ValidateUtil.checkDecimal(canBeZero,false, (BigDecimal) value, 2,false);
    }

    @Override
    public void initialize(PositiveMoney constraintAnnotation) {
        canBeZero = constraintAnnotation.canBeZero();
    }

}