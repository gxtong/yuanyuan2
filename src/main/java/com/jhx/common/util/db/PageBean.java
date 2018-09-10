package com.jhx.common.util.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jhx.common.util.LogUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = 6295579614989910451L;
    /**
     * 第几页，从1开始
     */
    @Min(1)
    private int pageNo;

    /**
     * 总记录数
     */
    private int rowCount;

    @Max(100)
    private int pageSize;

    @Size(max = 100)
    private String orderField = "id";

    @Size(max = 100)
    private String orderDirection = "desc";

    private List<T> data = new ArrayList<>();

    private Map<String, Object> sumMap = new HashMap<String, Object>();

    /**
     * 设置按id降序排序
     *
     * @author 钱智慧
     * @date 2017年9月13日 下午7:26:40
     */
    @Deprecated
    public void setOrderByIdDesc() {
        setOrderDirection(DbConstant.Desc);
        setOrderField("id");
    }

    public PageBean() {
        pageSize = 20;
        pageNo = 1;
    }

    public int getStartRow() {
        int startRow = (pageNo - 1) * pageSize;
        if (startRow < 0) {
            startRow = 0;
        }
        return startRow;
    }

    public PageBean(int pageNo, int pageSize) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public PageBean<T> setPageNo(int pageNo) {
        pageNo = pageNo <= 0 ? 1 : pageNo;
        this.pageNo = pageNo;
        return this;
    }

    public PageBean<T> setPageSize(int pageSize) {
        if (pageSize > 100)
            this.pageSize = 100;
        else
            this.pageSize = pageSize;
        return this;
    }


    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private T _item;

    @JsonIgnore
    public T getItem() {
        if (_item == null) {
            _item = getItemObject();
        }

        return _item;
    }

    @JsonIgnore
    private T getItemObject() {
        Type type = getClass().getGenericSuperclass();

        // 判断 是否泛型
        if (type instanceof ParameterizedType) {
            Type[] arr = ((ParameterizedType) type).getActualTypeArguments();
            Class<?> clazz = (Class) arr[0];  //将第一个泛型T对应的类返回（这里只有一个）
            try {
                return (T) clazz.newInstance();
            } catch (Exception e) {
                LogUtil.err(e);
            }
        }

        return null;
    }
}
