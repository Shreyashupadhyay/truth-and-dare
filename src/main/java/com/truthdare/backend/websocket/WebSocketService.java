package com.truthdare.backend.websocket;

import com.truthdare.backend.dto.RoomStateDto;
import com.truthdare.backend.dto.WebSocketEvent;
import com.truthdare.backend.service.GameService;
import com.truthdare.backend.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for broadcasting WebSocket messages to clients
 */
@Slf4j
@Service
public class WebSocketService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;
    private final GameService gameService;
    
    // WebSocket event types
    public static final String EVENT_ROOM_CREATED = "ROOM_CREATED";
    public static final String EVENT_PLAYER_JOINED = "PLAYER_JOINED";
    public static final String EVENT_PLAYER_LEFT = "PLAYER_LEFT";
    public static final String EVENT_GAME_STARTED = "GAME_STARTED";
    public static final String EVENT_QUESTION_SENT = "QUESTION_SENT";
    public static final String EVENT_ADMIN_OVERRIDE = "ADMIN_OVERRIDE";
    public static final String EVENT_NEXT_TURN = "NEXT_TURN";
    public static final String EVENT_ROOM_STATE = "ROOM_STATE";
    public static final String EVENT_GAME_MODE_CHANGED = "GAME_MODE_CHANGED";
    
    public WebSocketService(SimpMessagingTemplate messagingTemplate,
                           RoomService roomService,
                           GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.roomService = roomService;
        this.gameService = gameService;
    }
    
    /**
     * Broadcast a room state update to all subscribers of a room
     */
    public void broadcastRoomState(String roomId) {
        var room = roomService.getRoomById(roomId);
        if (room == null) {
            log.warn("Cannot broadcast state for non-existent room: {}", roomId);
            return;
        }
        
        RoomStateDto state = gameService.toRoomStateDto(room);
        WebSocketEvent event = new WebSocketEvent(EVENT_ROOM_STATE, state);
        
        String topic = "/topic/room/" + room.getRoomCode();
        messagingTemplate.convertAndSend(topic, event);
        
        log.debug("Broadcasted room state for room {}", room.getRoomCode());
    }
    
    /**
     * Broadcast a custom event to a room
     */
    public void broadcastEvent(String roomCode, String eventType, Object data) {
        WebSocketEvent event = new WebSocketEvent(eventType, data);
        String topic = "/topic/room/" + roomCode;
        messagingTemplate.convertAndSend(topic, event);
        
        log.debug("Broadcasted event {} to room {}", eventType, roomCode);
    }
    
    /**
     * Broadcast an event to the admin-only topic
     */
    public void broadcastToAdmin(String roomCode, String eventType, Object data) {
        WebSocketEvent event = new WebSocketEvent(eventType, data);
        String topic = "/topic/room/" + roomCode + "/admin";
        messagingTemplate.convertAndSend(topic, event);
        
        log.debug("Broadcasted admin event {} to room {}", eventType, roomCode);
    }
    
    /**
     * Notify that a room was created
     */
    public void notifyRoomCreated(String roomCode, Object data) {
        broadcastEvent(roomCode, EVENT_ROOM_CREATED, data);
    }
    
    /**
     * Notify that a player joined
     */
    public void notifyPlayerJoined(String roomCode, Object playerData) {
        broadcastEvent(roomCode, EVENT_PLAYER_JOINED, playerData);
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify that a player left
     */
    public void notifyPlayerLeft(String roomCode, String playerId) {
        broadcastEvent(roomCode, EVENT_PLAYER_LEFT, new PlayerLeftData(playerId));
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify that the game started
     */
    public void notifyGameStarted(String roomCode) {
        broadcastEvent(roomCode, EVENT_GAME_STARTED, null);
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify that a question was sent
     */
    public void notifyQuestionSent(String roomCode, Object questionData) {
        broadcastEvent(roomCode, EVENT_QUESTION_SENT, questionData);
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify admin override
     */
    public void notifyAdminOverride(String roomCode, Object questionData) {
        broadcastEvent(roomCode, EVENT_ADMIN_OVERRIDE, questionData);
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify next turn
     */
    public void notifyNextTurn(String roomCode) {
        broadcastEvent(roomCode, EVENT_NEXT_TURN, null);
        // Also update room state
        var room = roomService.getRoomByCode(roomCode);
        if (room != null) {
            broadcastRoomState(room.getRoomId());
        }
    }
    
    /**
     * Notify game mode changed
     */
    public void notifyGameModeChanged(String roomCode, Object gameModeData) {
        broadcastEvent(roomCode, EVENT_GAME_MODE_CHANGED, gameModeData);
    }
    
    // Helper class for player left event
    private static class PlayerLeftData {
        public String playerId;
        
        public PlayerLeftData(String playerId) {
            this.playerId = playerId;
        }
    }
}