package com.truthdare.backend.util;

import java.security.SecureRandom;

/**
 * Utility class for generating room codes and secure tokens
 */
public class RoomCodeGenerator {
    
    private static final String ROOM_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ROOM_CODE_LENGTH = 6;
    private static final int ADMIN_TOKEN_LENGTH = 32;
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Generate a random 6-character room code (e.g., "ABC123")
     */
    public static String generateRoomCode() {
        StringBuilder code = new StringBuilder(ROOM_CODE_LENGTH);
        for (int i = 0; i < ROOM_CODE_LENGTH; i++) {
            code.append(ROOM_CODE_CHARS.charAt(random.nextInt(ROOM_CODE_CHARS.length())));
        }
        return code.toString();
    }
    
    /**
     * Generate a secure random admin token
     */
    public static String generateAdminToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder(ADMIN_TOKEN_LENGTH);
        for (int i = 0; i < ADMIN_TOKEN_LENGTH; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        return token.toString();
    }
}