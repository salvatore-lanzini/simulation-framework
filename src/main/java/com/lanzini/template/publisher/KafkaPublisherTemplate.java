package com.lanzini.template.publisher;

import com.lanzini.template.connect.PublisherTemplateConnectionFactory;
import com.lanzini.enums.ConnectionTypeEnum;
import com.lanzini.exception.KafkaPublisherException;
import com.lanzini.exception.PublisherTemplateConnectionException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Useful Template to publish messages how to Apache Kafka producer
 */
public class KafkaPublisherTemplate {

    /**
     * Send a json message with Apache Kafka Producer
     *
     * @param topic Apache Kafka topic to publish message
     * @param message message to publish
     * @param <T> the type of the message
     * @throws KafkaPublisherException if something went wrong
     */
    public static <T> void send(String topic, T message) throws KafkaPublisherException {
        send(topic,Json.stringify(message));
    }

    /**
     * Send a message with Apache Kafka Producer
     *
     * @param topic Apache Kafka topic to publish message
     * @param message message to publish
     * @throws KafkaPublisherException if something went wrong
     */
    public static void send(String topic, String message) throws KafkaPublisherException {
        Producer<String, String> producer = createProducer();
        try {
            producer.send(new ProducerRecord<>(topic, Json.stringify(message))).get();
        }catch(Exception e){
            throw new KafkaPublisherException(e.getMessage());
        }
    }

    private static Producer<String, String> createProducer() {
        if (PublisherTemplateConnectionFactory.getKafkaProducer() == null)
            throw new PublisherTemplateConnectionException(ConnectionTypeEnum.KAFKA);
        else
            return PublisherTemplateConnectionFactory.getKafkaProducer();
    }
}
