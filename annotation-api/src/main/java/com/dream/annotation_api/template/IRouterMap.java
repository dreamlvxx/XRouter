package com.dream.annotation_api.template;

import com.dream.annotation_ann.model.RouteMeta;

import java.util.Map;

public interface IRouterMap {
    void loadInto(Map<String, RouteMeta> dst);
}
