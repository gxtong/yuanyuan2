package com.jhx.common.layui;

import com.jhx.common.layui.dto.BaseModel;
import com.jhx.common.layui.dto.MinMaxValueModel;
import com.jhx.common.layui.dto.RegExModel;
import com.jhx.common.layui.dto.ValueModel;
import com.jhx.common.util.LogUtil;
import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.validate.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;

/**
 * 将验证注解转化为Model
 *
 * @author 钱智慧
 * date 6/28/18 10:00 AM
 */
public class ModelUtil {

    private static String postHandleMsg(String srcMsg, String type, Object value) {
        if (StringUtils.isBlank(type)) {
            return srcMsg;
        }
        return srcMsg.replace(new StringBuilder("{").append(type).append("}"), value.toString());
    }

    //预处理注解信息：1、若没有指定注解的message，则将message置为空字符 2、替换display
    private static String preHandleMsg(Annotation annotation, Field propField) {
        try {
            String message = ReflectUtil.annotationValue(annotation, "message").toString();
            String msg = message.contains(annotation.annotationType().getName()) ? "" : message;

            Display displayAnnotation = propField.getDeclaredAnnotation(Display.class);
            String display = displayAnnotation != null ? displayAnnotation.value() : propField.getName();

            msg = StringUtils.replace(msg, "{display}", display).trim();
            return msg;
        } catch (Exception e) {
            LogUtil.err(e);
            return "";
        }
    }

    //require，传入NotNull NotBlank NotEmpty
    public static BaseModel getRequiredModel(Annotation annotation, Field propField) {
        BaseModel ret = null;

        if (annotation instanceof NotNull || annotation instanceof NotBlank || annotation instanceof NotEmpty) {
            String msg = ModelUtil.preHandleMsg(annotation, propField);
            ret = new BaseModel(true, msg);
        }

        return ret;
    }


    //min-len和max-len：传入Size Range Length
    public static ValueModel getMinMaxLenModel(Annotation annotation, boolean isMin, Field propField) {
        ValueModel ret = null;
        if (propField.getType() != String.class) {
            return ret;
        }
        if (annotation instanceof Size || annotation instanceof Length || annotation instanceof Range) {
            try {
                Integer min = Integer.valueOf(ReflectUtil.annotationValue(annotation, "min").toString());
                Integer max = Integer.valueOf(ReflectUtil.annotationValue(annotation, "max").toString());
                String msg = preHandleMsg(annotation, propField);
                msg = postHandleMsg(msg, "min", min);
                msg = postHandleMsg(msg, "max", max);
                if (isMin && min == 0) {
                    return null;
                }
                return new ValueModel(true, msg, isMin ? min : max);
            } catch (Exception e) {
                LogUtil.err(e);
            }
        }

        return ret;
    }

    //min-value max-value：传入DecimalMin(Max),Min(Max),Range,Positive,Negative,PositiveOrZero,NegativeOrZero,
    // PositiveInt,PositiveDecimal,PositiveMoney,PositiveRate
    public static MinMaxValueModel getMinMaxValueModel(Annotation annotation, boolean isMin, Field propField) {
        MinMaxValueModel ret = null;
        boolean allowStr = annotation instanceof PositiveInt || annotation instanceof PositiveDecimal;
        if ((!allowStr && propField.getType() == String.class) || propField.getType().isEnum() || Temporal.class.isAssignableFrom(propField.getType()) ||
                propField.getType() == boolean.class || propField.getType() == Boolean.class) {
            return ret;
        }

        Object value = null;
        String type = "";
        Object inclusive = null;

        if (isMin && annotation instanceof DecimalMin || !isMin && annotation instanceof DecimalMax) {
            type = "value";
            value = ReflectUtil.annotationValue(annotation, type);
            inclusive = ReflectUtil.annotationValue(annotation, "inclusive");
        } else if (isMin && annotation instanceof Min || !isMin && annotation instanceof Max) {
            type = "value";
            value = ReflectUtil.annotationValue(annotation, type);
            inclusive = true;
        } else if (annotation instanceof Range) {
            Range range = (Range) annotation;
            Object min = range.min();
            Object max = range.max();
            String msg = preHandleMsg(annotation, propField);
            msg = postHandleMsg(msg, "min", min);
            msg = postHandleMsg(msg, "max", max);
            return new MinMaxValueModel(true, msg, isMin ? min : max, true);
        } else if (isMin && annotation instanceof Positive) {
            inclusive = false;
            value = 0;
        } else if (!isMin && annotation instanceof Negative) {
            inclusive = false;
            value = 0;
        } else if (isMin && annotation instanceof PositiveOrZero) {
            inclusive = true;
            value = 0;
        } else if (!isMin && annotation instanceof NegativeOrZero) {
            inclusive = true;
            value = 0;
        } else if (annotation instanceof PositiveInt || annotation instanceof PositiveDecimal ||
                annotation instanceof PositiveMoney) {
            if (isMin) {
                inclusive = ReflectUtil.annotationValue(annotation, "canBeZero");
                value = 0;
            } else {//防止溢出抛异常
                value = getMinMaxValue(propField, false);
                inclusive = false;
            }
        } else if (annotation instanceof PositiveRate) {
            PositiveRate pr = (PositiveRate) annotation;
            if (isMin) {
                inclusive = pr.canBeZero();
                value = 0;
            } else {
                inclusive = true;

                value = pr.canOverOne() ? max : 100;//页面上要显示百分号
            }
        }

        if (value != null) {
            String msg = preHandleMsg(annotation, propField);
            msg = postHandleMsg(msg, type, value);
            ret = new MinMaxValueModel(true, msg, value, inclusive);
        }

        return ret;
    }

