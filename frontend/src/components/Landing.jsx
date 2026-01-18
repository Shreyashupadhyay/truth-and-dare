import { useState } from 'react'
import { api } from '../services/api'
import '../styles/Landing.css'

function Landing({ onRoomCreated, onRoomJoined }) {
  const [mode, setMode] = useState('create') // 'create' or 'join'
  const [gameMode, setGameMode] = useState('TRUTH_AND_DARE')
  const [playerName, setPlayerName] = useState('')
  const [roomCode, setRoomCode] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleCreateRoom = async (e) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    try {
      const data = await api.createRoom(gameMode, playerName)
      onRoomCreated(data)
    } catch (err) {
      setError(err.message || 'Failed to create room')
    } finally {
      setLoading(false)
    }
  }

  const handleJoinRoom = async (e) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    try {
      const data = await api.joinRoom(roomCode, playerName)
      onRoomJoined(data)
    } catch (err) {
      setError(err.message || 'Failed to join room')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="landing">
      <div className="container">
        <div className="card">
          <h1>🎲 Truth & Dare</h1>
          <p className="subtitle">A fun multiplayer game!</p>

          <div className="mode-tabs">
            <button
              className={`tab ${mode === 'create' ? 'active' : ''}`}
              onClick={() => setMode('create')}
            >
              Create Room
            </button>
            <button
              className={`tab ${mode === 'join' ? 'active' : ''}`}
              onClick={() => setMode('join')}
            >
              Join Room
            </button>
          </div>

          {error && <div className="error">{error}</div>}

          {mode === 'create' ? (
            <form onSubmit={handleCreateRoom}>
              <div className="input-group">
                <label>Your Name</label>
                <input
                  type="text"
                  value={playerName}
                  onChange={(e) => setPlayerName(e.target.value)}
                  placeholder="Enter your name"
                  required
                />
              </div>

              <div className="input-group">
                <label>Game Mode</label>
                <select
                  value={gameMode}
                  onChange={(e) => setGameMode(e.target.value)}
                >
                  <option value="TRUTH_ONLY">Truth Only</option>
                  <option value="DARE_ONLY">Dare Only</option>
                  <option value="TRUTH_AND_DARE">Truth & Dare</option>
                </select>
              </div>

              <button
                type="submit"
                className="btn"
                disabled={loading}
              >
                {loading ? 'Creating...' : 'Create Room'}
              </button>
            </form>
          ) : (
            <form onSubmit={handleJoinRoom}>
              <div className="input-group">
                <label>Your Name</label>
                <input
                  type="text"
                  value={playerName}
                  onChange={(e) => setPlayerName(e.target.value)}
                  placeholder="Enter your name"
                  required
                />
              </div>

              <div className="input-group">
                <label>Room Code</label>
                <input
                  type="text"
                  value={roomCode}
                  onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
                  placeholder="Enter room code"
                  maxLength={6}
                  required
                />
              </div>

              <button
                type="submit"
                className="btn"
                disabled={loading}
              >
                {loading ? 'Joining...' : 'Join Room'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  )
}

export default Landing