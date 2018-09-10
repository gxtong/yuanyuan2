package com.jhx.common.util;

import com.jhx.common.util.validate.Display;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 钱智慧
 * @date 2017年9月22日 下午7:21:46
 */
public class EnumUtil {
    public static<T extends Enum> String toOrdinals(List<T> list) {
        StringBuilder ret = new StringBuilder();

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Enum anEnum = list.get(i);
                ret.append(anEnum.ordinal());
                if (i < list.size() - 1) {
                    ret.append(',');
                }
            }
        }

        return ret.toString();
    }

    /**
     * 将逗号分隔的枚举名称字符串转成枚举display字符串
     * @param nameStr
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static<T extends Enum> String toDisplays(Class<T> enumClazz,String nameStr) {
        StringBuilder sb=new StringBuilder();
        if (StringUtils.isNotBlank(nameStr)) {
            String[] split = StringUtils.split(nameStr, ',');
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                T anEnum = (T) T.valueOf(enumClazz, s);
                String display = getDisplay(anEnum);
                sb.append(display);
                if(i<split.length-1){
                    sb.append(',');
                }
            }
        }
        return sb.toString();
    }

    public static<T extends Enum> String toNames(List<T> list) {
        StringBuilder ret = new StringBuilder();

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Enum anEnum = list.get(i);
                ret.append(anEnum.name());
                if (i < list.size() - 1) {
                    ret.append(',');
                }
            }
        }

        return ret.toString();
    }

    /**
     * 将逗号分隔的字符串转成枚举列表
     * @param nameStr
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static<T extends Enum> List<T> fromNames(Class<T> enumClazz,String nameStr) {
        List<T> ret = new ArrayList<>();

        if (StringUtils.isNotBlank(nameStr)) {
            for (String s : StringUtils.split(nameStr, ',')) {
                T anEnum = (T)T.valueOf(enumClazz, s);
                ret.add(anEnum);
            }
        }

        return ret;
    }

    public static <T> String getDisplay(T enumValue) {
        String name = ((Enum<?>) enumValue).name();
        for (Field field : enumValue.getClass().getFields()) {
            if (name.equals(field.getName())) {
                Display display = field.getAnnotation(Display.class);
                return display==null?name:display.value();
            }
        }

        return name;
    }

    /**
     * author 钱智慧
     * date 2017/12/21 19:24
     *
     * @return key是枚举变量值（不是ordinal），value是Display注解的值
     **/
    public static Map<String, String> getDisplayMap(Class<?> enumClass) {
        Map<String, String> ret = new HashMap<>();

        for (Field field : enumClass.getFields()) {
            Display display = field.getAnnotation(Display.class);
            ret.put(field.getName(), display.value());
        }
        return ret;
    }

    public static <T> Map<String, String> getDisplayMap(Class<?> enumClass, List<T> ignoreList) {
        Map<String, String> ret = new HashMap<>();

        List<String> ignoreNames = new ArrayList<>();
        ignoreList.forEach(v -> {
            String name = ((Enum<?>) v).name();
            ignoreNames.add(name);
        });

        for (Field field : enumClass.getFields()) {
            Display display = field.getAnnotation(Display.class);
            if (!ignoreNames.contains(field.getName())) {
                ret.put(field.getName(), display.value());
            }
        }
        return ret;
    }
}