    private static int max = 100000000;
    private static int min = -100000000;

    private static Object getMinMaxValue(Field field, boolean isMin) {
        Class<?> type = field.getType();
        if (type == byte.class || type == Byte.class) {
            return isMin ? Byte.MIN_VALUE : Byte.MAX_VALUE;
        } else if (type == short.class || type == Short.class) {
            return isMin ? Short.MIN_VALUE : Short.MAX_VALUE;
        } else if (type == int.class || type == Integer.class) {
            return isMin ? Integer.max(min, Integer.MIN_VALUE) : Integer.min(max, Integer.MAX_VALUE);
        } else if (type == long.class || type == Long.class) {
            return isMin ? Long.max(min, Long.MIN_VALUE) : Long.min(max, Long.MAX_VALUE);
        } else if (type == float.class || type == Float.class) {
            return isMin ? Float.max(min, Float.MIN_VALUE) : Float.min(max, Float.MAX_VALUE);
        } else if (type == double.class || type == Double.class) {
            return isMin ? Double.max(min, Double.MIN_VALUE) : Double.min(max, Double.MAX_VALUE);
        } else if (type == BigDecimal.class) {
            return isMin ? min : max;
        } else if (type == BigInteger.class) {
            return isMin ? min : max;
        }

        return null;
    }


    //integer-num：传入Digits
    public static ValueModel getIntegerNumModel(Annotation annotation, Field propField) {
        ValueModel ret = null;

        if (annotation instanceof Digits) {
            String msg = preHandleMsg(annotation, propField);
            Object integer = ReflectUtil.annotationValue(annotation, "integer");
            msg = postHandleMsg(msg, "integer", integer);
            ret = new ValueModel(true, msg, integer);
        }

        return ret;
    }

    //fraction-num：传入Digits,PositiveDecimal,PositiveRate,PositiveMoney，特别的：当属性类型本身是byte,short,int,long等整型时，fraction-num为0
    public static ValueModel getFractionNumModel(Annotation annotation, Field propField) {
        ValueModel ret = null;
        Class<?> propClazz = propField.getType();
        Object value = null;
        String type = "";
        if (annotation instanceof Digits) {
            type = "fraction";
            value = ReflectUtil.annotationValue(annotation, type);
        } else if (annotation instanceof PositiveDecimal) {
            type = "scale";
            value = ReflectUtil.annotationValue(annotation, type);
        } else if (annotation instanceof PositiveRate) {
            value = 2;//因为页面会乘以100,所以小数位限制为2位
        } else if (annotation instanceof PositiveMoney) {
            value = 2;
        } else if (propClazz == byte.class || propClazz == short.class || propClazz == int.class || propClazz == long.class ||
                propClazz == Byte.class || propClazz == Short.class || propClazz == Integer.class || propClazz == Long.class || propClazz == BigInteger.class
                ) {
            return new ValueModel(true, "", 0);
        }

        if (value != null) {
            String msg = preHandleMsg(annotation, propField);
            msg = postHandleMsg(msg, type, value);
            ret = new ValueModel(true, msg, value);
        }

        return ret;
    }

    //reg-ex：传入Email Url Pattern
    public static RegExModel getRegExModel(Annotation annotation, Field propField) {
        RegExModel ret = null;

        String expr = null;
        String type = "";
        if (annotation instanceof Email) {
            expr = "^[\\\\w-\\\\+]+(\\\\.[\\\\w]+)*@[\\\\w-]+(\\\\.[\\\\w]+)*(\\\\.[a-z]{2,})$";
        } else if (annotation instanceof URL) {
            expr = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        } else if (annotation instanceof Pattern) {
            expr = ReflectUtil.annotationValue(annotation, "regexp").toString();
        }

        if (StringUtils.isNotBlank(expr)) {
            String msg = preHandleMsg(annotation, propField);
            ret = new RegExModel(true, msg, expr);
        }

        return ret;
    }
}
