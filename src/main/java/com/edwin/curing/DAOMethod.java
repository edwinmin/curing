package com.edwin.curing;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.edwin.curing.enums.DAOActionType;
import com.google.common.collect.Maps;

/**
 * @author jinming.wu
 * @date 2014-8-21
 */
public class DAOMethod {

    @Setter
    @Getter
    private String              name;

    @Setter
    @Getter
    private Map<String, Object> params = Maps.newHashMap();

    @Setter
    @Getter
    private DAOActionType       actionType;
}
