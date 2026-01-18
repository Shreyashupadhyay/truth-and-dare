import { useState, useEffect } from 'react'
import { api } from '../services/api'
import websocket from '../services/websocket'
import AdminPanel from './AdminPanel'
import '../styles/GameRoom.css'

function GameRoom({ roomData, onLeave }) {
  const [roomState, setRoomState] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [isAdmin, setIsAdmin] = useState(false)

  const refreshRoomState = async () => {
    try {
      const state = await api.getRoomState(roomData.roomCode)
      setRoomState(state)
      setIsAdmin(state.players?.some(p => p.playerId === roomData.playerId && p.role === 'ADMIN'))
    } catch (err) {
      console.error('Failed to refresh room state:', err)
    }
  }

  const handleWebSocketMessage = (event) => {
    console.log('WebSocket event received:', event)
    
    if (event.eventType === 'ROOM_STATE') {
      setRoomState(event.data)
    } else if (event.eventType === 'PLAYER_JOINED') {
      // Refresh room state
      refreshRoomState()
    } else if (event.eventType === 'GAME_STARTED') {
      refreshRoomState()
    } else if (event.eventType === 'QUESTION_SENT' || event.eventType === 'ADMIN_OVERRIDE') {
      refreshRoomState()
    } else if (event.eventType === 'NEXT_TURN') {
      refreshRoomState()
    }
  }

  useEffect(() => {
    // Fetch initial room state
    const fetchRoomState = async () => {
      try {
        const state = await api.getRoomState(roomData.roomCode)
        setRoomState(state)
        setIsAdmin(state.players?.some(p => p.playerId === roomData.playerId && p.role === 'ADMIN'))
        setLoading(false)
      } catch (err) {
        setError(err.message || 'Failed to load room')
        setLoading(false)
      }
    }

    fetchRoomState()

    // Connect to WebSocket
    websocket.connect(roomData.roomCode, handleWebSocketMessage)

    // Cleanup on unmount
    return () => {
      websocket.disconnect()
    }
  }, [roomData])

  const handleStartGame = async () => {
    if (!roomData.adminToken) {
      setError('Admin token not available')
      return
    }

    try {
      await api.startGame(roomData.roomId, roomData.adminToken)
      // WebSocket will update the state
    } catch (err) {
      setError(err.message || 'Failed to start game')
    }
  }

  const handleNextQuestion = async () => {
    try {
      await api.getNextQuestion(roomData.roomId)
      // WebSocket will update the state
    } catch (err) {
      setError(err.message || 'Failed to get next question')
    }
  }

  const handleNextTurn = async () => {
    try {
      await api.nextTurn(roomData.roomId)
      // WebSocket will update the state
    } catch (err) {
      setError(err.message || 'Failed to move to next turn')
    }
  }

  if (loading) {
    return (
      <div className="container">
        <div className="card">
          <div className="loading">Loading room...</div>
        </div>
      </div>
    )
  }

  if (error && !roomState) {
    return (
      <div className="container">
        <div className="card">
          <div className="error">{error}</div>
          <button className="btn" onClick={onLeave}>
            Go Back
          </button>
        </div>
      </div>
    )
  }

  if (!roomState) {
    return null
  }

  return (
    <div className="game-room">
      <div className="container">
        {/* Header */}
        <div className="card">
          <div className="room-header">
            <div>
              <h2>Room: {roomState.roomCode}</h2>
              <p className="room-status">
                Status: <span className={`status-badge ${roomState.status.toLowerCase()}`}>
                  {roomState.status}
                </span>
              </p>
              <p className="game-mode">Mode: {roomState.gameMode.replace(/_/g, ' ')}</p>
            </div>
            <button className="btn btn-secondary" onClick={onLeave}>
              Leave Room
            </button>
          </div>
        </div>

        {/* Players List */}
        <div className="card">
          <h3>Players ({roomState.players.length})</h3>
          <div className="players-list">
            {roomState.players.map((player) => (
              <div
                key={player.playerId}
                className={`player-item ${
                  roomState.currentPlayer?.playerId === player.playerId ? 'current' : ''
                }`}
              >
                <span className="player-name">{player.name}</span>
                {player.role === 'ADMIN' && <span className="badge">Admin</span>}
                {roomState.currentPlayer?.playerId === player.playerId && (
                  <span className="badge current-badge">Current Turn</span>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Current Question */}
        {roomState.status === 'ACTIVE' && roomState.currentQuestion && (
          <div className="card question-card">
            <h3>Current Question</h3>
            <div className={`question ${roomState.currentQuestion.type.toLowerCase()}`}>
              <div className="question-type">
                {roomState.currentQuestion.type === 'TRUTH' ? '💬 TRUTH' : '🎯 DARE'}
                {roomState.currentQuestion.isAdminInjected && (
                  <span className="admin-badge">Admin Injected</span>
                )}
              </div>
              <p className="question-text">{roomState.currentQuestion.text}</p>
              {roomState.currentQuestion.playerId && (
                <p className="question-for">
                  For: {roomState.players.find(p => p.playerId === roomState.currentQuestion.playerId)?.name}
                </p>
              )}
            </div>
          </div>
        )}

        {/* Game Controls */}
        <div className="card">
          {roomState.status === 'WAITING' ? (
            <div>
              {isAdmin && (
                <button className="btn" onClick={handleStartGame}>
                  Start Game
                </button>
              )}
              <p>Waiting for admin to start the game...</p>
            </div>
          ) : (
            <div className="game-controls">
              {isAdmin ? (
                <>
                  <button className="btn" onClick={handleNextQuestion}>
                    Get Next Question
                  </button>
                  <button className="btn btn-secondary" onClick={handleNextTurn}>
                    Next Turn
                  </button>
                </>
              ) : (
                <button className="btn btn-secondary" onClick={handleNextTurn}>
                  Next Turn
                </button>
              )}
            </div>
          )}
        </div>

        {/* Admin Panel */}
        {isAdmin && roomData.adminToken && (
          <AdminPanel
            roomId={roomData.roomId}
            roomCode={roomState.roomCode}
            adminToken={roomData.adminToken}
            roomState={roomState}
          />
        )}

        {error && <div className="error">{error}</div>}
      </div>
    </div>
  )
}

export default GameRoom