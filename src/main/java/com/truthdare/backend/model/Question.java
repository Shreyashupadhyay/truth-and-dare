package com.truthdare.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a question (truth or dare)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String questionId;
    private String text;
    private QuestionType type;
    private String playerId; // Which player this question is for
    private boolean isAdminInjected; // Whether this was injected by admin
    
    public Question(String text, QuestionType type, String playerId, boolean isAdminInjected) {
        this.questionId = java.util.UUID.randomUUID().toString();
        this.text = text;
        this.type = type;
        this.playerId = playerId;
        this.isAdminInjected = isAdminInjected;
    }
}