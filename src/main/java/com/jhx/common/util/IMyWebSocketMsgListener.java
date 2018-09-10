package com.jhx.common.util;

/**
 * @author 钱智慧
 * date 4/18/18 6:31 PM
 */
public interface IMyWebSocketMsgListener {
    void onMessage(String message);

    default void onOpen(){}
}
