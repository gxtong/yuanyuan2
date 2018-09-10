package com.jhx.common.util.validate;

import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.db.DbConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;


public class RateControlValidator implements ConstraintValidator<RateControl, Object> {

    private String[] targetArr;

    private String[] byArr;

    private boolean[] rateCanBeZeroArr;

    private boolean[] decimalCanBeZeroArr;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean ret = false;

        for (int i = 0; i < targetArr.length; i++) {
            String target = targetArr[i];
            String by = byArr[i];

            boolean byRate = (boolean) ReflectUtil.getValue(value, by);
            BigDecimal targetValue = (BigDecimal) ReflectUtil.getValue(value, target);
            boolean canBeZero=canBeZero(byRate,i);
            return ValidateUtil.checkDecimal(canBeZero,false,targetValue,byRate?4: DbConstant.DecimalScale,byRate);
        }

        return ret;
    }

    private boolean canBeZero(boolean byRate, int index) {
        if (byRate) {
            return rateCanBeZeroArr.length == 1 ? rateCanBeZeroArr[0] : rateCanBeZeroArr[index];
        } else {
            return decimalCanBeZeroArr.length == 1 ? decimalCanBeZeroArr[0] : decimalCanBeZeroArr[index];
        }
    }

    @Override
    public void initialize(RateControl constraintAnnotation) {
        rateCanBeZeroArr = constraintAnnotation.rateCanBeZero();
        decimalCanBeZeroArr = constraintAnnotation.decimalCanBeZero();
        targetArr = constraintAnnotation.target();
        byArr = constraintAnnotation.by();
    }
}
