package com.lanzini.core;

/**
 * A configurator for connections with servers and brokers for the simulation
 */
@FunctionalInterface
public interface ConnectionTemplate {
    /**
     * Set up a connection with PublisherTemplateConnectionFactory
     */
    void connect();
}
