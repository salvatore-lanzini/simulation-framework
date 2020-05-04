package com.lanzini.core;

@FunctionalInterface
public interface Publisher<T> {
    void publish(T message);
}
