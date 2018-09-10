package com.jhx.common.util;

import java.io.Serializable;

/**
 * @author 钱智慧
 * date 7/2/18 5:59 PM
 */
public interface Getter<T, V> extends Serializable {
    V apply(T bean);
}
