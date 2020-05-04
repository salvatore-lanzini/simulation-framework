package com.lanzini.simulatorexecutor;


import com.lanzini.core.ConfigurationExecutor;
import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;
import com.lanzini.exception.SimulatorException;

public class SimulatorExecutor<T> {

    private MessageFactory<T> messageFactory;
    private Publisher<T> publisher;
    private ConfigurationExecutor<T> configurationExecutor;

    SimulatorExecutor(){}

    MessageFactory<T> getMessageFactory() {
        return messageFactory;
    }

    void setMessageFactory(MessageFactory<T> messageFactory) {
        this.messageFactory = messageFactory;
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

    public void simulate(){
        if(this.messageFactory == null || this.publisher == null || this.configurationExecutor == null)
            throw new SimulatorException();
        configurationExecutor.execute(messageFactory,publisher);
    }
}
