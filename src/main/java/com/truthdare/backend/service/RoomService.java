package com.truthdare.backend.service;

import com.truthdare.backend.model.GameMode;
import com.truthdare.backend.model.Player;
import com.truthdare.backend.model.Role;
import com.truthdare.backend.model.Room;
import com.truthdare.backend.util.RoomCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing rooms (in-memory storage)
 */
@Slf4j
@Service
public class RoomService {
    
    // In-memory storage: roomCode -> Room
    private final Map<String, Room> roomsByCode = new ConcurrentHashMap<>();
    
    // In-memory storage: roomId -> Room (for quick lookup)
    private final Map<String, Room> roomsById = new ConcurrentHashMap<>();
    
    /**
     * Create a new room
     * @param gameMode The game mode
     * @param adminName Name of the admin/creator
     * @return The created room
     */
    public Room createRoom(GameMode gameMode, String adminName) {
        String roomCode = generateUniqueRoomCode();
        String adminToken = RoomCodeGenerator.generateAdminToken();
        
        Room room = new Room(roomCode, adminToken, gameMode);
        
        // Create admin player
        Player admin = new Player(UUID.randomUUID().toString(), adminName, Role.ADMIN);
        room.addPlayer(admin);
        
        // Store room
        roomsByCode.put(roomCode, room);
        roomsById.put(room.getRoomId(), room);
        
        log.info("Created room: {} with code: {} by admin: {}", room.getRoomId(), roomCode, adminName);
        
        return room;
    }
    
    /**
     * Get room by code
     */
    public Room getRoomByCode(String roomCode) {
        return roomsByCode.get(roomCode);
    }
    
    /**
     * Get room by ID
     */
    public Room getRoomById(String roomId) {
        return roomsById.get(roomId);
    }
    
    /**
     * Add a player to a room
     * @param roomCode The room code
     * @param playerName The player's name
     * @return The created player, or null if room doesn't exist
     */
    public Player addPlayerToRoom(String roomCode, String playerName) {
        Room room = roomsByCode.get(roomCode);
        if (room == null) {
            return null;
        }
        
        // Check if player name already exists in room
        boolean nameExists = room.getPlayers().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(playerName));
        
        if (nameExists) {
            throw new IllegalArgumentException("Player name already exists in this room");
        }
        
        Player player = new Player(UUID.randomUUID().toString(), playerName, Role.PLAYER);
        room.addPlayer(player);
        
        log.info("Player {} joined room {}", playerName, roomCode);
        
        return player;
    }
    
    /**
     * Remove a player from a room
     */
    public boolean removePlayerFromRoom(String roomCode, String playerId) {
        Room room = roomsByCode.get(roomCode);
        if (room == null) {
            return false;
        }
        
        boolean removed = room.removePlayer(playerId);
        
        // If room is empty, clean it up (optional - you might want to keep it)
        if (room.getPlayers().isEmpty()) {
            roomsByCode.remove(roomCode);
            roomsById.remove(room.getRoomId());
            log.info("Room {} cleaned up (no players)", roomCode);
        }
        
        return removed;
    }
    
    /**
     * Verify admin token for a room
     */
    public boolean verifyAdminToken(String roomId, String adminToken) {
        Room room = roomsById.get(roomId);
        return room != null && room.isAdminTokenValid(adminToken);
    }
    
    /**
     * Generate a unique room code (retries if collision occurs)
     */
    private String generateUniqueRoomCode() {
        String code;
        int attempts = 0;
        do {
            code = RoomCodeGenerator.generateRoomCode();
            attempts++;
            if (attempts > 100) {
                throw new RuntimeException("Failed to generate unique room code after 100 attempts");
            }
        } while (roomsByCode.containsKey(code));
        
        return code;
    }
}