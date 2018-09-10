package com.jhx.common.layui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 钱智慧
 * date 7/13/18 2:22 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetDataMapModel {
    private boolean rate;
    private List<String> targetNames = new ArrayList<>();
}
