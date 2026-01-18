package com.truthdare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response after creating a room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomResponse {
    private String roomId;
    private String roomCode;
    private String adminToken; // Admin must save this securely
    private String playerId; // Admin's player ID
}