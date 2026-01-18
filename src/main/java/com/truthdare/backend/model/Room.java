package com.truthdare.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a game room
 */
@Data
@NoArgsConstructor
public class Room {
    private String roomId;
    private String roomCode; // Short code for players to join
    private String adminToken; // Secure token for admin access
    private GameMode gameMode;
    private RoomStatus status;
    private List<Player> players;
    private int currentTurnIndex; // Index in players list
    private Question currentQuestion;
    private ConcurrentLinkedQueue<Question> adminOverrideQueue; // Priority queue for admin-injected questions
    private LocalDateTime createdAt;
    private LocalDateTime lastActivityAt;
    
    public Room(String roomCode, String adminToken, GameMode gameMode) {
        this.roomId = java.util.UUID.randomUUID().toString();
        this.roomCode = roomCode;
        this.adminToken = adminToken;
        this.gameMode = gameMode;
        this.status = RoomStatus.WAITING;
        this.players = new ArrayList<>();
        this.currentTurnIndex = 0;
        this.adminOverrideQueue = new ConcurrentLinkedQueue<>();
        this.createdAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Add a player to the room
     */
    public void addPlayer(Player player) {
        this.players.add(player);
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Remove a player by ID
     */
    public boolean removePlayer(String playerId) {
        boolean removed = this.players.removeIf(p -> p.getPlayerId().equals(playerId));
        if (removed) {
            // Adjust turn index if needed
            if (this.currentTurnIndex >= this.players.size() && this.players.size() > 0) {
                this.currentTurnIndex = 0;
            }
            this.lastActivityAt = LocalDateTime.now();
        }
        return removed;
    }
    
    /**
     * Get the current player whose turn it is
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty() || currentTurnIndex < 0 || currentTurnIndex >= players.size()) {
            return null;
        }
        return players.get(currentTurnIndex);
    }
    
    /**
     * Move to the next turn
     */
    public void nextTurn() {
        if (players.isEmpty()) {
            return;
        }
        this.currentTurnIndex = (this.currentTurnIndex + 1) % players.size();
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Check if admin token is valid
     */
    public boolean isAdminTokenValid(String token) {
        return this.adminToken != null && this.adminToken.equals(token);
    }
    
    /**
     * Add an admin-injected question to the priority queue
     */
    public void addAdminQuestion(Question question) {
        this.adminOverrideQueue.offer(question);
        this.lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Poll an admin question from the queue (if available)
     */
    public Question pollAdminQuestion() {
        Question question = this.adminOverrideQueue.poll();
        if (question != null) {
            this.lastActivityAt = LocalDateTime.now();
        }
        return question;
    }
}