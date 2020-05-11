package com.lanzini.simulatorexecutor;


import com.lanzini.template.connect.PublisherTemplateConnectionFactory;
import com.lanzini.core.ConfigurationExecutor;
import com.lanzini.core.ConnectionTemplate;
import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;
import com.lanzini.exception.SimulatorException;

/**
 * Executor of the simulation
 *
 * @param <T> the type of the simulation
 */
public class SimulatorExecutor<T> {

    private MessageFactory<T> messageFactory;
    private ConnectionTemplate connectionTemplate;
    private Publisher<T> publisher;
    private ConfigurationExecutor<T> configurationExecutor;

    SimulatorExecutor(){}

    MessageFactory<T> getMessageFactory() {
        return messageFactory;
    }

    void setMessageFactory(MessageFactory<T> messageFactory) {
        this.messageFactory = messageFactory;
    }

    ConnectionTemplate getConnectionTemplate() {
        return connectionTemplate;
    }

    void setConnectionTemplate(ConnectionTemplate connectionTemplate) {
        this.connectionTemplate = connectionTemplate;
    }

    Publisher<T> getPublisher() {
        return publisher;
    }

    void setPublisher(Publisher<T> publisher) {
        this.publisher = publisher;
    }

    ConfigurationExecutor<T> getConfigurationExecutor() {
        return configurationExecutor;
    }

    void setConfigurationExecutor(ConfigurationExecutor<T> configurationExecutor) {
        this.configurationExecutor = configurationExecutor;
    }

    /**
     * Start the simulation
     */
    public void simulate(){
        if(this.messageFactory == null || this.publisher == null || this.configurationExecutor == null)
            throw new SimulatorException();
        if(connectionTemplate != null) connectionTemplate.connect();
        configurationExecutor.execute(messageFactory,publisher);
        PublisherTemplateConnectionFactory.disconnect();
    }
}
