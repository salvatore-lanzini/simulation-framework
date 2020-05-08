package com.lanzini.template.publisher;

import com.lanzini.exception.MqttPublisherException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.UUID;

/**
 * Useful template to publish messages how to Mqtt Message
 */
public class MqttPublisherTemplate {

    private static final String CLIENT_ID_PREFIX = "Si-Frmwk-Mqtt-Id";

    /**
     * send a mqtt json message
     * @param brokerHost mqtt broker host
     * @param brokerPort mqtt broker port
     * @param brokerUsername mqtt broker username
     * @param brokerPassword mqtt broker password
     * @param message json message to send
     * @param topic mqtt topic
     * @param qos mqtt quality of service
     * @param <T> the type of the message
     * @throws MqttPublisherException if something went wrong
     */
    public static <T> void send(String brokerHost, String brokerPort, String brokerUsername, String brokerPassword,
                            T message, String topic, int qos) throws MqttPublisherException{
        send(brokerHost,brokerPort,brokerUsername,brokerPassword,Json.stringify(message),topic,qos);
    }

    /**
     * send a mqtt message
     * @param brokerHost mqtt broker host
     * @param brokerPort mqtt broker port
     * @param brokerUsername mqtt broker username
     * @param brokerPassword mqtt broker password
     * @param message message to send
     * @param topic mqtt topic
     * @param qos mqtt quality of service
     * @throws MqttPublisherException if something went wrong
     */
    public static void send(String brokerHost, String brokerPort, String brokerUsername, String brokerPassword,
                            String message, String topic, int qos) throws MqttPublisherException{
        try{
            MqttClient mqttClient = broker(brokerHost,brokerPort,brokerUsername,brokerPassword);
            mqttClient.publish(topic,mqttMessage(message,qos));
            mqttClient.close();
        }catch (Exception e){
            throw new MqttPublisherException(e.getMessage());
        }
    }

    private static MqttClient broker(String brokerHost, String brokerPort,
                                        String brokerUsername, String brokerPassword) throws MqttPublisherException{
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            String urlBroker = String.format("tcp://%s:%s", brokerHost, brokerPort);
            options.setServerURIs(new String[]{urlBroker});
            options.setUserName(brokerUsername);
            options.setPassword(brokerPassword.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            MqttClient publisher = new MqttClient(urlBroker, CLIENT_ID_PREFIX + UUID.randomUUID().toString());
            publisher.connect(options);
            return publisher;
        }catch (Exception e){
            throw new MqttPublisherException(e.getMessage());
        }
    }

    private static MqttMessage mqttMessage(String message, int qos) throws MqttPublisherException{
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            return mqttMessage;
        }catch(Exception e){
            throw new MqttPublisherException(e.getMessage());
        }
    }

}
