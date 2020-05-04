package com.lanzini.simulatorexecutor;


import com.lanzini.core.ConfigurationExecutor;
import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;

public class SimulatorFlow {
    private SimulatorExecutor simulatorExecutor;

    private SimulatorFlow(){
        this.simulatorExecutor = new SimulatorExecutor();
    }

    public static SimulatorFlow flow(){
        return new SimulatorFlow();
    }

    public <T> SimulatorFlow message(MessageFactory<T> messageFactory){
        this.simulatorExecutor.setMessageFactory(messageFactory);
        return this;
    }

    public <T> SimulatorFlow publish(Publisher<T> publisher){
        this.simulatorExecutor.setPublisher(publisher);
        return this;
    }

    public <T> SimulatorFlow configure(ConfigurationExecutor<T> configurationExecutor){
        this.simulatorExecutor.setConfigurationExecutor(configurationExecutor);
        return this;
    }

    public SimulatorExecutor build(){
        return simulatorExecutor;
    }
}
