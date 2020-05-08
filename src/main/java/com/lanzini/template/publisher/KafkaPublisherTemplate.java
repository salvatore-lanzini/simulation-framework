package com.lanzini.template.publisher;

import com.lanzini.exception.KafkaPublisherException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import java.util.UUID;

/**
 * Useful Template to publish messages how to Apache Kafka producer
 */
public class KafkaPublisherTemplate {

    /**
     * Send a json message with Apache Kafka Producer
     * @param brokers list of Apache Kafka Brokers
     * example with 1 broker "99.999.999.90:9092"
     * example with 3 brokers "99.999.999.90:9092;99.999.999.91:9092;99.999.999.92:9092;"
     * @param topic Apache Kafka topic to publish message
     * @param message message to publish
     * @param <T> the type of the message
     * @throws KafkaPublisherException if something went wrong
     */
    public static <T> void send(String brokers, String topic, T message) throws KafkaPublisherException {
        send(brokers,topic,Json.stringify(message));
    }

    /**
     * Send a message with Apache Kafka Producer
     * @param brokers list of Apache Kafka Brokers
     * example with 1 broker "99.999.999.90:9092"
     * example with 3 brokers "99.999.999.90:9092;99.999.999.91:9092;99.999.999.92:9092;"
     * @param topic Apache Kafka topic to publish message
     * @param message message to publish
     * @throws KafkaPublisherException if something went wrong
     */
    public static void send(String brokers, String topic, String message) throws KafkaPublisherException {
        try {
            Producer<String, String> producer = createProducer(brokers);
            producer.send(new ProducerRecord<>(topic, Json.stringify(message))).get();
            deleteProducer(producer);
        }catch(Exception e){
            throw new KafkaPublisherException(e.getMessage());
        }
    }

    private static Producer<String, String> createProducer(String brokers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    private static void deleteProducer(Producer<String, String> producer){
        producer.flush();
        producer.close();
    }
}
