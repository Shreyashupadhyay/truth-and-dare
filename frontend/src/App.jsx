import { useState } from 'react'
import Landing from './components/Landing'
import GameRoom from './components/GameRoom'
import './App.css'

function App() {
  console.log('App component is rendering!') // Debug log
  
  const [currentScreen, setCurrentScreen] = useState('landing')
  const [roomData, setRoomData] = useState(null)

  const handleRoomCreated = (data) => {
    console.log('Room created:', data) // Debug log
    setRoomData(data)
    setCurrentScreen('game')
  }

  const handleRoomJoined = (data) => {
    console.log('Room joined:', data) // Debug log
    setRoomData(data)
    setCurrentScreen('game')
  }

  const handleBackToLanding = () => {
    setCurrentScreen('landing')
    setRoomData(null)
  }

  // Simple render test
  console.log('Current screen:', currentScreen)

  return (
    <div className="App">
      {currentScreen === 'landing' ? (
        <Landing
          onRoomCreated={handleRoomCreated}
          onRoomJoined={handleRoomJoined}
        />
      ) : roomData ? (
        <GameRoom
          roomData={roomData}
          onLeave={handleBackToLanding}
        />
      ) : (
        <div className="container">
          <div className="card">
            <div className="error">No room data available</div>
            <button className="btn" onClick={handleBackToLanding}>
              Go Back
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default App