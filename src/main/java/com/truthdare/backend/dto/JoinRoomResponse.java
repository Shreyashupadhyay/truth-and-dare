package com.truthdare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response after joining a room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomResponse {
    private String roomId;
    private String roomCode;
    private String playerId;
    private boolean success;
    private String message;
}