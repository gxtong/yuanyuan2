package com.jhx.common.util.db;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class KeywordListModelIn<T> extends PageBean<T> {
    private String keyword;
}
