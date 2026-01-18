package com.truthdare.backend.dto;

import com.truthdare.backend.model.GameMode;
import com.truthdare.backend.model.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Current state of a room (sent via WebSocket)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStateDto {
    private String roomId;
    private String roomCode;
    private GameMode gameMode;
    private RoomStatus status;
    private List<PlayerDto> players;
    private PlayerDto currentPlayer;
    private QuestionDto currentQuestion;
    private int currentTurnIndex;
}