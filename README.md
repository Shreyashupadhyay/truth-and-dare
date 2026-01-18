# Truth & Dare Web Application

A real-time multiplayer Truth & Dare game built with Java Spring Boot backend and React frontend. Players can create or join rooms using room codes, and admins have full control over the game flow including injecting custom questions in real-time.

## 🎯 Features

- **Multi-room Support**: Create or join game rooms using 6-character room codes
- **Real-time Updates**: WebSocket-based real-time synchronization across all players
- **Admin Controls**: Full admin control panel with ability to:
  - Inject custom questions (immediately overrides current question)
  - Change game mode (Truth Only, Dare Only, Truth & Dare)
  - Force next turn
  - Start the game
- **Question Priority Strategy**:
  1. Admin-injected questions (highest priority)
  2. External API questions (from truthordarebot.xyz)
  3. Local fallback questions
- **Game Modes**:
  - Truth Only
  - Dare Only
  - Truth & Dare (mixed)

## 🏗️ Architecture

### Backend
- **Java 17** with **Spring Boot 3.5.9**
- **Spring WebSocket (STOMP)** for real-time communication
- **Spring Security** for CORS configuration
- **WebClient** for external API integration
- **In-memory storage** (ConcurrentHashMap) - no database required

### Frontend
- **React 18** with **Vite**
- **STOMP.js** for WebSocket client
- Modern, responsive UI

## 📁 Project Structure

```
backend/
├── src/main/java/com/truthdare/backend/
│   ├── client/          # External API client
│   ├── config/          # Spring configuration (WebSocket, Security)
│   ├── controller/      # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── model/           # Domain models (Room, Player, Question, etc.)
│   ├── service/         # Business logic services
│   ├── util/            # Utility classes
│   └── websocket/       # WebSocket service for broadcasting
└── build.gradle

frontend/
├── src/
│   ├── components/      # React components (Landing, GameRoom, AdminPanel)
│   ├── services/        # API and WebSocket services
│   └── styles/          # CSS files
├── package.json
└── vite.config.js
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **npm** or **yarn**

### Backend Setup

1. **Navigate to the backend directory**:
   ```bash
   cd backend
   ```

2. **Build the project**:
   ```bash
   # On Windows
   gradlew.bat build
   
   # On Linux/Mac
   ./gradlew build
   ```

3. **Run the Spring Boot application**:
   ```bash
   # On Windows
   gradlew.bat bootRun
   
   # On Linux/Mac
   ./gradlew bootRun
   ```

   The backend server will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start the development server**:
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

   **Note**: The Vite config includes a proxy for API calls and WebSocket connections, so you can access the backend through the frontend dev server.

## 📡 API Endpoints

### Room Management

- **POST** `/api/rooms` - Create a new room
  ```json
  {
    "gameMode": "TRUTH_AND_DARE",
    "playerName": "Admin"
  }
  ```
  Response:
  ```json
  {
    "roomId": "uuid",
    "roomCode": "ABC123",
    "adminToken": "secure-token",
    "playerId": "uuid"
  }
  ```

- **POST** `/api/rooms/join` - Join a room
  ```json
  {
    "roomCode": "ABC123",
    "playerName": "Player Name"
  }
  ```

- **GET** `/api/rooms/{roomCode}/state` - Get room state

### Game Actions

- **POST** `/api/game/{roomId}/start` - Start the game (Admin only)
  Headers: `X-Admin-Token: {adminToken}`

- **GET** `/api/game/{roomId}/question?type=TRUTH` - Get next question

- **POST** `/api/game/{roomId}/next-turn` - Move to next turn

### Admin Operations

- **POST** `/api/admin/{roomId}/inject-question` - Inject custom question (Admin only)
  Headers: `X-Admin-Token: {adminToken}`
  ```json
  {
    "questionText": "Custom question text",
    "questionType": "TRUTH",
    "targetPlayerId": "optional-player-id"
  }
  ```

- **PUT** `/api/admin/{roomId}/game-mode` - Change game mode (Admin only)
  Headers: `X-Admin-Token: {adminToken}`
  ```json
  {
    "gameMode": "TRUTH_ONLY"
  }
  ```

- **POST** `/api/admin/{roomId}/force-next-turn` - Force next turn (Admin only)
  Headers: `X-Admin-Token: {adminToken}`

## 🔌 WebSocket Events

The application uses STOMP over WebSocket for real-time updates. Connect to:
- **WebSocket Endpoint**: `ws://localhost:8080/ws`

