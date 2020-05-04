package com.lanzini;

import com.lanzini.configurationexecutor.ConfigurationExecutorTemplate;
import com.lanzini.core.ConfigurationExecutor;
import com.lanzini.simulatorexecutor.SimulatorFlow;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        SimulatorFlow.flow()
        .message( ()-> new Long(System.currentTimeMillis()).toString() )
        .<String>publish( message -> System.out.println(message))
                .<String>configure((messageFactory, publisher) ->
                        new ConfigurationExecutorTemplate<String>().
                                executeWithMessages(5,5, 1000,messageFactory,publisher))
                .build().simulate();

    }
}
