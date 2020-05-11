package com.lanzini.core;

/**
 * Define the way to publish the message
 *
 * @param <T> the type of the message to publish
 */
@FunctionalInterface
public interface Publisher<T> {
    /**
     * publish the message
     *
     * @param message to publish
     */
    void publish(T message);
}
