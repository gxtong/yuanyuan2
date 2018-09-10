package com.jhx.common.util;

import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 线程安全的BigDecimal封装，可用于多线程运算中，和BigDecimal不同，SafeDecimal是一个可变对象
 * author 钱智慧
 * date 2017/12/19 16:17
 */
@Accessors(chain = true)
public class SafeDecimal {
    private AtomicReference<BigDecimal> num;

    public BigDecimal value(){
        return num.get();
    }

    public SafeDecimal() {
        num = new AtomicReference<>(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return num.toString();
    }

    public void add(BigDecimal augend) {
        while (true){
            BigDecimal oldValue=num.get();
            if(num.compareAndSet(oldValue,oldValue.add(augend))){
                return;
            }
        }
    }

    public void subtract(BigDecimal subtrahend) {
        while (true){
            BigDecimal oldValue=num.get();
            if(num.compareAndSet(oldValue,oldValue.subtract(subtrahend))){
                return;
            }
        }
    }

    public void multiply(BigDecimal multiplicand) {
        while (true){
            BigDecimal oldValue=num.get();
            if(num.compareAndSet(oldValue,oldValue.multiply(multiplicand))){
                return;
            }
        }
    }

    public void divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        while (true){
            BigDecimal oldValue=num.get();
            if(num.compareAndSet(oldValue,oldValue.divide(divisor,scale,roundingMode))){
                return;
            }
        }
    }
}
