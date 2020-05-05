package com.lanzini.exception;

/**
 * Runtime Exception to raise Unchecked Excpetion
 */
public class SimulatorException extends RuntimeException {
    private static final String MSG = "Unable to start Simulation! Check your configuration Simulation Flow" +
            " [ MessageFactroy,Publisher,ConfigurationExecutor ]";

    public SimulatorException(){
        super(MSG);
    }
}
