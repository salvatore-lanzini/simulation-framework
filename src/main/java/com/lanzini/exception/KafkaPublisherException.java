package com.lanzini.exception;

/**
 * Custom Exception for KafkaPublisherTemplate
 */
public class KafkaPublisherException extends Exception {
    public KafkaPublisherException(String msg){
        super(msg);
    }
}
