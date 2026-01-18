package com.truthdare.backend.controller;

import com.truthdare.backend.dto.QuestionDto;
import com.truthdare.backend.model.Question;
import com.truthdare.backend.model.QuestionType;
import com.truthdare.backend.service.GameService;
import com.truthdare.backend.service.RoomService;
import com.truthdare.backend.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for game actions
 */
@Slf4j
@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {
    
    private final GameService gameService;
    private final RoomService roomService;
    private final WebSocketService webSocketService;
    
    public GameController(GameService gameService, RoomService roomService, WebSocketService webSocketService) {
        this.gameService = gameService;
        this.roomService = roomService;
        this.webSocketService = webSocketService;
    }
    
    /**
     * Start the game
     * POST /api/game/{roomId}/start
     */
    @PostMapping("/{roomId}/start")
    public ResponseEntity<Void> startGame(
            @PathVariable String roomId,
            @RequestHeader("X-Admin-Token") String adminToken) {
        try {
            boolean started = gameService.startGame(roomId, adminToken);
            
            if (!started) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            var room = roomService.getRoomById(roomId);
            if (room != null) {
                webSocketService.notifyGameStarted(room.getRoomCode());
            }
            
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Get next question
     * GET /api/game/{roomId}/question?type=TRUTH
     */
    @GetMapping("/{roomId}/question")
    public ResponseEntity<QuestionDto> getNextQuestion(
            @PathVariable String roomId,
            @RequestParam(required = false) QuestionType type) {
        try {
            return gameService.getNextQuestion(roomId, type)
                    .map(question -> {
                        QuestionDto dto = new QuestionDto(
                                question.getQuestionId(),
                                question.getText(),
                                question.getType(),
                                question.getPlayerId(),
                                question.isAdminInjected()
                        );
                        
                        var room = roomService.getRoomById(roomId);
                        if (room != null) {
                            webSocketService.notifyQuestionSent(room.getRoomCode(), dto);
                        }
                        
                        return ResponseEntity.ok(dto);
                    })
                    .block();
        } catch (Exception e) {
            log.error("Error getting next question", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Move to next turn
     * POST /api/game/{roomId}/next-turn
     */
    @PostMapping("/{roomId}/next-turn")
    public ResponseEntity<Void> nextTurn(@PathVariable String roomId) {
        try {
            gameService.nextTurn(roomId);
            
            var room = roomService.getRoomById(roomId);
            if (room != null) {
                webSocketService.notifyNextTurn(room.getRoomCode());
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error moving to next turn", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}