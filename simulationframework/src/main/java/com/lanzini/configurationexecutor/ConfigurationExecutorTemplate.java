package com.lanzini.configurationexecutor;

import com.lanzini.core.MessageFactory;
import com.lanzini.core.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationExecutorTemplate<T> {
    Logger logger = LoggerFactory.getLogger(ConfigurationExecutorTemplate.class);

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

    private void executeMulthiThreadingExecution(int threads, MessageFactory<T> messageFactory, Publisher<T> publisher){
        for(int k = 0 ; k < threads ; k++) {
            Runnable runnable = () -> publisher.publish(messageFactory.createMessage());
            new Thread(runnable).start();
        }
    }

}
