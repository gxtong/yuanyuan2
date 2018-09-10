package com.jhx.common.util.db;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * author 钱智慧
 * date 2018/2/1 15:59
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
public class IdEntityBaseNoUpdate extends IdEntityBasePure {


    @Display("创建时间")
    @Column(columnDefinition = "datetime(3)")
    private LocalDateTime createAt = LocalDateTime.now();
}
