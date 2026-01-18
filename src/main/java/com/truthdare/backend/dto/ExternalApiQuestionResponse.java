package com.truthdare.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from external Truth or Dare API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalApiQuestionResponse {
    private String id;
    private String question;
    private String type; // "truth" or "dare"
    private Integer rating;
}