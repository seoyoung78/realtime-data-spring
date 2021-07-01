package com.mycompany.webapp.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.webapp.mqtt.MqttTemplate;
import com.mycompany.webapp.mqtt.MqttTemplate2;

@CrossOrigin(origins="*")
@Controller
public class HomeController {
	private static final Logger logger = 
			LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private MqttTemplate2 mqttTemplate;
	
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/chat2")
	public String chat2() {
		return "chat2";
	}
	
	@RequestMapping("/redis2")
	public String redis2() {
		return "redis2";
	}

	@RequestMapping("/sendRedisMessage2")
	public void sendRedisMessage(String topic, String content, HttpServletResponse response) {
		try {
			//Redis로 메시지를 보내면 WebSocket로 전달
			redisTemplate.convertAndSend(topic, content);
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter pw = response.getWriter();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("result", "success");
			pw.println(jsonObject.toString());	//{"result":"success"}
			pw.flush();
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/mqtt2")
	public String mqtt2() {
		return "mqtt2";
	}
	
	@RequestMapping("/sendMqttMessage2")
	public void sendMqttMessage2(String topic, String content, HttpServletResponse response) {
		try {
			//Redis로 메시지를 보내면 WebSocket로 전달
			mqttTemplate.sendMessage(topic, content);
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter pw = response.getWriter();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("result", "success");
			pw.println(jsonObject.toString());	//{"result":"success"}
			pw.flush();
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------------------
	
	@RequestMapping("/chat")
	public String chat() {
		return "chat";
	}
	
	@RequestMapping("/redis")
	public String redis() {
		return "redis";
	}
	
	@RequestMapping("/mqtt")
	public String mqtt() {
		return "mqtt";
	}
	
//	@RequestMapping("/sendRedisMessage")
//	public void sendRedisMessage(String topic, String content, HttpServletResponse res) {
//		logger.info("sendMessage");
//		try {
//			redisTemplate.convertAndSend(topic, content);
//		
//			JSONObject json = new JSONObject();
//			json.put("result", "success");
//			res.setContentType("application/json; charset=UTF-8");
//			PrintWriter writer = res.getWriter();
//			writer.write(json.toString());
//			writer.flush();
//			writer.close();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@RequestMapping("/sendMqttMessage")
//	public void sendMqttMessage(String topic, String content, HttpServletResponse res) {
//		logger.info("sendMessage");
//		try {
//			mqttTemplate.sendMessage(topic, content);
//		
//			JSONObject json = new JSONObject();
//			json.put("result", "success");
//			res.setContentType("application/json; charset=UTF-8");
//			PrintWriter writer = res.getWriter();
//			writer.write(json.toString());
//			writer.flush();
//			writer.close();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}	
}
