package com.lanzini.template.publisher;

import com.lanzini.enums.ConnectionTypeEnum;
import com.lanzini.exception.MqttPublisherException;
import com.lanzini.exception.PublisherTemplateConnectionException;
import com.lanzini.template.connect.PublisherTemplateConnectionFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.UUID;

/**
 * Useful template to publish messages how to Mqtt Message
 */
public class MqttPublisherTemplate {

    /**
     * send a mqtt json message
     *
     * @param message json message to send
     * @param topic mqtt topic
     * @param qos mqtt quality of service
     * @param <T> the type of the message
     * @throws MqttPublisherException if something went wrong
     */
    public static <T> void send(T message, String topic, int qos) throws MqttPublisherException{
        send(Json.stringify(message),topic,qos);
    }

    /**
     * send a mqtt message
     *
     * @param message message to send
     * @param topic mqtt topic
     * @param qos mqtt quality of service
     * @throws MqttPublisherException if something went wrong
     */
    public static void send(String message, String topic, int qos) throws MqttPublisherException{
        MqttClient mqttClient = broker();
        try{ mqttClient.publish(topic,mqttMessage(message,qos));
        }catch (Exception e){
            throw new MqttPublisherException(e.getMessage());
        }
    }

    private static MqttClient broker(){
       if(PublisherTemplateConnectionFactory.getMqttClient() == null)
           throw new PublisherTemplateConnectionException(ConnectionTypeEnum.MQTT);
       else return PublisherTemplateConnectionFactory.getMqttClient();
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
