package com.project.webserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint and allow SockJS fallback
        registry.addEndpoint("/ws");
        registry.addEndpoint("/ws").withSockJS();// In case you use another port
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable message broker for the "/topic" destination
        config.enableSimpleBroker("/all");
        config.setApplicationDestinationPrefixes("/app");
    }
}