package com.jhx.common.util.db.migrate.model;

import com.jhx.common.util.db.migrate.annotation.Migrate;
import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 迁移日志
 *
 * @author t.ch
 * @date 2018-03-23 14:43
 */
@Getter
@Setter
@Entity
public class MigrateLog{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Display("SQL语句")
    @Migrate(oldName = "migrateSql")
    @Column(columnDefinition = "text")
    private String sql;

    @Display("是否成功")
    private boolean success;

    @Display("创建时间")
    private LocalDateTime createAt = LocalDateTime.now();

}
