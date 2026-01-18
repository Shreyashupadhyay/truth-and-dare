package com.truthdare.backend.controller;

import com.truthdare.backend.dto.AdminInjectQuestionRequest;
import com.truthdare.backend.dto.QuestionDto;
import com.truthdare.backend.model.GameMode;
import com.truthdare.backend.model.Question;
import com.truthdare.backend.model.QuestionType;
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
 * REST controller for admin operations
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    private final RoomService roomService;
    private final GameService gameService;
    private final WebSocketService webSocketService;
    
    public AdminController(RoomService roomService, GameService gameService, WebSocketService webSocketService) {
        this.roomService = roomService;
        this.gameService = gameService;
        this.webSocketService = webSocketService;
    }
    
    /**
     * Inject a custom question (admin only)
     * POST /api/admin/{roomId}/inject-question
     */
    @PostMapping("/{roomId}/inject-question")
    public ResponseEntity<QuestionDto> injectQuestion(
            @PathVariable String roomId,
            @RequestHeader("X-Admin-Token") String adminToken,
            @Valid @RequestBody AdminInjectQuestionRequest request) {
        try {
            Room room = roomService.getRoomById(roomId);
            if (room == null) {
                return ResponseEntity.notFound().build();
            }
            
            if (!room.isAdminTokenValid(adminToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Sanitize input
            String questionText = request.getQuestionText().trim();
            if (questionText.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            // Determine target player
            String targetPlayerId = request.getTargetPlayerId();
            if (targetPlayerId == null) {
                // Default to current player
                if (room.getCurrentPlayer() != null) {
                    targetPlayerId = room.getCurrentPlayer().getPlayerId();
                }
            }
            
            // Create admin-injected question
            Question question = new Question(
                    questionText,
                    request.getQuestionType(),
                    targetPlayerId,
                    true // Mark as admin-injected
            );
            
            // Add to priority queue
            room.addAdminQuestion(question);
            
            // If game is active, immediately use this question
            if (room.getStatus() == com.truthdare.backend.model.RoomStatus.ACTIVE) {
                room.setCurrentQuestion(question);
            }
            
            QuestionDto dto = new QuestionDto(
                    question.getQuestionId(),
                    question.getText(),
                    question.getType(),
                    question.getPlayerId(),
                    question.isAdminInjected()
            );
            
            // Broadcast immediately via WebSocket
            webSocketService.notifyAdminOverride(room.getRoomCode(), dto);
            webSocketService.broadcastRoomState(room.getRoomId());
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error injecting question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Change game mode (admin only)
     * PUT /api/admin/{roomId}/game-mode
     */
    @PutMapping("/{roomId}/game-mode")
    public ResponseEntity<Void> changeGameMode(
            @PathVariable String roomId,
            @RequestHeader("X-Admin-Token") String adminToken,
            @RequestBody GameModeRequest request) {
        try {
            gameService.changeGameMode(roomId, adminToken, request.getGameMode());
            
            Room room = roomService.getRoomById(roomId);
            if (room != null) {
                webSocketService.notifyGameModeChanged(room.getRoomCode(), request);
                webSocketService.broadcastRoomState(room.getRoomId());
            }
            
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Force next turn (admin only)
     * POST /api/admin/{roomId}/force-next-turn
     */
    @PostMapping("/{roomId}/force-next-turn")
    public ResponseEntity<Void> forceNextTurn(
            @PathVariable String roomId,
            @RequestHeader("X-Admin-Token") String adminToken) {
        try {
            Room room = roomService.getRoomById(roomId);
            if (room == null) {
                return ResponseEntity.notFound().build();
            }
            
            if (!room.isAdminTokenValid(adminToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            gameService.nextTurn(roomId);
            
            webSocketService.notifyNextTurn(room.getRoomCode());
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error forcing next turn", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Helper class for game mode request
    private static class GameModeRequest {
        private GameMode gameMode;
        
        public GameMode getGameMode() {
            return gameMode;
        }
        
        public void setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
        }
    }
}