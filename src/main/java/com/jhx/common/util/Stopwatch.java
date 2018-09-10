package com.jhx.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用于统计代码执行时间的工具类
 *
 * @author 钱智慧
 * date 4/10/18 3:42 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Stopwatch {
    private String beginMsg;

    private String endMsg;

    //纳秒
    private long ns;

    private Stopwatch(String beginMsg, long ns) {
        this.beginMsg = beginMsg;
        this.ns = ns;

        LogUtil.info("begin：" + beginMsg);
    }

    private Stopwatch(long ns) {
        this.ns = ns;
    }

    public static Stopwatch startNew(String beginMsg) {
        return new Stopwatch(beginMsg, System.nanoTime());
    }

    public void restart(String beginMsg){
        this.beginMsg = beginMsg;
        this.ns = System.nanoTime();

        LogUtil.info("begin：" + beginMsg);
    }

    public void restart(){
        this.ns = System.nanoTime();
    }


    public static Stopwatch startNew() {
        return new Stopwatch(System.nanoTime());
    }

    public void end(String endMsg) {
        long l = System.nanoTime() - ns;
        float ms = l / 1000000f;
        LogUtil.info(String.format("end："+endMsg+"【%s ms,%s ns】" , ms, l));
    }

    public void end() {
        long l = System.nanoTime() - ns;
        float ms = l / 1000000f;
        LogUtil.info(String.format("end：【%s ms,%s ns】", ms, l));
    }
}
