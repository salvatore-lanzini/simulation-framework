package com.lanzini.core;

/**
 * A configurator for the execution of simulation
 *
 * @param <T> the type of element in Configuration Executor
 */
@FunctionalInterface
public interface ConfigurationExecutor<T> {
    /**
     * configure an execution of simulation
     *
     * @param messageFactory the factory of the messages
     * @param publisher the publisher of the message
     */
    void execute(MessageFactory<T> messageFactory, Publisher<T> publisher);
}
