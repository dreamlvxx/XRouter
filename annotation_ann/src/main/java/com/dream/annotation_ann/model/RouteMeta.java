package com.dream.annotation_ann.model;

public class RouteMeta {
    String path;
    private Class<?> destination;

    public RouteMeta(String path, Class<?> destination) {
        this.path = path;
        this.destination = destination;
    }

    public RouteMeta(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }
}
