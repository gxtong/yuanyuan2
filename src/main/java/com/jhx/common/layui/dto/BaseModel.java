package com.jhx.common.layui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

/**
 * @author 钱智慧
 * date 6/28/18 10:46 AM
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseModel {
    @JsonIgnore
    private boolean hasAnnotation;

    @JsonIgnore
    private String json;

    //错误消息
    @Setter(AccessLevel.NONE)
    private String msg ;

    public void setMsg(String msg) {
        Assert.isTrue(msg==null || (msg.indexOf('\'')<0 && msg.indexOf('\"')<0),"不能包括英文单引号或英文双引号");
        this.msg = msg;
    }

    public BaseModel(boolean hasAnnotation, String msg) {
        this.hasAnnotation = hasAnnotation;
        this.msg = msg;
    }
}
