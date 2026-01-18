package com.truthdare.backend.client;

import com.truthdare.backend.dto.ExternalApiQuestionResponse;
import com.truthdare.backend.model.QuestionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Client for external Truth or Dare API
 * API Documentation: https://docs.truthordarebot.xyz/api-docs
 */
@Slf4j
@Component
public class TruthDareApiClient {
    
    private static final String BASE_URL = "https://api.truthordarebot.xyz/v1";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    
    private final WebClient webClient;
    
    public TruthDareApiClient() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(256 * 1024))
                .build();
    }
    
    /**
     * Fetch a truth question from the external API
     * @return Question text or null if API call fails
     */
    public Mono<String> fetchTruthQuestion() {
        return fetchQuestion("truth")
                .map(ExternalApiQuestionResponse::getQuestion)
                .onErrorResume(error -> {
                    log.error("Error fetching truth question from API: {}", error.getMessage());
                    return Mono.empty();
                });
    }
    
    /**
     * Fetch a dare question from the external API
     * @return Question text or null if API call fails
     */
    public Mono<String> fetchDareQuestion() {
        return fetchQuestion("dare")
                .map(ExternalApiQuestionResponse::getQuestion)
                .onErrorResume(error -> {
                    log.error("Error fetching dare question from API: {}", error.getMessage());
                    return Mono.empty();
                });
    }
    
    /**
     * Fetch a random question (truth or dare) from the external API
     * @return Question text or null if API call fails
     */
    public Mono<String> fetchRandomQuestion() {
        return fetchQuestion("random")
                .map(ExternalApiQuestionResponse::getQuestion)
                .onErrorResume(error -> {
                    log.error("Error fetching random question from API: {}", error.getMessage());
                    return Mono.empty();
                });
    }
    
    /**
     * Fetch a question of the specified type
     * @param type "truth", "dare", or "random"
     * @return API response or empty on error
     */
    private Mono<ExternalApiQuestionResponse> fetchQuestion(String type) {
        return webClient.get()
                .uri("/{type}", type)
                .retrieve()
                .bodyToMono(ExternalApiQuestionResponse.class)
                .timeout(TIMEOUT)
                .doOnSuccess(response -> log.debug("Successfully fetched {} question from API", type))
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.error("API returned error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                    return ex;
                });
    }
    
    /**
     * Get a question based on game mode and question type preference
     * @param gameMode The current game mode
     * @param questionType Preferred question type (can be null for random)
     * @return Question text or null if API call fails
     */
    public Mono<String> getQuestionForGameMode(com.truthdare.backend.model.GameMode gameMode, QuestionType questionType) {
        if (gameMode == com.truthdare.backend.model.GameMode.TRUTH_ONLY) {
            return fetchTruthQuestion();
        } else if (gameMode == com.truthdare.backend.model.GameMode.DARE_ONLY) {
            return fetchDareQuestion();
        } else if (gameMode == com.truthdare.backend.model.GameMode.TRUTH_AND_DARE) {
            if (questionType == QuestionType.TRUTH) {
                return fetchTruthQuestion();
            } else if (questionType == QuestionType.DARE) {
                return fetchDareQuestion();
            } else {
                return fetchRandomQuestion();
            }
        }
        return fetchRandomQuestion();
    }
}