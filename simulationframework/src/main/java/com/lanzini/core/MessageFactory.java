package com.lanzini.core;

@FunctionalInterface
public interface MessageFactory<T> {
    T createMessage();
}
