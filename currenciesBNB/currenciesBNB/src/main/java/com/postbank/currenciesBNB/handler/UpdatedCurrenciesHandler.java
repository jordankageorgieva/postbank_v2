package com.postbank.currenciesBNB.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.postbank.currenciesBNB.config.WebSocketConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdatedCurrenciesHandler implements WebSocketHandler{
	

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Connection established on session: {}" + session.getId());
		
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	//	String tutorial = (String) message.getPayload();
		if (!WebSocketConfig.onlyUpdateCurreencies.isEmpty()) {
			session.sendMessage(new TextMessage(WebSocketConfig.onlyUpdateCurreencies));
		} else {
			session.sendMessage(new TextMessage("There are no currencies in the last 1 min."));
		}
		
		System.out.println("Message: {}" + WebSocketConfig.onlyUpdateCurreencies);
//        session.sendMessage(new TextMessage(("Started processing tutorial: " + session + " - " + tutorial)));
//        Thread.sleep(1000);
//        session.sendMessage(new TextMessage("Completed processing tutorial: " + tutorial));
		
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("Exception occured: {} on session: {}" + exception.getMessage() + ";" + session.getId());
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("Connection closed on session: {} with status: {}" + session.getId() + closeStatus.getCode());
		
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

}
