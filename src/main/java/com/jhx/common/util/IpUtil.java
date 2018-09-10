
package com.jhx.common.util;

import java.net.InetAddress;

public class IpUtil {
    public static String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * desc 判断是否是本地地址或内网地址，比如127.0.0.1、localhost、172.17.80.73都会返回true
     *
     * @author 钱智慧
     * date 8/1/18 10:30 AM
     **/
    public static boolean isLocal(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isAnyLocalAddress() || addr.isLoopbackAddress() || addr.isSiteLocalAddress();
        } catch (Exception e) {
            return false;
        }
    }
}
