package com.jhx.common.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @author 钱智慧
* @date 2017年9月13日 下午2:09:46
*/
public class PositiveIntValidator implements ConstraintValidator<PositiveInt, Object> {


    private boolean canBeZero;
	
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String data = String.valueOf(value);
        if(canBeZero){
            return data.matches("^\\d+$");
        }else{
            return data.matches("^[0-9]*[1-9][0-9]*$");
        }
    }

	@Override
	public void initialize(PositiveInt constraintAnnotation) {
		canBeZero = constraintAnnotation.canBeZero();
	}

}