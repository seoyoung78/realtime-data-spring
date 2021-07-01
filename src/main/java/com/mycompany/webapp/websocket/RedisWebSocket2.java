package com.mycompany.webapp.websocket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
// chatWebSocket2로 관리 객체로 선언
public class RedisWebSocket2 extends TextWebSocketHandler implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket2.class);
	
	// Client 객체를 관리하는 컬렉션
	private List<Client> clients = new ArrayList<Client>();
	
	// 웹 클라이언트의 정보를 저장하고 있는 객체 설계
	private class Client {
		// 필드
		WebSocketSession session;
		String topic;
		
		// 생성자
		Client(WebSocketSession session) {
			this.session = session;
		}
		
		void setTopic(String topic) {
			this.topic = topic;
		}
	}
	
	// 메소드 재정의
	// 클라이언트가 연결하게 되면 서버측의 웹소켓 연결 정도가 들어옴
	// 웹 클라이언트가 WebSocket으로 최초 접속했을 때 콜백되는 메소드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("afterConnectionEstablished: " + session.getId());
		Client client = new Client(session);
		clients.add(client);
		logger.info("연결 Client 수: " + clients.size());
	}
	
	// 어떤 클라이언트에서 어떤 내용을 보내는지
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//메시지 내용 받기
		String strJson = message.getPayload();	//{"topic":"/topic1"}
		logger.info("handleTextMessage: " + strJson);
		
		// Topic을 받아서 클라이언트에 설정
		JSONObject jsonObject = new JSONObject(strJson);	//문자열로 날라오는 JSON을 JSONObject로 변환
		String topic = jsonObject.getString("topic");
		for(Client client : clients) {
			if(client.session == session) {
				client.setTopic(topic);
				break;
			}
		}
	}
	
	// 웹 클라이언트가 접속을 끊었을 때 콜백되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("afterConnectionClosed: " + session.getId());
		Iterator<Client> iterator = clients.iterator();
		while(iterator.hasNext()) {
			Client client = iterator.next();
			if(client.session == session) {
				client.session.close();
				iterator.remove();
			}
		}
		logger.info("연결 Client 수: " + clients.size());
	}

	// 지정한 Topic의 Redis에 있는 메시지 수신
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String topic = new String(message.getChannel());
			String content = new String(message.getBody());
			logger.info("onMessage: " + topic + " - " + content);
			
			// Redis에서 수신한 메시지를 클라이언트로 보냄
			// 모든 연결된 Client로 메시지 보내기
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("topic", topic);
			jsonObject.put("content", content);
			String json = jsonObject.toString();
			TextMessage sendMessage = new TextMessage(json);
			
			for(Client client: clients) {
				// "/topic/#"로 끝날 경우 하위 토픽 상관 없이 상위토픽이면 다 수신
				if(client.topic.endsWith("#")) {
					String t = client.topic.split("#")[0];
					// 상위 토픽이 같은지 비교
					if (topic.startsWith(t)) {
						client.session.sendMessage(sendMessage);						
					}
				} else {
					// Redis의 topic과 클라이언트의 topic이 동일할 경우만 메시지 수신할 수 있도록
					if(topic.equals(client.topic)) {
						client.session.sendMessage(sendMessage);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
