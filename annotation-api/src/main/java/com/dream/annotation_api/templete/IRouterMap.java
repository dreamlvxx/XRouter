package com.dream.annotation_api.templete;

import com.dream.annotation_ann.RouteMeta;

import java.util.Map;

public interface IRouterMap {
    void loadMap(Map<String, RouteMeta> dst);
}
