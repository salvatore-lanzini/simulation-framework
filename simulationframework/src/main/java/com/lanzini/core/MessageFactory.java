package com.lanzini.core;

/**
 * A creator of factory for the message
 * @param <T> - the type of the message
 */
@FunctionalInterface
public interface MessageFactory<T> {
    /**
     * Creates the message
     * @return the message
     */
    T createMessage();
}
