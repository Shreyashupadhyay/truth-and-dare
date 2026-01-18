# Truth & Dare API Documentation

## Base URL
- Development: `http://localhost:8080/api`
- WebSocket: `ws://localhost:8080/ws`

## Authentication
Admin endpoints require `X-Admin-Token` header with the admin token received when creating a room.

---

## Room Management

### Create Room
**POST** `/api/rooms`

**Request Body:**
```json
{
  "gameMode": "TRUTH_AND_DARE",
  "playerName": "Admin"
}
```

**Game Modes:**
- `TRUTH_ONLY` - Only truth questions
- `DARE_ONLY` - Only dare questions  
- `TRUTH_AND_DARE` - Mixed questions

**Response:** `200 OK`
```json
{
  "roomId": "550e8400-e29b-41d4-a716-446655440000",
  "roomCode": "ABC123",
  "adminToken": "aBcDeFgHiJkLmNoPqRsTuVwXyZ123456",
  "playerId": "660e8400-e29b-41d4-a716-446655440001"
}
```

---

### Join Room
**POST** `/api/rooms/join`

**Request Body:**
```json
{
  "roomCode": "ABC123",
  "playerName": "John Doe"
}
```

**Response:** `200 OK`
```json
{
  "roomId": "550e8400-e29b-41d4-a716-446655440000",
  "roomCode": "ABC123",
  "playerId": "770e8400-e29b-41d4-a716-446655440002",
  "success": true,
  "message": "Successfully joined room"
}
```

**Error Response:** `404 NOT FOUND`
```json
{
  "success": false,
  "message": "Room not found"
}
```

---

### Get Room State
**GET** `/api/rooms/{roomCode}/state`

**Response:** `200 OK`
```json
{
  "roomId": "550e8400-e29b-41d4-a716-446655440000",
  "roomCode": "ABC123",
  "gameMode": "TRUTH_AND_DARE",
  "status": "ACTIVE",
  "currentTurnIndex": 0,
  "players": [
    {
      "playerId": "660e8400-e29b-41d4-a716-446655440001",
      "name": "Admin",
      "role": "ADMIN"
    },
    {
      "playerId": "770e8400-e29b-41d4-a716-446655440002",
      "name": "John Doe",
      "role": "PLAYER"
    }
  ],
  "currentPlayer": {
    "playerId": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Admin",
    "role": "ADMIN"
  },
  "currentQuestion": {
    "questionId": "880e8400-e29b-41d4-a716-446655440003",
    "text": "What's your biggest secret?",
    "type": "TRUTH",
    "playerId": "660e8400-e29b-41d4-a716-446655440001",
    "isAdminInjected": false
  }
}
```

---

## Game Actions

### Start Game
**POST** `/api/game/{roomId}/start`

**Headers:**
- `X-Admin-Token: {adminToken}`

**Response:** `200 OK` (empty body)

**Error Responses:**
- `400 BAD REQUEST` - Game already started or insufficient players
- `403 FORBIDDEN` - Invalid admin token

---

### Get Next Question
**GET** `/api/game/{roomId}/question?type=TRUTH`

**Query Parameters:**
- `type` (optional): `TRUTH` or `DARE` - Preferred question type

**Response:** `200 OK`
```json
{
  "questionId": "880e8400-e29b-41d4-a716-446655440003",
  "text": "What's the most embarrassing thing that's ever happened to you?",
  "type": "TRUTH",
  "playerId": "660e8400-e29b-41d4-a716-446655440001",
  "isAdminInjected": false
}
```

---

### Next Turn
**POST** `/api/game/{roomId}/next-turn`

**Response:** `200 OK` (empty body)

---

## Admin Operations

### Inject Custom Question
**POST** `/api/admin/{roomId}/inject-question`

**Headers:**
- `X-Admin-Token: {adminToken}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "questionText": "Do 20 push-ups right now!",
  "questionType": "DARE",
  "targetPlayerId": "770e8400-e29b-41d4-a716-446655440002"
}
```

**Note:** `targetPlayerId` is optional. If omitted, applies to current player.

**Response:** `200 OK`
```json
{
  "questionId": "990e8400-e29b-41d4-a716-446655440004",
  "text": "Do 20 push-ups right now!",
  "type": "DARE",
  "playerId": "770e8400-e29b-41d4-a716-446655440002",
  "isAdminInjected": true
}
```

**Error Responses:**
- `403 FORBIDDEN` - Invalid admin token
- `404 NOT FOUND` - Room not found

---

### Change Game Mode
**PUT** `/api/admin/{roomId}/game-mode`

**Headers:**
- `X-Admin-Token: {adminToken}`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "gameMode": "TRUTH_ONLY"
}
```

**Response:** `200 OK` (empty body)

---

### Force Next Turn
**POST** `/api/admin/{roomId}/force-next-turn`

**Headers:**
- `X-Admin-Token: {adminToken}`

**Response:** `200 OK` (empty body)

---

## WebSocket Events

### Connection
Connect to: `ws://localhost:8080/ws`

