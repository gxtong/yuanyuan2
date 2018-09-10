package com.jhx.common.util.db.migrate;

import com.jhx.common.util.db.migrate.core.MigrateCore;
import lombok.Getter;

/**
 * 数据库迁移工具类<br/>
 * 1. 新增类 - 创建表  <br/>
 * 2. 修改属性名, 修改表名 - 对应修改 <br/>
 * 3. 新增属性 - 添加到对应的表 <br/>
 * 4. 修改属性类型或约束(包括长度变更) - 尝试更改 若失败 则删除对应列并重新添加 <br/>
 * 5. 删除属性 - 删除对应列 <br/>
 * 6. 修改索引 - 对应修改 <br/>
 *
 * @author t.ch
 * @time 2018-01-24 08:42
 */
@Getter
public class DbMigrateUtil {
    public static void migrate(String url, String username, String password, String... packageNames) {
        MigrateCore core = new MigrateCore(url, username, password, packageNames);
        core.start();
    }
}
