package com.truthdare.backend.controller;

import com.truthdare.backend.dto.*;
import com.truthdare.backend.model.Player;
import com.truthdare.backend.model.Room;
import com.truthdare.backend.service.GameService;
import com.truthdare.backend.service.RoomService;
import com.truthdare.backend.websocket.WebSocketService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for room management
 */
@Slf4j
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*") // In production, restrict this to your frontend domain
public class RoomController {
    
    private final RoomService roomService;
    private final WebSocketService webSocketService;
    private final GameService gameService;
    
    public RoomController(RoomService roomService, WebSocketService webSocketService, GameService gameService) {
        this.roomService = roomService;
        this.webSocketService = webSocketService;
        this.gameService = gameService;
    }
    
    /**
     * Create a new room
     * POST /api/rooms
     */
    @PostMapping
    public ResponseEntity<CreateRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        try {
            String playerName = request.getPlayerName();
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Admin";
            }
            
            Room room = roomService.createRoom(request.getGameMode(), playerName);
            
            // Find the admin player
            Player admin = room.getPlayers().stream()
                    .filter(p -> p.getRole() == com.truthdare.backend.model.Role.ADMIN)
                    .findFirst()
                    .orElse(null);
            
            CreateRoomResponse response = new CreateRoomResponse(
                    room.getRoomId(),
                    room.getRoomCode(),
                    room.getAdminToken(),
                    admin != null ? admin.getPlayerId() : null
            );
            
            // Notify via WebSocket
            webSocketService.notifyRoomCreated(room.getRoomCode(), response);
            webSocketService.broadcastRoomState(room.getRoomId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating room", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Join a room
     * POST /api/rooms/join
     */
    @PostMapping("/join")
    public ResponseEntity<JoinRoomResponse> joinRoom(@Valid @RequestBody JoinRoomRequest request) {
        try {
            Room room = roomService.getRoomByCode(request.getRoomCode().toUpperCase());
            
            if (room == null) {
                JoinRoomResponse response = new JoinRoomResponse();
                response.setSuccess(false);
                response.setMessage("Room not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            if (room.getStatus() == com.truthdare.backend.model.RoomStatus.ACTIVE) {
                JoinRoomResponse response = new JoinRoomResponse();
                response.setSuccess(false);
                response.setMessage("Game has already started");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Player player = roomService.addPlayerToRoom(request.getRoomCode().toUpperCase(), request.getPlayerName());
            
            if (player == null) {
                JoinRoomResponse response = new JoinRoomResponse();
                response.setSuccess(false);
                response.setMessage("Failed to join room");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
            JoinRoomResponse response = new JoinRoomResponse();
            response.setRoomId(room.getRoomId());
            response.setRoomCode(room.getRoomCode());
            response.setPlayerId(player.getPlayerId());
            response.setSuccess(true);
            response.setMessage("Successfully joined room");
            
            // Notify via WebSocket
            webSocketService.notifyPlayerJoined(room.getRoomCode(), new PlayerDto(
                    player.getPlayerId(),
                    player.getName(),
                    player.getRole()
            ));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            JoinRoomResponse response = new JoinRoomResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error joining room", e);
            JoinRoomResponse response = new JoinRoomResponse();
            response.setSuccess(false);
            response.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get room state
     * GET /api/rooms/{roomCode}/state
     */
    @GetMapping("/{roomCode}/state")
    public ResponseEntity<RoomStateDto> getRoomState(@PathVariable String roomCode) {
        Room room = roomService.getRoomByCode(roomCode.toUpperCase());
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        
        RoomStateDto state = gameService.toRoomStateDto(room);
        return ResponseEntity.ok(state);
    }
}