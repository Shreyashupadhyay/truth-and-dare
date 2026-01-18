package com.truthdare.backend.model;

/**
 * Room lifecycle status
 */
public enum RoomStatus {
    WAITING,    // Room created, waiting for players
    ACTIVE      // Game is in progress
}