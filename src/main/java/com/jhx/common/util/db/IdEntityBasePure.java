package com.jhx.common.util.db;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 钱智慧
 * date 4/8/18 6:57 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class IdEntityBasePure  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 0 , message = "[{display}]参数非法")
    @Max(value = Integer.MAX_VALUE , message = "[{display}]参数非法")
    @Display("编号")
    private int id;
}
