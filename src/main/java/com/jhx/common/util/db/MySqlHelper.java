package com.jhx.common.util.db;

import com.google.common.collect.ImmutableMap;
import com.jhx.common.util.Getter;
import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.Util;
import com.jhx.common.util.dist.trade.common.entity.B2bOrderBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 钱智慧
 * @date 2017年9月2日 下午2:36:43
 */
@SuppressWarnings("unchecked")
public class MySqlHelper<T> extends SqlHelper<T> {

    public MySqlHelper(Class<T> persistentClass, JdbcTemplate jt) {
        tableName = persistentClass.getSimpleName();
        this.persistentClass = persistentClass;
        propList.addAll(getPropList(persistentClass));
        setJdbcTemplate(jt);
    }

    protected MySqlHelper() {
    }

    /**
     * 获取属性名
     *
     * @param clazz 类
     * @return 属性名列表
     */
    protected List<String> getPropList(Class<?> clazz) {
        List<String> propList = new ArrayList<>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (!"class".equals(pd.getName())) {
                propList.add(pd.getName().toLowerCase());
            }
        }
        return propList;
    }

    @Override
    protected PrepareSqlParamResult prepareSqlParamForUpdate(Object bean, List<String> updateColumns) {
        PrepareSqlParamResult ret = new PrepareSqlParamResult();
        StringJoiner sj = new StringJoiner(",");
        List<String> tempPropList = new ArrayList<>();
        for (String column : updateColumns) {
            Optional<String> propName = propList.stream().filter(a -> a.equalsIgnoreCase(column)).findFirst();
            tempPropList.add(propName.get());
        }

        Map<String, Object> fieldMap = ReflectUtil.getAllFields(bean, tempPropList);

        for (String column : updateColumns) {
            Optional<String> propName = propList.stream().filter(a -> a.equalsIgnoreCase(column)).findFirst();

            if (fieldMap.containsKey(propName.get())) {
                sj.add(parseName(propName.get()) + "=:" + propName.get());
            } else {
                sj.add(parseName(propName.get()) + "=NULL");
            }
        }

        ret.sql = " SET " + sj.toString();
        ret.map = new MapSqlParameterSource(fieldMap);

        return ret;
    }

    public static void main(String[] args) {
        Map<String, Object> allFields = ReflectUtil.getAllFields(new B2bOrderBase());
        System.out.println(allFields);
    }


    @Override
    protected PrepareSqlParamResult prepareSqlParamForAdd(Object bean) {
        PrepareSqlParamResult ret = new PrepareSqlParamResult();

        Map<String, Object> allFields = ReflectUtil.getAllFields(bean);
        StringJoiner sjNames = new StringJoiner(",", "(", ")");
        StringJoiner sjPlaceHolders = new StringJoiner(",", "(", ")");
        for (String key : allFields.keySet()) {
            sjNames.add(parseName(key));
            sjPlaceHolders.add(":" + key);
        }

        ret.sql = sjNames.toString() + " VALUES " + sjPlaceHolders.toString();
        ret.map = new MapSqlParameterSource(allFields);

        return ret;
    }

    protected PrepareSqlParamResult prepareSqlParamForBatchAdd(Object bean) {
        PrepareSqlParamResult ret = new PrepareSqlParamResult();
        Map<String, Object> allFields = ReflectUtil.getAllFieldsForBatchAdd(bean);
        StringJoiner sjNames = new StringJoiner(",", "(", ")");
        StringJoiner sjPlaceHolders = new StringJoiner(",", "(", ")");
        for (String key : allFields.keySet()) {
            sjNames.add(parseName(key));
            sjPlaceHolders.add(":" + key);
        }
        ret.sql = sjNames.toString() + " VALUES " + sjPlaceHolders.toString();
        ret.map = new MapSqlParameterSource(allFields);
        return ret;
    }

    protected <Entity> SqlParameterSource[] prepareSqlParams(Collection<Entity> beans) {
        SqlParameterSource[] ret = new SqlParameterSource[beans.size()];
        int i = 0;
        for (Entity bean : beans) {
            Map<String, Object> allFields = ReflectUtil.getAllFieldsForBatchAdd(bean);
            ret[i++] = new MapSqlParameterSource(allFields);
        }
        return ret;
    }

    @Override
    public boolean exist(String conditionSql, Map<String, Object> argMap) {
        if (argMap == null) {
            throw new RuntimeException("argMap不能为空");
        }
        argMap = changeEnum(argMap);
        String sql = "SELECT COUNT(0) `cnt` FROM " + parseName(tableName) + " WHERE " + conditionSql;
        return (long) getSimple(sql, argMap).get("cnt") > 0;
    }


    @Override
    public <IdEntity extends IdEntityBasePure> void updateById(IdEntity data, String... updateColumns) {
        Map<String, Object> argMap = ImmutableMap.of("id", data.getId());
        update(data, "`id`=:id", argMap, updateColumns);
    }

    @Override
    public <IdEntity extends IdEntityBasePure, V> void updateById(IdEntity data, Getter<IdEntity, V>... getters) {
        String[] arr = new String[getters.length];
        for (int i = 0; i < getters.length; i++) {
            Getter<IdEntity, V> getter = getters[i];
            String prop = Util.prop(getter);
            arr[i] = prop;
        }

        updateById(data, arr);
    }


    @Override
    public <Entity> void update(Entity data, String conditionSql, Map<String, Object> conditionArgMap, String... updateColumns) {
        for (String c : updateColumns) {
            for (String a : conditionArgMap.keySet()) {
                if (StringUtils.equalsIgnoreCase(c, a)) {
                    throw new RuntimeException("更新列：" + c + "和sql参数：" + a + "一样");
                }
            }
        }
        PrepareSqlParamResult param = prepareSqlParamForUpdate(data, Arrays.asList(updateColumns));
        param.map.addValues(conditionArgMap);
        String sql = "UPDATE " + parseName(tableName) + " " + param.sql + " WHERE " + conditionSql;
        getJt().update(sql, param.map);
    }

    @Override
    public void deleteById(int id) {
        delete("id=:id", ImmutableMap.of("id", id));
    }

    @Override
    public void delete(String conditionSql, Map<String, Object> conditionArgMap) {
        conditionArgMap = changeEnum(conditionArgMap);
        String sql = "DELETE FROM " + parseName(tableName) + " WHERE " + conditionSql;
        getJt().update(sql, conditionArgMap);
    }

    @Override
    public T getById(int id) {
        return get("SELECT * FROM " + parseName(tableName) + " WHERE `id`=:id", ImmutableMap.of("id", id));
    }

    @Override
    public T getByIdForUpdate(int id) {
        return get("SELECT * FROM " + parseName(tableName) + " WHERE `id`=:id FOR UPDATE", ImmutableMap.of("id", id));
    }

    @Override
    public T get(String sql, Map<String, Object> argMap) {
        List<T> list = this.list(sql, argMap);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public T get() {
        return get("SELECT * FROM " + parseName(tableName) + " LIMIT 1", null);
    }

    @Override
    public T getForUpdate() {
        return get("SELECT * FROM " + parseName(tableName) + " LIMIT 1 FOR UPDATE", null);
    }

    @Override
    public T getForUpdate(String conditionSql, Map<String, Object> argMap) {
        if (argMap == null) {
            throw new RuntimeException("argMap不能为空");
        }
        argMap = changeEnum(argMap);
        String sql = "SELECT * FROM " + parseName(tableName) + " WHERE " + conditionSql + " LIMIT 1 FOR UPDATE";
        return get(sql, argMap);
    }

    @Override
    public <Entity> int addBatch(Collection<Entity> entities) {
        if (entities != null && !entities.isEmpty()) {
            MySqlHelper.PrepareSqlParamResult ret = prepareSqlParamForBatchAdd(entities.iterator().next());
            String sql = "INSERT INTO " + parseName(tableName) + " " + ret.getSql();
            SqlParameterSource[] params = prepareSqlParams(entities);

            int[] ids = getJt().batchUpdate(sql, params);
            return ids.length;
        } else {
            return 0;
        }
    }

    @Override
    public <Entity> void add(Entity t) {
        MySqlHelper.PrepareSqlParamResult ret = prepareSqlParamForAdd(t);
        String sql = "INSERT INTO " + parseName(tableName) + " " + ret.getSql();
        getJt().update(sql, ret.getMap());
    }

    @Override
    public <IdEntity extends IdEntityBasePure> void add(IdEntity t) {
        MySqlHelper.PrepareSqlParamResult ret = prepareSqlParamForAdd(t);
        String sql = "INSERT INTO " + parseName(tableName) + " " + ret.getSql();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJt().update(sql, ret.getMap(), keyHolder);
        if (keyHolder.getKey() != null) {
            t.setId(keyHolder.getKey().intValue());
        }
    }

    @Override
    public List<T> list() {
        return this.list("SELECT * FROM " + parseName(tableName), null);
    }

    @Override
    public List<T> list(String sql, Map<String, Object> argMap) {
        argMap = changeEnum(argMap);
        sql = setSelectColumn(sql);
        @SuppressWarnings({"unchecked", "rawtypes"}) List<T> list = getJt().query(sql, argMap, new BeanPropertyRowMapper(persistentClass));
        return list == null ? new ArrayList<T>() : list;
    }

    @Override
    public int getCount(String sql, Map<String, Object> argMap) {
        argMap = changeEnum(argMap);
        StringBuilder sb = new StringBuilder("SELECT COUNT(0) `cnt` FROM (");
        sb.append(sql);
        sb.append(") AS `_tn`");
        return (int) (long) getSimple(sb.toString(), argMap).get("cnt");
    }


    /**
     * 把sql语句里的* 换成列名,只能替换当前类的表,
     * 如果是其他表的则sql语句可能会错误
     *
     * @param sql
     * @return
     */
    private String setSelectColumn(String sql) {
        Pattern ptn = Pattern.compile("([\\w\\.`]*)\\*");
        Matcher mtch = ptn.matcher(sql);
        if (mtch.find()) {
            String prefix = mtch.group(1);
            StringJoiner joiner = new StringJoiner(",");
            joiner.setEmptyValue("");
            propList.stream().forEach(name -> joiner.add(prefix == null ? parseName(name) : prefix + parseName(name)));
            sql = sql.replace(mtch.group(), joiner.toString());
        }
        return sql;
    }

    @Override
    public PageBean<T> list(PageBean<T> pageBean, String sql, Map<String, Object> argMap) {
        argMap = changeEnum(argMap);
        sql = setSelectColumn(sql);

        int startRow = pageBean.getStartRow();
        int offset = pageBean.getPageSize();

        StringBuilder sb = new StringBuilder(sql);
        int count = this.getCount(sb.toString(), argMap);
        pageBean.setRowCount(count);

        if (StringUtils.isNotBlank(pageBean.getOrderField()) && propList.contains(pageBean.getOrderField().toLowerCase())) {
            sb.append(" ORDER BY ");
            sb.append(parseName(pageBean.getOrderField()));
            if (StringUtils.isNotBlank(pageBean.getOrderDirection()) && DbConstant.OrderDirections.contains(pageBean.getOrderDirection().toLowerCase())) {
                sb.append(" ");
                sb.append(pageBean.getOrderDirection());
            }
            if (propList.contains("id") && !"id".equalsIgnoreCase(pageBean.getOrderField())) {
                sb.append(",`id` ASC");
            }
        }
        sb.append(" LIMIT ");
        sb.append(startRow);
        sb.append(",");
        sb.append(offset);

        @SuppressWarnings({"rawtypes", "unchecked"}) List<T> data = getJt().query(sb.toString(), argMap, new BeanPropertyRowMapper(persistentClass));
        pageBean.setData(data);
        return pageBean;
    }

    @Override
    public PageBean<T> list(PageBean<T> pageBean, String sql) {
        return list(pageBean, sql, null);
    }

    @Override
    public <E> PageBean<E> query(PageBean<E> pageBean, String sql, Map<String, Object> argMap, Class<E> clazz) {
        argMap = changeEnum(argMap);
        sql = setSelectColumn(sql); // 由于是自定义的类型，所以把*转成字段名很可能会出错
        List<String> propList = getPropList(clazz); // 要使用自定义的类属性判断

        int startRow = pageBean.getStartRow();
        int offset = pageBean.getPageSize();
        StringBuilder sb = new StringBuilder(sql);
        int count = this.getCount(sb.toString(), argMap);
        pageBean.setRowCount(count);

        if (StringUtils.isNotBlank(pageBean.getOrderField()) && propList.contains(pageBean.getOrderField().toLowerCase())) {
            sb.append(" ORDER BY ");
            sb.append(parseName(pageBean.getOrderField()));
            if (StringUtils.isNotBlank(pageBean.getOrderDirection()) && DbConstant.OrderDirections.contains(pageBean.getOrderDirection().toLowerCase())) {
                sb.append(" ");
                sb.append(pageBean.getOrderDirection());
            }
        }
        sb.append(" LIMIT ");
        sb.append(startRow);
        sb.append(",");
        sb.append(offset);


        @SuppressWarnings({"unchecked", "rawtypes"}) List<E> data = getJt().query(sb.toString(), argMap, new BeanPropertyRowMapper(clazz));
        pageBean.setData(data);
        return pageBean;
    }
}
