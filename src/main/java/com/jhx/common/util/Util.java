package com.jhx.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 钱智慧
 * date 6/25/18 4:24 PM
 */
public class Util {
    private static ConcurrentHashMap<Getter<?, ?>, PropDisplayModel> displayMap = new ConcurrentHashMap<>();

    //key是class.name+"#"+propertyName
    private static ConcurrentHashMap<String, String[]> _displayMap = new ConcurrentHashMap<>();


    /**
     * desc 生成uuid字符串，不带"-"
     *
     * @author 钱智慧
     * date 4/9/18 12:50 PM
     **/
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 谨慎调用，该方法只用于测试，不要在生产环境中使用
     * desc 让调用线程等待一段时间，单位毫秒，Thread.sleep只能传递>=1毫秒的时间
     *
     * @author 钱智慧
     * date 4/25/18 3:47 PM
     **/
    public static void waitFor(long nanoInterval) {
        long start = System.nanoTime();
        long end = 0;
        do {
            end = System.nanoTime();
        } while (start + nanoInterval >= end);
    }

    /**
     * desc 格式化boolean类型数据
     *
     * @param trueDisplay  为true时显示的文字
     * @param falseDisplay 为false时显示的文字
     * @author 钱智慧
     * date 7/4/18 2:40 PM
     **/
    public static String format(boolean value, String trueDisplay, String falseDisplay) {
        if (value) {
            return trueDisplay;
        } else {
            return falseDisplay;
        }
    }

    /**
     * desc 以百分比的形式输出，输出形式示例：0.2354=>23.54%，0.1=>10.00%,0.12359=>12.36%，若超过了
     * 4位小数，则为四舍五入
     *
     * @author 钱智慧
     * date 7/4/18 9:34 AM
     **/
    public static String rate(BigDecimal value) {
        if (value == null || value.signum() == 0) {
            return "0.00%";
        }
        BigDecimal v = value.multiply(BigDecimal.valueOf(100));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(v) + "%";
    }

