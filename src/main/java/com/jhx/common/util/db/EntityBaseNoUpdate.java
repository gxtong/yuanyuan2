package com.jhx.common.util.db;

import com.jhx.common.util.StrUtil;
import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author 钱智慧
 * date 2018/2/1 16:01
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
public class EntityBaseNoUpdate implements Serializable {


    /**
     * 创建时间
     */
    @Display("创建时间")
    @Column(columnDefinition = "datetime(3)")
    private LocalDateTime createAt = LocalDateTime.now();

    @Override
    public String toString() {
        return StrUtil.toStr(this);
    }
}
