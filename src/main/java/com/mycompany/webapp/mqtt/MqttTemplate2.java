package com.mycompany.webapp.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MqttTemplate2 {
	private static final Logger logger = LoggerFactory.getLogger(MqttTemplate2.class);
	
	// Mqtt Client
	private MqttClient mqttClient;
	
	// 생성자
	public MqttTemplate2() {
		try {
			// Mqtt QoS 0 사용하기 위함
			// 접속 MQTT 브로커에 접속할 정보를 가지고 객체 생성
			mqttClient = new MqttClient(
					"tcp://localhost:1883", MqttClient.generateClientId(), null);
			
			// 연결 하기(접속한다고 자동 연결x
			MqttConnectOptions options = new MqttConnectOptions();
			// 브로커와 연결이 끊어지더라도 자동 연결 시도
			options.setAutomaticReconnect(true);
			mqttClient.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	// MQTT 브로커로 메시지 전송
	public void sendMessage(String topic, String content) {
		try {
			MqttMessage message = new MqttMessage();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("topic", topic);
			jsonObject.put("content", content);
			String json = jsonObject.toString();
			
			message.setPayload(json.getBytes());
			message.setQos(0);	// 기본값은 1
			
			mqttClient.publish(topic, message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
