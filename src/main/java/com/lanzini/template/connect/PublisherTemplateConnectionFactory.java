package com.lanzini.template.connect;

import com.lanzini.exception.PublisherTemplateConnectionException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.UUID;

/**
 * Factory of connections with brokers and servers
 */
public class PublisherTemplateConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(PublisherTemplateConnectionFactory.class);
    private static final String CLIENT_ID_PREFIX = "Si-Frmwk-Mqtt-Id";

    private static MqttClient mqttClient;
    private static FTPClient ftpClient;
    private static KafkaProducer<String, String> kafkaProducer;

    public static MqttClient getMqttClient() {
        return mqttClient;
    }

    public static FTPClient getFtpClient() {
        return ftpClient;
    }

    public static KafkaProducer<String, String> getKafkaProducer() {
        return kafkaProducer;
    }

    /**
     * Open connection with Apache Kafka brokers
     * @param brokers list of Apache Kafka brokers
     * example with 1 broker "99.999.999.90:9092"
     * example with 3 brokers "99.999.999.90:9092;99.999.999.91:9092;99.999.999.92:9092;"
     */
    public static void kafka(String brokers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    /**
     * Open connection with Mqtt broker
     *
     * @param brokerHost mqtt broker host
     * @param brokerPort mqtt broker port
     * @param brokerUsername mqtt broker username
     * @param brokerPassword mqtt broker password
     */
    public static void mqtt(String brokerHost, int brokerPort,
                     String brokerUsername, String brokerPassword) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            String urlBroker = String.format("tcp://%s:%d", brokerHost, brokerPort);
            options.setServerURIs(new String[]{urlBroker});
            options.setUserName(brokerUsername);
            options.setPassword(brokerPassword.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            MqttClient publisher = new MqttClient(urlBroker, CLIENT_ID_PREFIX + UUID.randomUUID().toString());
            publisher.connect(options);
            mqttClient = publisher;
        } catch (Exception e) {
            throw new PublisherTemplateConnectionException(e.getMessage());
        }
    }

    /**
     * Open a connection with Ftp Server
     *
     * @param server ftp server host
     * @param port ftp server port
     * @param user ftp server username
     * @param password ftp server password
     */
    public static void ftp(String server, int port, String user, String password) {
        try {
            FTPClient ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftp.connect(server, port);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }
            ftp.login(user, password);
            ftpClient = ftp;
        } catch (Exception e) {
            throw new PublisherTemplateConnectionException(e.getMessage());
        }
    }

    /**
     * Close all connections with servers and brokers
     */
    public static void disconnect() {
        try {
            if (mqttClient != null){ mqttClient.disconnect(); mqttClient.close(); }
            if (ftpClient != null) ftpClient.disconnect();
            if (kafkaProducer != null) { kafkaProducer.flush(); kafkaProducer.close(); }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
