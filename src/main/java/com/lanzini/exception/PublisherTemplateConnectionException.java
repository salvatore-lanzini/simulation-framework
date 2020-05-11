package com.lanzini.exception;

import com.lanzini.enums.ConnectionTypeEnum;

/**
 * Unchecked exception for operation with servers and brokers without connection opened
 */
public class PublisherTemplateConnectionException extends RuntimeException{
    public PublisherTemplateConnectionException(ConnectionTypeEnum connectionTypeEnum){
        super(String.format("Nessuna connessione di tipo %s aperta! Impossibile pubblicare",
                connectionTypeEnum.toString()));
    }

    public PublisherTemplateConnectionException(String msg){
        super(msg);
    }
}
