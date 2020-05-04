package com.lanzini.core;

@FunctionalInterface
public interface ConfigurationExecutor<T> {
    void execute(MessageFactory<T> messageFactory, Publisher<T> publisher);
}
