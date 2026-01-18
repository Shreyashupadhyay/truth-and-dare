package com.truthdare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket event message wrapper
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketEvent {
    private String eventType; // ROOM_CREATED, PLAYER_JOINED, GAME_STARTED, QUESTION_SENT, etc.
    private Object data; // Event-specific data
    private Long timestamp;
    
    public WebSocketEvent(String eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}