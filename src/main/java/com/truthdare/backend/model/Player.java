package com.truthdare.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a player in a room
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String playerId;
    private String name;
    private Role role;
    private LocalDateTime joinedAt;
    
    public Player(String playerId, String name, Role role) {
        this.playerId = playerId;
        this.name = name;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }
}