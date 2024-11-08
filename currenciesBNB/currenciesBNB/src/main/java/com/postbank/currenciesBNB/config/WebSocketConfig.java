package com.postbank.currenciesBNB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.postbank.currenciesBNB.handler.UpdatedCurrenciesHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	public static String onlyUpdateCurreencies;

	@Bean
    WebSocketHandler tutorialHandler() {
		return new UpdatedCurrenciesHandler();
    }

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(tutorialHandler(), "/ws-download-currencies")
        .setAllowedOrigins("*");	
	}
}
