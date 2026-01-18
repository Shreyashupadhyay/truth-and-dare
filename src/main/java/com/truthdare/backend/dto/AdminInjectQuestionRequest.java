package com.truthdare.backend.dto;

import com.truthdare.backend.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to inject a custom question (admin only)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInjectQuestionRequest {
    @NotBlank(message = "Question text is required")
    private String questionText;
    
    @NotNull(message = "Question type is required")
    private QuestionType questionType;
    
    private String targetPlayerId; // If null, applies to current player
}