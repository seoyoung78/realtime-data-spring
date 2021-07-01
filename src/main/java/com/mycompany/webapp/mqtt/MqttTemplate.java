package com.mycompany.webapp.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mycompany.webapp.websocket.RedisWebSocket;

@Component
public class MqttTemplate {
	private static final Logger logger = 
			LoggerFactory.getLogger(RedisWebSocket.class);
	private MqttClient mqttClient;
	
	public MqttTemplate() {
		try {
			mqttClient = new MqttClient(
				"tcp://localhost:1883", MqttClient.generateClientId(), null);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			mqttClient.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String topic, String content) {
		try {
			MqttMessage message = new MqttMessage();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("topic", topic);
			jsonObject.put("content", content);
			String json = jsonObject.toString();
			message.setPayload(json.getBytes());
			
			message.setQos(0); //Default: 1
			
			mqttClient.publish(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