Subscribe to:
- `/topic/room/{roomCode}` - Room updates
- `/topic/room/{roomCode}/admin` - Admin-only updates

### Event Types

#### ROOM_CREATED
```json
{
  "eventType": "ROOM_CREATED",
  "data": {
    "roomId": "550e8400-e29b-41d4-a716-446655440000",
    "roomCode": "ABC123",
    "adminToken": "aBcDeFgHiJkLmNoPqRsTuVwXyZ123456",
    "playerId": "660e8400-e29b-41d4-a716-446655440001"
  },
  "timestamp": 1703123456789
}
```

#### PLAYER_JOINED
```json
{
  "eventType": "PLAYER_JOINED",
  "data": {
    "playerId": "770e8400-e29b-41d4-a716-446655440002",
    "name": "John Doe",
    "role": "PLAYER"
  },
  "timestamp": 1703123456790
}
```

#### PLAYER_LEFT
```json
{
  "eventType": "PLAYER_LEFT",
  "data": {
    "playerId": "770e8400-e29b-41d4-a716-446655440002"
  },
  "timestamp": 1703123456791
}
```

#### GAME_STARTED
```json
{
  "eventType": "GAME_STARTED",
  "data": null,
  "timestamp": 1703123456792
}
```

#### QUESTION_SENT
```json
{
  "eventType": "QUESTION_SENT",
  "data": {
    "questionId": "880e8400-e29b-41d4-a716-446655440003",
    "text": "What's your biggest fear?",
    "type": "TRUTH",
    "playerId": "660e8400-e29b-41d4-a716-446655440001",
    "isAdminInjected": false
  },
  "timestamp": 1703123456793
}
```

#### ADMIN_OVERRIDE
```json
{
  "eventType": "ADMIN_OVERRIDE",
  "data": {
    "questionId": "990e8400-e29b-41d4-a716-446655440004",
    "text": "Do 20 push-ups right now!",
    "type": "DARE",
    "playerId": "770e8400-e29b-41d4-a716-446655440002",
    "isAdminInjected": true
  },
  "timestamp": 1703123456794
}
```

#### NEXT_TURN
```json
{
  "eventType": "NEXT_TURN",
  "data": null,
  "timestamp": 1703123456795
}
```

#### ROOM_STATE
```json
{
  "eventType": "ROOM_STATE",
  "data": {
    "roomId": "550e8400-e29b-41d4-a716-446655440000",
    "roomCode": "ABC123",
    "gameMode": "TRUTH_AND_DARE",
    "status": "ACTIVE",
    "currentTurnIndex": 1,
    "players": [...],
    "currentPlayer": {...},
    "currentQuestion": {...}
  },
  "timestamp": 1703123456796
}
```

#### GAME_MODE_CHANGED
```json
{
  "eventType": "GAME_MODE_CHANGED",
  "data": {
    "gameMode": "TRUTH_ONLY"
  },
  "timestamp": 1703123456797
}
```

---

## Error Responses

All error responses follow this format:

```json
{
  "message": "Error description",
  "timestamp": 1703123456789
}
```

### HTTP Status Codes
- `200 OK` - Success
- `400 BAD REQUEST` - Invalid request data
- `403 FORBIDDEN` - Invalid admin token
- `404 NOT FOUND` - Resource not found
- `500 INTERNAL SERVER ERROR` - Server error

---

## Question Priority Strategy

When generating a question, the system follows this priority:

1. **Admin-injected question** (highest priority)
   - From admin's custom question injection
   - Immediately overrides current question

2. **External API question**
   - Fetched from truthordarebot.xyz API
   - Falls back to local if API fails

3. **Local fallback question**
   - Pre-defined questions in the system
   - Used when external API is unavailable

---

## Example Workflow

### 1. Create Room
```bash
POST /api/rooms
{
  "gameMode": "TRUTH_AND_DARE",
  "playerName": "Alice"
}

Response:
{
  "roomId": "...",
  "roomCode": "XYZ789",
  "adminToken": "...",
  "playerId": "..."
}
```

### 2. Player Joins
```bash
POST /api/rooms/join
{
  "roomCode": "XYZ789",
  "playerName": "Bob"
}
```

### 3. Start Game (Admin)
```bash
POST /api/game/{roomId}/start
Headers: X-Admin-Token: {adminToken}
```

### 4. Get Question
```bash
GET /api/game/{roomId}/question
```

### 5. Admin Injects Question
```bash
POST /api/admin/{roomId}/inject-question
Headers: X-Admin-Token: {adminToken}
{
  "questionText": "Custom question!",
  "questionType": "TRUTH"
}
```

### 6. Next Turn
```bash
POST /api/game/{roomId}/next-turn
```

---

## Notes

- Room codes are case-insensitive but stored as uppercase
- Admin tokens are securely generated random strings
- All timestamps are in milliseconds since epoch
- WebSocket events are sent to all connected clients in the room
- Admin operations require valid admin token in header