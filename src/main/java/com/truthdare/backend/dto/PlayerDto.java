package com.truthdare.backend.dto;

import com.truthdare.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Player information DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private String playerId;
    private String name;
    private Role role;
}