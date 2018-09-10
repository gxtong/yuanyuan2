package com.jhx.common.util.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jhx.common.util.Constant;
import com.jhx.common.util.LogUtil;
import com.jhx.common.util.dto.Result;
import com.jhx.common.util.validate.Display;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * SPRING MVC 异常统一处理<br>
 * 顺序是从上到下
 *
 * @author t.ch
 * @time 2017-09-05 11:47:58
 */

@ControllerAdvice
public class WebExceptionHandler {

    /**
     * 参数验证异常
     *
     * @param bindException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result handleBindException(BindException bindException) {
        Object target = bindException.getTarget();
        FieldError fieldError = bindException.getFieldError();
        String message = fieldError.getDefaultMessage();
        if (StringUtils.isNotBlank(message) && message.contains("{display}")) {
            try {
                String property = fieldError.getField();
                Field field = target.getClass().getDeclaredField(property);
                Display displayAnnotation = field.getDeclaredAnnotation(Display.class);
                if (displayAnnotation != null) {
                    String display = displayAnnotation.value();
                    message = StringUtils.replace(message, "{display}", display);
                } else {
                    message = StringUtils.replace(message, "{display}", property);
                }
            } catch (NoSuchFieldException | SecurityException e1) {
                LogUtil.err(target.getClass(), e1); // 记录日志
            }
        }
        Result result = new Result();
        result.setMsg(message);
        return result;
    }

    /**
     * 请求方式异常
     * @param err
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException err){
        String msg = "请求方式错误：不支持["+err.getMethod()+"]请求";
        LogUtil.err(msg); // 记录日志
        return new Result(false,msg);
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException err){
        String msg = "必填项不能为空 : 参数["+err.getParameterName()+"], 类型["+err.getParameterType()+"]";
        LogUtil.err(msg); // 记录日志
        return new Result(false, msg);
    }


    @Autowired
    private HttpServletRequest request;

    /**
     * 总异常处理
     *
     * @param err
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception err) {
        try {
            LogUtil.err(Constant.JacksonMapper.writeValueAsString(request.getParameterMap()));
        } catch (JsonProcessingException ignore) {
        }
        LogUtil.err(err); // 记录日志
        return new Result(false,"系统异常:"+err.getMessage());
    }
}
