package com.jhx.common.util.db;

import com.jhx.common.util.Getter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public abstract class SqlHelper<T> extends NamedParameterJdbcDaoSupport {

    public final static char QuoteMark = '`';

    protected List<String> propList = new ArrayList<>();

    protected String tableName;

    protected Class<T> persistentClass;

    /**
     * 条件拼接,忽略掉  {@code isIgnore=true } 的情况<br>
     * <p>使用示例</p>
     * <p>
     * <pre>
     * String srcSql = "select * from customer";
     * String conditionSql = "userName=:userName";
     * String inputValue = "jack";
     * boolean isIgnore = StringUtils.isBlank(inputValue); // false
     * Map<String,Object> argMap = new HashMap<>();
     * srcSql = SqlHelper.conditionSql(inputValue,isIgnore,argMap,srcSql,conditionSql);
     * System.out.println(srcSql); // 输出  select * from customer where userName=:userName
     * // 改变参数
     * inputValue = "";
     * conditionSql = "realName like :realName";
     * isIgnore = StringUtils.isBlank(inputValue); // true
     * srcSql = SqlHelper.conditionSql(inputValue,isIgnore,argMap,srcSql,conditionSql);
     * System.out.println(srcSql); // 输出  select * from customer where userName=:userName
     * </pre>
     *
     * @param inputValue   参数值, 若等于null, 则忽略, 如果为枚举类型则取 {@link Enum#ordinal()}
     * @param isIgnore     是否忽略该参数
     * @param argMap       参数Map , 不能为null
     * @param srcSql       原SQL
     * @param conditionSql 条件SQL
     * @LastChanger t.ch
     */
    public static String conditionSql(Object inputValue, boolean isIgnore, Map<String, Object> argMap, String srcSql, String conditionSql) {

        String ret = srcSql;
        if (isIgnore || inputValue == null) {
            ret = srcSql;
        } else {
            ret = conditionSql(srcSql, conditionSql);
            Pattern pattern = Pattern.compile(":(\\w+)");
            Matcher matcher = pattern.matcher(conditionSql);
            if (matcher.find()) {
                String name = matcher.group(1);
                if (inputValue instanceof Enum<?>) { // 枚举对象取rodinal
                    Enum<?> en = (Enum<?>) inputValue;
                    argMap.put(name, en.ordinal());
                } else {
                    argMap.put(name, inputValue);
                }
            }
        }
        return ret;
    }

    private static String conditionSql(String srcSql, String conditionSql) {
        String ret = "";

        if (StringUtils.containsIgnoreCase(srcSql, "where")) {
            ret = srcSql + " and " + conditionSql;
        } else {
            if (StringUtils.containsIgnoreCase(conditionSql, "where")) {
                ret = srcSql + " " + conditionSql;
            } else {
                ret = srcSql + " where " + conditionSql;
            }
        }
        return ret;
    }

    protected NamedParameterJdbcTemplate getJt() {
        return getNamedParameterJdbcTemplate();
    }

    /**
     * 返回值中的sql示例：set name=:name,money=:money
     *
     * @param bean
     * @param updateColumns
     * @return
     */
    protected abstract PrepareSqlParamResult prepareSqlParamForUpdate(Object bean, List<String> updateColumns);

    /**
     * 返回的sql示例：(name,money) values (:name,:money)
     *
     * @param bean
     * @return
     */
    protected abstract PrepareSqlParamResult prepareSqlParamForAdd(Object bean);

    /**
     * 判断符合条件的记录是否存在
     *
     * @param conditionSql 示例："userName=:userName"
     * @param argMap       示例：{userName,"jack"}
     * @return
     */
    public abstract boolean exist(String conditionSql, Map<String, Object> argMap);

    /**
     * 执行原生sql进行db修改
     *
     * @param sql
     * @param argMap
     */
    public void doSql(String sql, Map<String, Object> argMap) {
        argMap = changeEnum(argMap);
        getJt().update(sql, argMap);
    }

    /**
     * 执行自定义数据绑定的查询，慎用，无分页
     *
     * @param sql    SQL语句
     * @param argMap 参数
     * @param clazz  类
     * @return 没有数据则返回空列表
     */
    public <E> List<E> query(String sql, Map<String, Object> argMap, Class<E> clazz) {
        @SuppressWarnings({"unchecked", "rawtypes"}) List<E> list = getJt().query(sql, argMap, new BeanPropertyRowMapper(clazz));
        return list == null ? new ArrayList<E>() : list;
    }

    /**
     * 执行自定义数据绑定的查询，带分页
     *
     * @param pageBean
     * @param sql
     * @param argMap
     * @param clazz
     * @return
     * @author 钱智慧
     * @date 2017年9月13日 下午5:21:39
     */
    public abstract <E> PageBean<E> query(PageBean<E> pageBean, String sql, Map<String, Object> argMap, Class<E> clazz);

    /**
     * 按传入的条件进行更新，针对那些直接继承自EntityBase的实现，{@link com.jhx.common.util.db.IdEntityBase}
     *
     * @param data
     * @param conditionSql
     * @param conditionArgMap
     * @param updateColumns
     * @author 钱智慧
     * @date 2017年9月2日 下午3:04:43
     */
    public abstract <Entity> void update(Entity data, String conditionSql, Map<String, Object> conditionArgMap, String... updateColumns);

    /**
     * 按id更新，针对那些直接继承自IdEntityBase的实体，{@link com.jhx.common.util.db.EntityBase}
     *
     * @param data
     * @param updateColumns˙
     * @author 钱智慧
     * @date 2017年9月2日 下午2:58:49
     */
    public abstract <IdEntity extends IdEntityBasePure> void updateById(IdEntity data, String... updateColumns);

    public abstract <IdEntity extends IdEntityBasePure, V> void updateById(IdEntity data, Getter<IdEntity, V>... getters);

    /**
     * 按主键id删除
     *
     * @param id
     * @author 钱智慧
     * @date 2017年9月2日 下午3:07:31
     */
    public abstract void deleteById(int id);

    /**
     * 条件删除
     * 使用示例：
     * <pre>
     * 	Map<String, Object> param = ImmutableMap.of("ids", Lists.newArrayList(2, 3, 4), "money", 0);
     * 	userDao.delete("id in (:ids) and money>=:money", param);
     * </pre>
     *
     * @param conditionSql
     * @param conditionArgMap
     */
    public abstract void delete(String conditionSql, Map<String, Object> conditionArgMap);

    /**
     * 通过id获取数据
     *
     * @param id
     * @return
     */
    public abstract T getById(int id);

    /**
     * 为了更新而获取，会加上行级锁，调用此方法时请务必要开启事务
     *
     * @param id
     * @return
     */
    public abstract T getByIdForUpdate(int id);

    /**
     * 查找第一条数据，返回第一个被添加进去的记录
     *
     * @return
     */
    public abstract T get();

    /**
     * 查找第一条数据，返回第一个被添加进去的记录
     * 锁住数据直到事务提交
     *
     * @return
     */
    public abstract T getForUpdate();

    public abstract T getForUpdate(String conditionSql, Map<String, Object> argMap);

    /**
     * 按指定条件查找第一条数据
     *
     * @param sql
     * @param argMap
     * @return
     */
    public abstract T get(String sql, Map<String, Object> argMap);

    /**
     * 添加数据：针对主键为id的数据，返回的主键会赋值到实体中
     *
     * @param t
     */
    public abstract <IdEntity extends IdEntityBasePure> void add(IdEntity t);

    /**
     * 批量添加数据，注意sql连接串上需要打开设置：rewriteBatchedStatements=true，否则不起作用
     * DbUtil.transaction(transactionManager, () -> {
     * int p = helper.addBatch(list);
     * return true;
     * });
     */
    public abstract <Entity> int addBatch(Collection<Entity> entities);

    /**
     * 列出所有记录，无分页，慎用
     *
     * @return
     */
    public abstract List<T> list();

    /**
     * 检索所有记录，无分页，慎用
     *
     * @param sql
     * @param argMap
     * @return
     */
    public abstract List<T> list(String sql, Map<String, Object> argMap);

    /**
     * 添加数据：针对主键不是id的数据，一般用于添加中间表数据
     *
     * @param t
     */
    public abstract <Entity> void add(Entity t);

    /**
     * desc 将查询到的单行数据，以map的形式返回，比如"select sum(money) as totalMoney from table1"，
     * 返回的Map键值为totalMoney,值为对应的数值。若查询的结果集为空，则Map的size为0，若不只一条记录，
     * 则会抛异常，比如"select name,age from table1"，所以一定要确保返回的结果集只有一行
     *
     * @author 钱智慧
     * date 2018/4/6 上午10:51
     **/
    public Map<String, Object> getSimple(String sql, Map<String, Object> argMap) {
        argMap = changeEnum(argMap);
        Map<String, Object> ret;
        try {
            ret = getJt().queryForMap(sql, argMap);
        } catch (EmptyResultDataAccessException e) {
            ret = new HashMap<String, Object>();
        }
        return ret;
    }

    public abstract int getCount(String sql, Map<String, Object> argMap);

    /**
     * desc 将查询参数argMap中的枚举转成ordinal
     * author 钱智慧
     * date 2017/12/14 10:19
     **/
    protected static Map<String, Object> changeEnum(Map<String, Object> argMap) {
        Map<String, Object> ret = null;

        if (argMap != null) {
            ret = new HashMap<>();
            for (String k : argMap.keySet()) {
                Object obj = argMap.get(k);
                if (obj.getClass().isEnum()) {
                    ret.put(k, ((Enum) obj).ordinal());
                } else {
                    ret.put(k, obj);
                }
            }
        }

        if (ret == null) {
            ret = argMap;
        }

        return ret;
    }

    /**
     * 分页查询
     *
     * @param pageBean
     * @param sql
     * @param argMap
     * @return
     * @author 钱智慧
     * @date 2017年9月2日 下午3:25:29
     */
    public abstract PageBean<T> list(PageBean<T> pageBean, String sql, Map<String, Object> argMap);

    /**
     * desc 分页查询
     *
     * @author 钱智慧
     * date 2018/6/30 下午12:17
     **/
    public abstract PageBean<T> list(PageBean<T> pageBean, String sql);

    @Data
    protected static class PrepareSqlParamResult {
        protected String sql;
        protected MapSqlParameterSource map;
    }

    /**
     * 给字段两边加上`号
     *
     * @author t.ch
     */
    protected String parseName(String name) {
        return QuoteMark + name + QuoteMark;
    }
}
