package com.lanzini.configurationexecutor;

import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful template for a configurationExecutor 'execute' method
 * @param <T> the type of the simulation
 */
public class ConfigurationExecutorTemplate<T> {
    Logger logger = LoggerFactory.getLogger(ConfigurationExecutorTemplate.class);

    /**
     * Execute a multi-threading simulation with a number of threads, messages and delay intra messages
     * @param threads number of threads wich execute simulation
     * @param messages number of messages to publish
     * @param delay delay intra messages
     * @param messageFactory messageFactory of simulation
     * @param publisher publisher of simulation
     */
    public void executeWithMessages(int threads, int messages, long delay, MessageFactory<T> messageFactory, Publisher<T> publisher){
        for(int i=0 ; i < messages ; i++) {
            executeMulthiThreadingExecution(threads, messageFactory, publisher);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Execute a multi-threading simulation with a number of threads, a duration of simulation in terms of minutes and
     * a delay intra messages
     * @param threads number of threads wich execute simulation
     * @param timeRangeMinutes duration of the simulation in terms of minutes
     * @param delay delay intra messages
     * @param messageFactory messageFactory of simulation
     * @param publisher publisher of simulation
     */
    public void executeWithTimeRangeMinutes(int threads, int timeRangeMinutes, long delay, MessageFactory<T> messageFactory, Publisher<T> publisher){
        long startTimestamp = System.currentTimeMillis();
        long timeRangeMinutesMillis = timeRangeMinutes * 60 * 1000;
        long timeLimit = startTimestamp + timeRangeMinutesMillis;
        do{
            executeMulthiThreadingExecution(threads, messageFactory, publisher);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }while(System.currentTimeMillis() < timeLimit);
    }

    /**
     * Execute multi threading simulation
     * @param threads number of threads
     * @param messageFactory messageFactory of simulation
     * @param publisher publisher of simulation
     */
    private void executeMulthiThreadingExecution(int threads, MessageFactory<T> messageFactory, Publisher<T> publisher){
        for(int k = 0 ; k < threads ; k++) {
            Runnable runnable = () -> publisher.publish(messageFactory.createMessage());
            new Thread(runnable).start();
        }
    }

}
