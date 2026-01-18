import { useState } from 'react'
import { api } from '../services/api'
import '../styles/AdminPanel.css'

function AdminPanel({ roomId, roomCode, adminToken, roomState }) {
  const [showPanel, setShowPanel] = useState(false)
  const [customQuestion, setCustomQuestion] = useState('')
  const [questionType, setQuestionType] = useState('TRUTH')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)

  const handleInjectQuestion = async (e) => {
    e.preventDefault()
    setError(null)
    setSuccess(null)
    setLoading(true)

    try {
      await api.injectQuestion(roomId, adminToken, customQuestion, questionType)
      setCustomQuestion('')
      setSuccess('Question injected successfully!')
      setTimeout(() => setSuccess(null), 3000)
    } catch (err) {
      setError(err.message || 'Failed to inject question')
    } finally {
      setLoading(false)
    }
  }

  const handleChangeGameMode = async (newMode) => {
    setError(null)
    setSuccess(null)
    setLoading(true)

    try {
      await api.changeGameMode(roomId, adminToken, newMode)
      setSuccess('Game mode changed successfully!')
      setTimeout(() => setSuccess(null), 3000)
    } catch (err) {
      setError(err.message || 'Failed to change game mode')
    } finally {
      setLoading(false)
    }
  }

  const handleForceNextTurn = async () => {
    setError(null)
    setSuccess(null)
    setLoading(true)

    try {
      await api.forceNextTurn(roomId, adminToken)
      setSuccess('Turn advanced successfully!')
      setTimeout(() => setSuccess(null), 3000)
    } catch (err) {
      setError(err.message || 'Failed to force next turn')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="admin-panel">
      <div className="card">
        <div className="admin-header">
          <h3>⚙️ Admin Panel</h3>
          <button
            className="btn btn-secondary"
            onClick={() => setShowPanel(!showPanel)}
          >
            {showPanel ? 'Hide' : 'Show'} Admin Controls
          </button>
        </div>

        {showPanel && (
          <div className="admin-controls">
            {error && <div className="error">{error}</div>}
            {success && <div className="success">{success}</div>}

            {/* Inject Custom Question */}
            <div className="admin-section">
              <h4>Inject Custom Question</h4>
              <form onSubmit={handleInjectQuestion}>
                <div className="input-group">
                  <label>Question Text</label>
                  <textarea
                    value={customQuestion}
                    onChange={(e) => setCustomQuestion(e.target.value)}
                    placeholder="Enter your custom question..."
                    rows={3}
                    required
                  />
                </div>

                <div className="input-group">
                  <label>Question Type</label>
                  <select
                    value={questionType}
                    onChange={(e) => setQuestionType(e.target.value)}
                  >
                    <option value="TRUTH">Truth</option>
                    <option value="DARE">Dare</option>
                  </select>
                </div>

                <button
                  type="submit"
                  className="btn"
                  disabled={loading}
                >
                  {loading ? 'Injecting...' : 'Inject Question'}
                </button>
              </form>
            </div>

            {/* Change Game Mode */}
            <div className="admin-section">
              <h4>Change Game Mode</h4>
              <div className="game-mode-buttons">
                <button
                  className={`btn ${roomState.gameMode === 'TRUTH_ONLY' ? 'active' : 'btn-secondary'}`}
                  onClick={() => handleChangeGameMode('TRUTH_ONLY')}
                  disabled={loading}
                >
                  Truth Only
                </button>
                <button
                  className={`btn ${roomState.gameMode === 'DARE_ONLY' ? 'active' : 'btn-secondary'}`}
                  onClick={() => handleChangeGameMode('DARE_ONLY')}
                  disabled={loading}
                >
                  Dare Only
                </button>
                <button
                  className={`btn ${roomState.gameMode === 'TRUTH_AND_DARE' ? 'active' : 'btn-secondary'}`}
                  onClick={() => handleChangeGameMode('TRUTH_AND_DARE')}
                  disabled={loading}
                >
                  Truth & Dare
                </button>
              </div>
            </div>

            {/* Force Next Turn */}
            <div className="admin-section">
              <h4>Game Controls</h4>
              <button
                className="btn btn-secondary"
                onClick={handleForceNextTurn}
                disabled={loading}
              >
                {loading ? 'Processing...' : 'Force Next Turn'}
              </button>
            </div>

            {/* Admin Info */}
            <div className="admin-info">
              <p><strong>Admin Token:</strong> {adminToken}</p>
              <p className="info-note">⚠️ Keep your admin token secret! It's required for admin operations.</p>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default AdminPanel