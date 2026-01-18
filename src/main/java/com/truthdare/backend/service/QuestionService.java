package com.truthdare.backend.service;

import com.truthdare.backend.client.TruthDareApiClient;
import com.truthdare.backend.model.GameMode;
import com.truthdare.backend.model.Question;
import com.truthdare.backend.model.QuestionType;
import com.truthdare.backend.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Service for managing questions with priority strategy:
 * 1. Admin injected question (highest priority)
 * 2. External API question
 * 3. Local fallback question
 */
@Slf4j
@Service
public class QuestionService {
    
    private final TruthDareApiClient apiClient;
    private final Random random = new Random();
    
    // Local fallback questions
    private static final List<String> FALLBACK_TRUTH_QUESTIONS = Arrays.asList(
            "What's the most embarrassing thing that's ever happened to you?",
            "What's a secret you've never told anyone?",
            "Who was your first crush?",
            "What's your biggest fear?",
            "What's the worst lie you've ever told?",
            "What's something you're ashamed of?",
            "Who do you have a crush on right now?",
            "What's the most trouble you've ever gotten into?",
            "What's one thing you wish you could change about yourself?",
            "What's your guilty pleasure?"
    );
    
    private static final List<String> FALLBACK_DARE_QUESTIONS = Arrays.asList(
            "Do your best impression of someone in the room",
            "Call your ex and tell them you miss them",
            "Eat a spoonful of a condiment of the group's choice",
            "Let someone go through your phone for 1 minute",
            "Do 20 push-ups",
            "Sing a song chosen by the group",
            "Dance with no music for 1 minute",
            "Let the group post a status on your social media",
            "Text someone you haven't talked to in a year",
            "Do your best celebrity impression"
    );
    
    public QuestionService(TruthDareApiClient apiClient) {
        this.apiClient = apiClient;
    }
    
    /**
     * Get the next question for a room using priority strategy
     * @param room The room
     * @param preferredType Preferred question type (for TRUTH_AND_DARE mode)
     * @return A Mono containing the question
     */
    public Mono<Question> getNextQuestion(Room room, QuestionType preferredType) {
        String currentPlayerId = room.getCurrentPlayer() != null 
                ? room.getCurrentPlayer().getPlayerId() 
                : null;
        
        // Priority 1: Check for admin-injected question
        Question adminQuestion = room.pollAdminQuestion();
        if (adminQuestion != null) {
            // Update player ID if needed
            if (adminQuestion.getPlayerId() == null && currentPlayerId != null) {
                adminQuestion.setPlayerId(currentPlayerId);
            }
            log.debug("Using admin-injected question for room {}", room.getRoomCode());
            return Mono.just(adminQuestion);
        }
        
        // Priority 2: Try external API
        Mono<String> apiQuestionMono = apiClient.getQuestionForGameMode(room.getGameMode(), preferredType)
                .doOnSuccess(q -> log.debug("Fetched question from external API"))
                .onErrorResume(e -> {
                    log.warn("External API failed, falling back to local questions: {}", e.getMessage());
                    return Mono.empty();
                });
        
        // Priority 3: Fallback to local questions if API fails
        Mono<Question> questionMono = apiQuestionMono
                .switchIfEmpty(Mono.fromCallable(() -> getFallbackQuestion(room.getGameMode(), preferredType)))
                .map(text -> {
                    QuestionType type = determineQuestionType(room.getGameMode(), preferredType, text);
                    Question question = new Question(text, type, currentPlayerId, false);
                    return question;
                });
        
        return questionMono;
    }
    
    /**
     * Get a fallback question from local pool
     */
    private String getFallbackQuestion(GameMode gameMode, QuestionType preferredType) {
        if (gameMode == GameMode.TRUTH_ONLY) {
            return FALLBACK_TRUTH_QUESTIONS.get(random.nextInt(FALLBACK_TRUTH_QUESTIONS.size()));
        } else if (gameMode == GameMode.DARE_ONLY) {
            return FALLBACK_DARE_QUESTIONS.get(random.nextInt(FALLBACK_DARE_QUESTIONS.size()));
        } else {
            // TRUTH_AND_DARE mode
            if (preferredType == QuestionType.TRUTH) {
                return FALLBACK_TRUTH_QUESTIONS.get(random.nextInt(FALLBACK_TRUTH_QUESTIONS.size()));
            } else if (preferredType == QuestionType.DARE) {
                return FALLBACK_DARE_QUESTIONS.get(random.nextInt(FALLBACK_DARE_QUESTIONS.size()));
            } else {
                // Random
                boolean isTruth = random.nextBoolean();
                List<String> questions = isTruth ? FALLBACK_TRUTH_QUESTIONS : FALLBACK_DARE_QUESTIONS;
                return questions.get(random.nextInt(questions.size()));
            }
        }
    }
    
    /**
     * Determine question type based on game mode and context
     */
    private QuestionType determineQuestionType(GameMode gameMode, QuestionType preferredType, String questionText) {
        if (gameMode == GameMode.TRUTH_ONLY) {
            return QuestionType.TRUTH;
        } else if (gameMode == GameMode.DARE_ONLY) {
            return QuestionType.DARE;
        } else {
            // TRUTH_AND_DARE mode - try to infer from text or use preferred type
            String lowerText = questionText.toLowerCase();
            if (lowerText.contains("truth") || lowerText.contains("confess") || lowerText.contains("tell")) {
                return QuestionType.TRUTH;
            } else if (lowerText.contains("dare") || lowerText.contains("do") || lowerText.contains("perform")) {
                return QuestionType.DARE;
            } else if (preferredType != null) {
                return preferredType;
            } else {
                // Default to random
                return random.nextBoolean() ? QuestionType.TRUTH : QuestionType.DARE;
            }
        }
    }
}