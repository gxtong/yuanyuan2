package com.jhx.common.util.db;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordDatetimeListModelIn<T> extends DatetimeListModelIn<T> {
    private String keyword;
}
