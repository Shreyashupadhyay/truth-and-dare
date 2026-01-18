package com.truthdare.backend.service;

import com.truthdare.backend.dto.RoomStateDto;
import com.truthdare.backend.dto.PlayerDto;
import com.truthdare.backend.dto.QuestionDto;
import com.truthdare.backend.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for game logic and state management
 */
@Slf4j
@Service
public class GameService {
    
    private final RoomService roomService;
    private final QuestionService questionService;
    
    public GameService(RoomService roomService, QuestionService questionService) {
        this.roomService = roomService;
        this.questionService = questionService;
    }
    
    /**
     * Start the game in a room
     */
    public boolean startGame(String roomId, String adminToken) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        
        if (!room.isAdminTokenValid(adminToken)) {
            throw new SecurityException("Invalid admin token");
        }
        
        if (room.getPlayers().size() < 2) {
            throw new IllegalStateException("Need at least 2 players to start");
        }
        
        if (room.getStatus() == RoomStatus.ACTIVE) {
            return false; // Game already started
        }
        
        room.setStatus(RoomStatus.ACTIVE);
        room.setCurrentTurnIndex(0);
        
        log.info("Game started in room {}", room.getRoomCode());
        
        return true;
    }
    
    /**
     * Get next question for the current turn
     */
    public Mono<Question> getNextQuestion(String roomId, QuestionType preferredType) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return Mono.error(new IllegalArgumentException("Room not found"));
        }
        
        if (room.getStatus() != RoomStatus.ACTIVE) {
            return Mono.error(new IllegalStateException("Game is not active"));
        }
        
        if (room.getCurrentPlayer() == null) {
            return Mono.error(new IllegalStateException("No current player"));
        }
        
        return questionService.getNextQuestion(room, preferredType)
                .doOnNext(question -> {
                    room.setCurrentQuestion(question);
                    log.debug("Set question for room {}: {}", room.getRoomCode(), question.getText());
                });
    }
    
    /**
     * Move to the next turn
     */
    public void nextTurn(String roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        
        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new IllegalStateException("Game is not active");
        }
        
        room.nextTurn();
        
        log.debug("Moved to next turn in room {}", room.getRoomCode());
    }
    
    /**
     * Change game mode (admin only)
     */
    public void changeGameMode(String roomId, String adminToken, GameMode newMode) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        
        if (!room.isAdminTokenValid(adminToken)) {
            throw new SecurityException("Invalid admin token");
        }
        
        room.setGameMode(newMode);
        
        log.info("Game mode changed to {} in room {}", newMode, room.getRoomCode());
    }
    
    /**
     * Convert Room to RoomStateDto
     */
    public RoomStateDto toRoomStateDto(Room room) {
        RoomStateDto dto = new RoomStateDto();
        dto.setRoomId(room.getRoomId());
        dto.setRoomCode(room.getRoomCode());
        dto.setGameMode(room.getGameMode());
        dto.setStatus(room.getStatus());
        dto.setCurrentTurnIndex(room.getCurrentTurnIndex());
        
        // Convert players
        List<PlayerDto> playerDtos = room.getPlayers().stream()
                .map(p -> new PlayerDto(p.getPlayerId(), p.getName(), p.getRole()))
                .collect(Collectors.toList());
        dto.setPlayers(playerDtos);
        
        // Current player
        Player currentPlayer = room.getCurrentPlayer();
        if (currentPlayer != null) {
            dto.setCurrentPlayer(new PlayerDto(
                    currentPlayer.getPlayerId(),
                    currentPlayer.getName(),
                    currentPlayer.getRole()
            ));
        }
        
        // Current question
        Question currentQuestion = room.getCurrentQuestion();
        if (currentQuestion != null) {
            dto.setCurrentQuestion(new QuestionDto(
                    currentQuestion.getQuestionId(),
                    currentQuestion.getText(),
                    currentQuestion.getType(),
                    currentQuestion.getPlayerId(),
                    currentQuestion.isAdminInjected()
            ));
        }
        
        return dto;
    }
}