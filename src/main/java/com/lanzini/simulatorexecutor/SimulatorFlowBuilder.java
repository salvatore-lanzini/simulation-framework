package com.lanzini.simulatorexecutor;


import com.lanzini.core.ConfigurationExecutor;
import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;

/**
 * Builder of the simulation
 */
public class SimulatorFlowBuilder {
    private SimulatorExecutor simulatorExecutor;

    private SimulatorFlowBuilder(){
        this.simulatorExecutor = new SimulatorExecutor();
    }

    /**
     * Get a new flow builder
     * @return a new instance of SimulationFlowBuilder
     */
    public static SimulatorFlowBuilder flow(){
        return new SimulatorFlowBuilder();
    }

    /**
     * Set the messageFactory of flow builder
     * @param messageFactory the messageFactory of flow builder
     * @param <T> the type of the message
     * @return the instance of flow builder
     */
    public <T> SimulatorFlowBuilder message(MessageFactory<T> messageFactory){
        this.simulatorExecutor.setMessageFactory(messageFactory);
        return this;
    }

    /**
     * Set the publisher of flow builder
     * @param publisher the publisher of flow builder
     * @param <T> the type of the simulation
     * @return the instance of flow builder
     */
    public <T> SimulatorFlowBuilder publish(Publisher<T> publisher){
        this.simulatorExecutor.setPublisher(publisher);
        return this;
    }

    /**
     * Set the configurationExecutor of flow builder
     * @param configurationExecutor the configurationExecutor of flow builder
     * @param <T> the type of the simulation
     * @return the instance of flow builder
     */
    public <T> SimulatorFlowBuilder configure(ConfigurationExecutor<T> configurationExecutor){
        this.simulatorExecutor.setConfigurationExecutor(configurationExecutor);
        return this;
    }

    /**
     * get a new instance of simulatorExecutor
     * @return the simulatorExecutor
     */
    public SimulatorExecutor build(){
        return simulatorExecutor;
    }
}
