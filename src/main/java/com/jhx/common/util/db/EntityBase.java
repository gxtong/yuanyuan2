package com.jhx.common.util.db;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
public class EntityBase extends EntityBaseNoUpdate {

    /**
     * 修改时间
     */
    @Display("修改时间")
    @Column(columnDefinition = "datetime(3)")
    private LocalDateTime updateAt = LocalDateTime.now();

}
