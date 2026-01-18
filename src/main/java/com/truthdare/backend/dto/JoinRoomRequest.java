package com.truthdare.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to join a room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomRequest {
    @NotBlank(message = "Room code is required")
    private String roomCode;
    
    @NotBlank(message = "Player name is required")
    private String playerName;
}