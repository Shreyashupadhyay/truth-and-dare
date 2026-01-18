package com.truthdare.backend.dto;

import com.truthdare.backend.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Question information DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String questionId;
    private String text;
    private QuestionType type;
    private String playerId;
    private boolean isAdminInjected;
}