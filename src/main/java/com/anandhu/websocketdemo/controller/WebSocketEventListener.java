package com.anandhu.websocketdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.anandhu.websocketdemo.model.ChatMessage;

@Component
public class WebSocketEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@EventListener
	public void handleWebSocketConnectionListener(SessionConnectedEvent connectedEvent)
	{
		logger.info("Recieved a new web scocket connection");
	}
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent)
	{
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
		
		String username = (String)headerAccessor.getSessionAttributes().get("username");
		if(username!=null)
		{
			logger.info("User Disconnected : "+username);
			
			 ChatMessage chatMessage = new ChatMessage();
	            chatMessage.setType(ChatMessage.MessageType.LEAVE);
	            chatMessage.setSender(username);
	            
	            messagingTemplate.convertAndSend("/topic/public", chatMessage);
	            
			
		}
	}
}
