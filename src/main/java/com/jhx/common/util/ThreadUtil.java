package com.jhx.common.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author 钱智慧
 * date 2018/1/10 19:14
 */
@Getter
@Setter
@Accessors(chain = true)
public class ThreadUtil {
    /**
     * desc 将传入lambda方法用新创建的后台线程来运行
     * author 钱智慧
     * date 2018/1/10 19:16
     **/
    public static Thread start(Runnable lambdaMethod){
        Thread th = new Thread(lambdaMethod);
        th.setDaemon(true);
        th.start();
        return th;
    }
}
