package com.jhx.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.EmbeddedId;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * 反射工具
 */
public class ReflectUtil {

    /**
     * 反射获取对象所有属性及相应值，包括所有上级类，其中枚举取ordinal值
     * 忽略 静态属性 和 类型非BigDecimal的null值
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> getAllFields(Object bean) {
        return getAllFields(bean, null);
    }

    /**
     * 反射获取对象所有属性及相应值，包括所有上级类，其中枚举取ordinal值
     * 不忽略任何值
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> getAllFieldsForBatchAdd(Object bean) {
        Map<String, Object> ret = new HashMap<>();
        reflectAllField(bean.getClass(), field -> {
            if (ret.containsKey(field.getName())) {
                return; // 为了避免把值覆盖为null, 因为:继承如果存在同名的属性,则通过反射从父类获取的是null
            }
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (isStatic) {
                return; // 忽略静态属性
            }
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(bean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (val == null) {
                if (field.getType() == BigDecimal.class) { // BigDecimal 默认为 0
                    val = BigDecimal.ZERO;
                }
            }
            if (field.isAnnotationPresent(EmbeddedId.class)) {
                Map<String, Object> map = getAllFieldsForBatchAdd(val);
                ret.putAll(map);
                return;
            }
            if (val instanceof Enum<?>) { // 枚举对象取rodinal
                Enum<?> en = (Enum<?>) val;
                val = en.ordinal();
            }
            ret.put(field.getName(), val);
        });

        return ret;
    }

    /**
     * 反射获取对象属性及相应值，包括所有上级类，其中枚举取ordinal值
     *
     * @param targetPropList 只反射这些指定的属性
     * @return
     */
    public static Map<String, Object> getAllFields(Object bean, List<String> targetPropList) {
        Map<String, Object> ret = new HashMap<>();
        reflectAllField(bean.getClass(), field -> {
            if (ret.containsKey(field.getName())) {
                return; // 为了避免把值覆盖为null, 因为:继承如果存在同名的属性,则通过反射从父类获取的是null
            }
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (isStatic) {
                return; // 忽略静态属性
            }
            boolean isOk = targetPropList == null;
            Optional<String> item = null;
            if (!isOk) {
                item = targetPropList.stream().filter(a -> StringUtils.equalsIgnoreCase(a, field.getName()))
                        .findFirst();
                isOk = item.isPresent();
            }

            if (isOk) {
                field.setAccessible(true);
                Object val = null;
                try {
                    val = field.get(bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (val == null) {
                    if (field.getType() == BigDecimal.class) { // BigDecimal 默认为 0
                        val = BigDecimal.ZERO;
                    } else {
                        return;
                    }
                }
                if (field.isAnnotationPresent(EmbeddedId.class)) {
                    Map<String, Object> map = getAllFields(val, targetPropList);
                    ret.putAll(map);
                    return;
                }
                if (val instanceof Enum<?>) { // 枚举对象取rodinal
                    Enum<?> en = (Enum<?>) val;
                    val = en.ordinal();
                }
                ret.put((item != null && item.isPresent()) ? item.get() : field.getName(), val);
            }
        });

        return ret;
    }

    /**
     * 反射获取对象所有属性
     *
     * @param clazz 类
     */
    public static void reflectAllField(Class<?> clazz, Consumer<Field> fieldConsumer) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                fieldConsumer.accept(field);
            }
        }
    }

    /**
     * desc 反射获取对象所有属性，fn若返回false则停止循环
     *
     * @author 钱智慧
     * date 6/26/18 8:52 PM
     **/
    public static void reflectAllField(Class<?> clazz, Function<Field, Boolean> fn) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Boolean b = fn.apply(field);
                if (!b) {
                    break;
                }
            }
        }
    }

    /**
     * desc 反射获取注解指定属性的值
     *
     * @author 钱智慧
     * date 6/28/18 10:28 AM
     **/
    public static Object annotationValue(Annotation annotation, String memberName) {
        try {
            InvocationHandler h = Proxy.getInvocationHandler(annotation);
            Field hField = h.getClass().getDeclaredField("memberValues");
            hField.setAccessible(true);
            Map memberValues = (Map) hField.get(h);
            return memberValues.get(memberName);
        } catch (Exception e) {
            LogUtil.err(e);
            return null;
        }
    }

    public static Class<?> getFieldClass(Class<?> aClass, String propertyName) {
        Class<?> ret = null;

        Field field = getField(aClass, propertyName);
        if (field != null) {
            ret = field.getType();
        }

        return ret;
    }

    public static Field getField(Class<?> aClass, String propertyName) {
        final Field[] ret = {null};
        reflectAllField(aClass, field -> {
            if (field.getName().equals(propertyName)) {
                ret[0] = field;
                return false;
            }
            return true;
        });

        return ret[0];
    }

    /**
     * desc 通过反射获取值
     * @author 钱智慧
     * date 7/12/18 2:25 PM
     **/
    public static Object getValue( Object object, String propertyName) {
        Field field = getField(object.getClass(), propertyName);
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            LogUtil.err(e);
        }
        return null;
    }
}
