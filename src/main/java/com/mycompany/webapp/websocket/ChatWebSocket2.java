package com.mycompany.webapp.websocket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
// chatWebSocket2로 관리 객체로 선언
public class ChatWebSocket2 extends TextWebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket2.class);
	
	// Client 객체를 관리하는 컬렉션
	private List<Client> clients = new ArrayList<Client>();
	
	// 웹 클라이언트의 정보를 저장하고 있는 객체 설계
	private class Client {
		// 필드
		WebSocketSession session;
		
		// 생성자
		Client(WebSocketSession session) {
			this.session = session;
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
		String strJson = message.getPayload();
		logger.info("handleTextMessage: " + strJson);
		
		// 모든 연결된 Client로 메시지 보내기
		TextMessage sendMessage = new TextMessage(strJson);
		for(Client client: clients) {
			client.session.sendMessage(sendMessage);
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
}