### Topics

- `/topic/room/{roomCode}` - Subscribe to room updates
- `/topic/room/{roomCode}/admin` - Admin-only updates

### Event Types

- `ROOM_CREATED` - Room was created
- `PLAYER_JOINED` - A player joined the room
- `PLAYER_LEFT` - A player left the room
- `GAME_STARTED` - Game started
- `QUESTION_SENT` - A new question was sent
- `ADMIN_OVERRIDE` - Admin injected a question
- `NEXT_TURN` - Turn advanced
- `ROOM_STATE` - Full room state update

### Event Format

```json
{
  "eventType": "QUESTION_SENT",
  "data": {
    "questionId": "uuid",
    "text": "Question text",
    "type": "TRUTH",
    "playerId": "uuid",
    "isAdminInjected": false
  },
  "timestamp": 1234567890
}
```

## 🔐 Security

- Admin operations require `X-Admin-Token` header
- Admin tokens are generated securely per room
- CORS is configured to allow frontend origin (configure in production)
- Input sanitization for admin-injected questions

## 🎮 How to Play

1. **Create a Room**:
   - Enter your name
   - Select game mode
   - Click "Create Room"
   - **Save your admin token!** (displayed in admin panel)

2. **Join a Room**:
   - Enter your name
   - Enter the room code
   - Click "Join Room"

3. **Start the Game** (Admin only):
   - Click "Start Game" button
   - Game begins and first player's turn starts

4. **During the Game**:
   - Questions are automatically generated
   - Players can see current question and whose turn it is
   - Click "Next Turn" to advance

5. **Admin Controls**:
   - Open Admin Panel
   - Inject custom questions (immediately overrides current)
   - Change game mode
   - Force next turn

## 🔧 Configuration

### Backend

Edit `src/main/resources/application.properties`:
```properties
server.port=8080
logging.level.com.truthdare.backend=DEBUG
```

### Frontend

Create `.env` file in `frontend/` directory:
```env
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=http://localhost:8080/ws
```

## 🐛 Troubleshooting

### Backend won't start
- Ensure Java 17+ is installed: `java -version`
- Check port 8080 is available
- Review logs in console

### Frontend can't connect to backend
- Ensure backend is running on port 8080
- Check CORS settings in `SecurityConfig.java`
- Verify proxy settings in `vite.config.js`

### WebSocket connection fails
- Ensure backend WebSocket endpoint is accessible
- Check browser console for WebSocket errors
- Verify SockJS is loading correctly

### Questions not loading
- Check external API availability (truthordarebot.xyz)
- Review backend logs for API errors
- Local fallback questions should work if API fails

## 📝 Development Notes

- **No Database**: All data is stored in memory (ConcurrentHashMap)
- **Room Cleanup**: Rooms are cleaned up when empty (optional)
- **Scalability**: Designed for horizontal scaling (can add Redis/Database later)
- **Mobile Ready**: Frontend is responsive and ready for mobile apps

## 🚧 Future Enhancements

- Add database persistence (PostgreSQL/MongoDB)
- Add user authentication and profiles
- Add game history and statistics
- Add private rooms with passwords
- Add mobile app (React Native)
- Add voice chat integration
- Add custom question banks per room

## 📄 License

This project is provided as-is for educational purposes.

## 🤝 Contributing

This is a demonstration project. Feel free to fork and modify as needed.

## 📞 Support

For issues or questions, please review the code comments and documentation inline.

---

**Built with ❤️ using Spring Boot and React**