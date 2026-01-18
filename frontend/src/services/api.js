// Use environment variable or default to relative path (same origin in production)
const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'

/**
 * API service for backend communication
 */
export const api = {
  /**
   * Create a new room
   */
  async createRoom(gameMode, playerName) {
    const response = await fetch(`${API_BASE_URL}/rooms`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        gameMode,
        playerName: playerName || 'Admin',
      }),
    })
    
    if (!response.ok) {
      throw new Error('Failed to create room')
    }
    
    return response.json()
  },

  /**
   * Join a room
   */
  async joinRoom(roomCode, playerName) {
    const response = await fetch(`${API_BASE_URL}/rooms/join`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        roomCode: roomCode.toUpperCase(),
        playerName,
      }),
    })
    
    const data = await response.json()
    
    if (!response.ok) {
      throw new Error(data.message || 'Failed to join room')
    }
    
    return data
  },

  /**
   * Get room state
   */
  async getRoomState(roomCode) {
    const response = await fetch(`${API_BASE_URL}/rooms/${roomCode}/state`)
    
    if (!response.ok) {
      throw new Error('Failed to get room state')
    }
    
    return response.json()
  },

  /**
   * Start game
   */
  async startGame(roomId, adminToken) {
    const response = await fetch(`${API_BASE_URL}/game/${roomId}/start`, {
      method: 'POST',
      headers: {
        'X-Admin-Token': adminToken,
      },
    })
    
    if (!response.ok) {
      throw new Error('Failed to start game')
    }
  },

  /**
   * Get next question
   */
  async getNextQuestion(roomId, type = null) {
    const params = type ? `?type=${type}` : ''
    const response = await fetch(`${API_BASE_URL}/game/${roomId}/question${params}`)
    
    if (!response.ok) {
      throw new Error('Failed to get question')
    }
    
    return response.json()
  },

  /**
   * Move to next turn
   */
  async nextTurn(roomId) {
    const response = await fetch(`${API_BASE_URL}/game/${roomId}/next-turn`, {
      method: 'POST',
    })
    
    if (!response.ok) {
      throw new Error('Failed to move to next turn')
    }
  },

  /**
   * Admin: Inject question
   */
  async injectQuestion(roomId, adminToken, questionText, questionType, targetPlayerId = null) {
    const response = await fetch(`${API_BASE_URL}/admin/${roomId}/inject-question`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Admin-Token': adminToken,
      },
      body: JSON.stringify({
        questionText,
        questionType,
        targetPlayerId,
      }),
    })
    
    if (!response.ok) {
      throw new Error('Failed to inject question')
    }
    
    return response.json()
  },

  /**
   * Admin: Change game mode
   */
  async changeGameMode(roomId, adminToken, gameMode) {
    const response = await fetch(`${API_BASE_URL}/admin/${roomId}/game-mode`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'X-Admin-Token': adminToken,
      },
      body: JSON.stringify({
        gameMode,
      }),
    })
    
    if (!response.ok) {
      throw new Error('Failed to change game mode')
    }
  },

  /**
   * Admin: Force next turn
   */
  async forceNextTurn(roomId, adminToken) {
    const response = await fetch(`${API_BASE_URL}/admin/${roomId}/force-next-turn`, {
      method: 'POST',
      headers: {
        'X-Admin-Token': adminToken,
      },
    })
    
    if (!response.ok) {
      throw new Error('Failed to force next turn')
    }
  },
}