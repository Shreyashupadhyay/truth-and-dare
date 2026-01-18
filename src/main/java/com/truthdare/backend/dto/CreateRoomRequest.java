package com.truthdare.backend.dto;

import com.truthdare.backend.model.GameMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to create a new room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    @NotNull(message = "Game mode is required")
    private GameMode gameMode;
    
    private String playerName; // Name of the admin/creator
}