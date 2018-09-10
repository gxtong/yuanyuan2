package com.jhx.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.function.Supplier;

/**
 * 打印log日志
 */
public class LogUtil {
    public static boolean IsInfoLevel;

    static {
        String s = AppPropUtil.get("logging.level.root");
        IsInfoLevel = StringUtils.isBlank(s) || "info".equals(s);
    }

    /**
     * 传入类名，打印Throwable异常信息
     *
     * @param clazz
     * @param e
     */
    public static void err(Class<?> clazz, Throwable e) {
        Logger.getLogger(clazz).error(e.getMessage(), e);
    }

    /**
     * desc 等价于：err(String.format(format, args))
     * @author 钱智慧
     * date 7/25/18 4:34 PM
     **/
    public static void err(String format, Object... args) {
        err(String.format(format, args));
    }

    /**
     * desc 等价于：info(String.format(format, args))
     * @author 钱智慧
     * date 7/25/18 4:34 PM
     **/
    public static void info(String format, Object... args) {
        info(String.format(format, args));
    }


    /**
     * 传入类名，打印自定义信息
     *
     * @param clazz
     * @param msg
     */
    public static void err(Class<?> clazz, String msg) {
        Logger.getLogger(clazz).error(msg);
    }

    /**
     * 打印Throwable异常信息
     *
     * @param e
     */
    public static void err(Throwable e) {
        Logger.getLogger(new CurrentClassGetter().getCurrentClass()).error(e.getMessage(), e);
    }


    private static class CurrentClassGetter extends SecurityManager {
        public Class getCurrentClass() {
            Class[] arr = getClassContext();
            int i=2;
            for (i = 2; i <arr.length ; i++) {
                if(arr[i]!=LogUtil.class){
                    break;
                }
            }
            return arr[i];
        }
    }

    /**
     * 打印自定义信息
     *
     * @param msg
     */
    public static void err(String msg) {
        Logger.getLogger(new CurrentClassGetter().getCurrentClass()).error(msg);
    }

    public static void err(String msg, Throwable e) {
        Logger.getLogger(new CurrentClassGetter().getCurrentClass()).error(msg, e);
    }

    /**
     * 打印消息并终止程序
     *
     * @param msg
     */
    public static void errThenExit(String msg) {
        err(msg);
        System.exit(0);
    }

    /**
     * 打印异常并终止程序
     *
     * @param throwable
     */
    public static void errThenExit(Throwable throwable) {
        err(throwable);
        System.exit(0);
    }

    public static void errThenExit(String msg, Throwable e) {
        err(msg, e);
        System.exit(0);
    }

    /**
     * 传入类名，打印Throwable异常信息
     *
     * @param clazz
     * @param e
     */
    public static void info(Class<?> clazz, Throwable e) {
        if (IsInfoLevel) {
            Logger.getLogger(clazz).info(e.getMessage(), e);
        }
    }

    public static void info(String msg, Throwable e) {
        if (IsInfoLevel) {
            Logger.getLogger(new CurrentClassGetter().getCurrentClass()).info(msg, e);
        }
    }

    /**
     * 传入类名，打印自定义信息
     *
     * @param clazz
     * @param msg
     */
    public static void info(Class<?> clazz, String msg) {
        if (IsInfoLevel) {
            Logger.getLogger(clazz).info(msg);
        }
    }

    /**
     * 打印Throwable异常信息
     *
     * @param e
     */
    public static void info(Throwable e) {
        if (IsInfoLevel) {
            Logger.getLogger(new CurrentClassGetter().getCurrentClass()).info(e.getMessage(), e);
        }
    }

    /**
     * 打印自定义信息
     *
     * @param msg
     */
    public static void info(String msg) {
        if (IsInfoLevel) {
            Logger.getLogger(new CurrentClassGetter().getCurrentClass()).info(msg);
        }
    }

    /**
     * desc 打印传入的lambda表达式结果
     *
     * @author 钱智慧
     * date 2018/4/24 下午11:08
     **/
    public static void info(Supplier<String> function) {
        if (IsInfoLevel) {
            info(function.get());
        }
    }
}
