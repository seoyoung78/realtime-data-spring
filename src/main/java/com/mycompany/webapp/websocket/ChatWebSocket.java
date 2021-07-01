package com.mycompany.webapp.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocket extends TextWebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
	
	//Client를 저장하기 위한 컬렉션
	private List<Client> clients = new ArrayList<Client>();
	
	//웹 클라이언트의 정보 저장용
	private class Client {
		WebSocketSession session;
		
		Client(WebSocketSession session) { 
			this.session = session; 
		}
		
		void close() {
			try {
				session.close();
			} catch (IOException e) {
			}
		}
	}	
	
	//WebSocket ----------------------------------------------------------------------
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("afterConnectionEstablished: " + session.getId());
		Client client = new Client(session);
		clients.add(new Client(session));
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String strJson = message.getPayload();
		logger.info("handleTextMessage: " + strJson);
		TextMessage sendMessage = new TextMessage(strJson);
		for(Client client : clients) {
			client.session.sendMessage(message);
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("afterConnectionClosed: " + session.getId());
		Iterator<Client> iterator = clients.iterator();
		while(iterator.hasNext()) {
			Client client = iterator.next();
			if(client.session.getId() == session.getId()) {
				client.close();
				iterator.remove();
			}
		}
	}
}
