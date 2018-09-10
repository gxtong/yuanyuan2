package com.jhx.common.util.validate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jhx.common.util.MyBeanUtils;
import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public class ValidateUtil {

    private static Validator Validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
            .buildValidatorFactory().getValidator();

    public static Result validate(Object valObj) {
        Result result = new Result(true, "数据正常");

        Set<ConstraintViolation<Object>> errSet = Validator.validate(valObj);

        Optional<ConstraintViolation<Object>> errOptional = errSet.stream().findFirst();
        if (errOptional.isPresent()) {
            ConstraintViolation<Object> cons = errOptional.get();
            String errMsg = cons.getMessage();

            try {
                if (errMsg.contains("{display}")) {
                    Object model = cons.getLeafBean();
                    PathImpl path = (PathImpl) cons.getPropertyPath();
                    String property = path.getLeafNode().asString();
                    Field field = model.getClass().getDeclaredField(property);
                    Display displayAnnotation = field.getDeclaredAnnotation(Display.class);
                    if (displayAnnotation != null) {
                        String display = displayAnnotation.value();
                        errMsg = StringUtils.replace(errMsg, "{display}", display);
                    } else {
                        errMsg = StringUtils.replace(errMsg, "{display}", property);
                    }
                }

                result.setOk(false);
                result.setMsg(errMsg);

            } catch (NoSuchFieldException | SecurityException e) {
                result.setOk(false);
                result.setMsg("参数验证错误");
            }

        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> ToBeanResult<T> toGsonAndValidator(String parameterJson, Class<?> T) {

        try {
            T obj = (T) new Gson().fromJson(parameterJson, T);
            Result validatorResult = validate(obj);
            ToBeanResult<T> result = MyBeanUtils.copy(validatorResult, ToBeanResult.class);
            result.setModel(obj);
            return result;
        } catch (JsonSyntaxException ex) {
            ToBeanResult<T> result = new ToBeanResult<>();
            result.setOk(false);
            result.setMsg("验证失败");
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ToBeanResult<T> toGsonAndValidator(String parameterJson, TypeToken<T> t) {

        try {
            T obj = (T) new Gson().fromJson(parameterJson, t.getType());
            Result validatorResult = validate(obj);
            ToBeanResult<T> result = MyBeanUtils.copy(validatorResult, ToBeanResult.class);
            result.setModel(obj);
            return result;
        } catch (JsonSyntaxException ex) {
            ToBeanResult<T> result = new ToBeanResult<>();
            result.setOk(false);
            result.setMsg("验证失败");
            return result;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToBeanResult<T> extends Result {
        private T model;
    }

    /**
     * desc 验证BigDecimal
     *
     * @param scale            小数位数
     * @param value            必须得是BigDecimal
     * @param mustLessEqualOne 是否必须小于等于1
     *                         date 4/14/18 3:24 PM
     * @author 钱智慧
     **/
    public static boolean checkDecimal(boolean canBeZero, boolean canBeNegative, BigDecimal value, int scale, boolean mustLessEqualOne) {
        if (value == null) {
            return false;
        }


        if (value.signum() == 0 && !canBeZero) {
            return false;
        }

        if (value.signum() < 0 && !canBeNegative) {
            return false;
        }

        if (mustLessEqualOne && value.compareTo(BigDecimal.ONE) > 0) {
            return false;
        }

        if (value.scale() > scale) {
            return false;
        } else {
            //数据长度限制
            if (value.precision() > DbConstant.MoneyPrecision + DbConstant.DecimalPrecision) {
                return false;
            }
            return true;
        }

    }
}