    /**
     * desc 格式化金额，示例：1=>1.00,1.0=>1.00，0=>0.00,1.21=>1.21
     * 注：一般用于动态控制的小数（如定值定比控制下的小数有时是金额有时是百分数）在列表的显示格式化，其他情况调用format即可
     *
     * @author 钱智慧
     * date 7/4/18 9:23 AM
     **/
    public static String money(BigDecimal money) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(money);
    }

    /**
     * desc 功能包括：格式化时间、枚举、boolean、金额、普通的BigDecimal去掉尾部多余的0、格式化float、double
     *
     * @author 钱智慧
     * date 7/4/18 2:37 PM
     **/
    public static String format(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof DayOfWeek) {
            return Constant.DayOfWeekMap.get(obj);
        }

        if(obj instanceof Double ){
            return format(BigDecimal.valueOf((double)obj));
        }

        if(obj instanceof Float){
            return format(BigDecimal.valueOf((float)obj));
        }

        if (obj instanceof BigDecimal) {
            BigDecimal value = (BigDecimal) obj;
            if (value.scale() == 2) {
                return money(value);
            } else {
                return value.stripTrailingZeros().toPlainString();
            }
        }

        if (obj instanceof LocalDateTime) {
            return ((LocalDateTime) obj).format(Constant.YMDHMS);
        }

        if (obj instanceof LocalDate) {
            return ((LocalDate) obj).format(Constant.YMD);
        }

        if (obj instanceof LocalTime) {
            return ((LocalTime) obj).format(Constant.HMS);
        }

        if (obj instanceof Enum) {
            return EnumUtil.getDisplay(obj);
        }

        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (b) {
                return "是";
            } else {
                return "否";
            }
        }

        if (obj instanceof List) {
            List b = (List) obj;
            return StringUtils.join(b, ",");
        }

        return obj.toString();
    }

    /**
     * desc 格式化金额字段
     *
     * @param zeroDisplay 为零时显示的字符
     * @author 钱智慧
     * date 7/4/18 6:01 PM
     **/
    public static String format(BigDecimal money, String zeroDisplay) {
        if (money == null) {
            throw new RuntimeException("金额不能为空");
        }

        if (money.equals(BigDecimal.ZERO)) {
            return zeroDisplay;
        } else {
            return money(money);
        }
    }

    /**
     * temporal只能是LcoalDateTime LocaDate LocalTime中的一种
     * desc 格式化时间，可以指定格式，格式示例：yyyy-MM-dd、HH:mm:ss以及其他时间格式
     *
     * @author 钱智慧
     * date 7/4/18 2:38 PM
     **/
    public static String format(Temporal temporal, String fmt) {
        if (temporal == null) {
            return "";
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);

        if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).format(formatter);
        } else if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).format(formatter);
        } else if (temporal instanceof LocalTime) {
            return ((LocalTime) temporal).format(formatter);
        } else {
            return "";
        }
    }

    /**
     * desc 传入lambda getter方法，返回对应属性上的Display注解的值
     *
     * @author 钱智慧
     * date 6/26/18 10:31 AM
     **/
    public static <T, V> String display(Getter<T, V> getter) {
        return displayMap.computeIfAbsent(getter, k -> {
            SerializedLambda lambda = getLambda(getter);
            PropDisplayModel ret = display(lambda);
            return ret;
        }).display;
    }

    /**
     * desc 传入lambda getter方法，返回对应属性名称
     *
     * @author 钱智慧
     * date 6/26/18 10:31 AM
     **/
    public static <T, V> String prop(Getter<T, V> getter) {
        return displayMap.computeIfAbsent(getter, k -> {
            SerializedLambda lambda = getLambda(getter);
            PropDisplayModel ret = display(lambda);
            return ret;
        }).prop;
    }

    public static Map<Boolean, String> displayBoolMap(Class<?> clazz, String propertyName) {
        Map<Boolean, String> map = new HashMap<>();
        String[] arr = displayArr(clazz, propertyName);
        map.put(true, arr[1]);
        map.put(false, arr[2]);
        return map;
    }

    public static String display(Class<?> clazz, String propertyName) {
        return displayArr(clazz, propertyName)[0];
    }

    public static <T, V> String[] displayArr(Class<?> clazz, String propertyName) {
        return _displayMap.computeIfAbsent(clazz.getName() + "#" + propertyName, k -> {
            final String[] ret = {propertyName, "", ""};

            ReflectUtil.reflectAllField(clazz, field -> {
                if (field.getName().equals(propertyName)) {
                    for (Annotation annotation : field.getAnnotations()) {
                        if (annotation instanceof Display) {
                            Display display = (Display) annotation;
                            ret[0] = display.value();
                            ret[1] = display.trueDisplay();
                            ret[2] = display.falseDisplay();
                        }
                    }
                    return false;
                }
                return true;
            });

            return ret;
        });
    }

    private static PropDisplayModel display(SerializedLambda lambda) {
        try {
            Class<?> clazz = Class.forName(lambda.getImplClass().replace('/', '.'));
            String methodName = lambda.getImplMethodName();
            String filedName = Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (StringUtils.equalsIgnoreCase(filedName, field.getName())) {
                        Display display = field.getAnnotation(Display.class);
                        if (display != null) {
                            return new PropDisplayModel(field.getName(), display.value(), display.trueDisplay(), display.falseDisplay());
                        } else {
                            return new PropDisplayModel(field.getName(), field.getName(), "", "");
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

        } catch (Exception e) {
            LogUtil.err(e);
        }
        return null;
    }

    private static <T, V> SerializedLambda getLambda(Getter<T, V> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(getter);
            return serializedLambda;
        } catch (Exception e) {
            LogUtil.err(e);
        }

        return null;
    }

    @AllArgsConstructor
    private static class PropDisplayModel {
        public String prop;
        public String display;
        public String trueDisplay;
        public String falseDisplay;
    }

    public static String toJson(Object object) {
        try {
            return Constant.JacksonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LogUtil.err("JSON转换错误", e);
        }
        return "";
    }

    /**
     * desc 判断两种Class是否是同一种类型，注意 isSameType(int.class,Integer.class)返回true
     * @author 钱智慧
     * date 8/15/18 5:32 PM
     **/
    public static boolean isSameType(Class<?> left, Class<?> right) {
        Class<?> tc = left.isPrimitive() ? Util.wrap(left) : left;
        Class<?> fc = right.isPrimitive() ? Util.wrap(right) : right;
        return tc == fc;
    }

    /**
     * desc 获取基本类型class的包装类class，比如:Integer clazz=wrap(int.class);
     *
     * @author 钱智慧
     * date 8/15/18 5:27 PM
     **/
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> c) {
        return c.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get(c) : c;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS
            = new ImmutableMap.Builder<Class<?>, Class<?>>()
            .put(boolean.class, Boolean.class)
            .put(byte.class, Byte.class)
            .put(char.class, Character.class)
            .put(double.class, Double.class)
            .put(float.class, Float.class)
            .put(int.class, Integer.class)
            .put(long.class, Long.class)
            .put(short.class, Short.class)
            .put(void.class, Void.class)
            .build();
}
