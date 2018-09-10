package com.jhx.common.util.db;

import com.jhx.common.util.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SqlHelper 的oracle实现
 *
 * @author 钱智慧
 * @date 2017年9月2日 下午3:52:42
 */
@SuppressWarnings("unchecked")
public class OracleSqlHelper<T> extends SqlHelper<T> {


    @Override
    protected PrepareSqlParamResult prepareSqlParamForUpdate(Object bean, List<String> updateColumns) {
        return null;
    }

    @Override
    protected PrepareSqlParamResult prepareSqlParamForAdd(Object bean) {
        return null;
    }

    @Override
    public boolean exist(String conditionSql, Map<String, Object> argMap) {
        return false;
    }

    @Override
    public <E> PageBean<E> query(PageBean<E> pageBean, String sql, Map<String, Object> argMap, Class<E> clazz) {
        return null;
    }

    @Override
    public <Entity> void update(Entity data, String conditionSql, Map<String, Object> conditionArgMap, String... updateColumns) {

    }

    @Override
    public <IdEntity extends IdEntityBasePure> void updateById(IdEntity data, String... updateColumns) {

    }

    @Override
    public <IdEntity extends IdEntityBasePure, V> void updateById(IdEntity data, Getter<IdEntity, V>... getters) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void delete(String conditionSql, Map<String, Object> conditionArgMap) {

    }

    @Override
    public T getById(int id) {
        return null;
    }

    @Override
    public T getByIdForUpdate(int id) {
        return null;
    }

    @Override
    public T get() {
        return null;
    }

    @Override
    public T getForUpdate() {
        return null;
    }

    @Override
    public T getForUpdate(String conditionSql, Map<String, Object> argMap) {
        return null;
    }

    @Override
    public T get(String sql, Map<String, Object> argMap) {
        return null;
    }

    @Override
    public <IdEntity extends IdEntityBasePure> void add(IdEntity t) {

    }

    @Override
    public <Entity> int addBatch(Collection<Entity> entities) {
        return 0;
    }

    @Override
    public List<T> list() {
        return null;
    }

    @Override
    public List<T> list(String sql, Map<String, Object> argMap) {
        return null;
    }

    @Override
    public <Entity> void add(Entity t) {

    }

    @Override
    public int getCount(String sql, Map<String, Object> argMap) {
        return 0;
    }

    @Override
    public PageBean<T> list(PageBean<T> pageBean, String sql, Map<String, Object> argMap) {
        return null;
    }

    @Override
    public PageBean<T> list(PageBean<T> pageBean, String sql) {
        return list(pageBean, sql, null);
    }
}