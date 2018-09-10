package com.jhx.common.util;

import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket客户端类，封装了心跳、掉线重连等功能
 *
 * @author 钱智慧
 * date 4/18/18 5:58 PM
 */
public class MyWebSocketClient {
    private WebSocketClient client;
    private String serverIpPort;
    private String serverInfo;
    private int connectionStatus; // 状态,0未连接,1连接中,2连接成功

    @Getter
    private LocalDateTime lastHeartbeatAt;

    @Getter
    private LocalDateTime lastReceiveAt;

    private List<IMyWebSocketMsgListener> listenerList = new ArrayList<>();

    public MyWebSocketClient(String serverIpPort, String serverName, IMyWebSocketMsgListener msgListener) {
        this.serverIpPort = serverIpPort;
        serverInfo = String.format("【%s,%s】", serverName, serverIpPort);
        listenerList.add(msgListener);
    }

    public void addMsgListener(IMyWebSocketMsgListener msgListener) {
        listenerList.add(msgListener);
    }

    //心跳检查连接，心跳发送的是空字符串，接收端要注意特别处理
    private void startBeat() throws URISyntaxException {
        ThreadUtil.start(() -> {
            while (true) {
                try {
                    if (client == null || client.isClosed() || client.isClosing()) {
                        connect();
                    } else if (client.isOpen()) {
                        try {
                            client.send("");
                            lastHeartbeatAt = LocalDateTime.now();
                        } catch (Exception e) {
                            LogUtil.err(e);
                            connect();
                        }
                    }
                    Thread.sleep(20000);
                } catch (Exception e) {
                    LogUtil.err(e);
                }
            }
        });
    }

    private void connect() {
        try {
            if (connectionStatus == 0) {
                connectionStatus = 1;
                LogUtil.err(String.format("开始连接%s……", serverInfo));
            }
            client = create();
            client.connect();
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    private WebSocketClient create() throws URISyntaxException {
        return new WebSocketClient(new URI("ws://" + serverIpPort)) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                connectionStatus = 2;
                LogUtil.err(String.format("成功连接上%s", serverInfo));
                for (IMyWebSocketMsgListener listener : listenerList) {
                    listener.onOpen();
                }
            }

            @Override
            public void onMessage(String message) {
                try {
                    for (IMyWebSocketMsgListener listener : listenerList) {
                        listener.onMessage(message);
                    }
                    lastReceiveAt = LocalDateTime.now();
                } catch (Exception e) {
                    LogUtil.err(e);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (connectionStatus == 2) {
                    LogUtil.err(String.format("和%s断开连接,reason:%s,remote:%s", serverInfo, reason, remote));
                    connectionStatus = 0;
                }
            }

            @Override
            public void onError(Exception ex) {
                if (connectionStatus == 2) {
                    LogUtil.err(String.format("和：%s连接出错", serverInfo));
                    LogUtil.err(ex);
                    connectionStatus = 0;
                }
            }
        };
    }

    public void start() throws URISyntaxException {
        connect();
        startBeat();
    }
}
