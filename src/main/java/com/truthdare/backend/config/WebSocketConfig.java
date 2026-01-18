package com.truthdare.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for STOMP protocol
 * Enables real-time communication between clients and server
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker to carry messages to clients
        // Messages prefixed with "/topic" will be routed to message subscribers
        config.enableSimpleBroker("/topic");
        
        // Messages prefixed with "/app" are routed to message-handling methods
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint that clients will connect to
        var endpoint = registry.addEndpoint("/ws");
        
        // Set allowed origins based on configuration
        if ("*".equals(allowedOrigins)) {
            endpoint.setAllowedOriginPatterns("*");
        } else {
            endpoint.setAllowedOrigins(allowedOrigins.split(","));
        }
        
        endpoint.withSockJS(); // Enable SockJS fallback options for browsers that don't support WebSocket
    }
}