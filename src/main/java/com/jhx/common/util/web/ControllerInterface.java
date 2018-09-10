package com.jhx.common.util.web;


import org.springframework.util.StringUtils;

/**
 * author 钱智慧
 * date 2018/1/19 15:06
 */
public interface ControllerInterface {
    default public String view() {
        String actionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String controllerName = StringUtils.uncapitalize(StringUtils.replace(this.getClass().getSimpleName(), "Controller", ""));
        return view(controllerName + "/" + actionName);
    }

    default public String view(String view) {
        return view;
    }

    default public String error() {
        return "error/error";
    }

    default public String redirect(String url) {
        url = url.startsWith("/") ? url : new StringBuilder("/").append(url).toString();
        return new StringBuilder("redirect:").append("url").toString();
    }
}
