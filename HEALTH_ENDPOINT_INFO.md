# ✅ Health Endpoint Information

## Correct Health Endpoint Path

**Correct URL:**
```
https://truth-and-dare-2.onrender.com/api/health
```

**Wrong URL (what you tried):**
```
https://truth-and-dare-2.onrender.com/api/rooms/health  ❌
```

## Expected Response

```json
{
  "status": "UP",
  "service": "truth-dare-backend"
}
```

## Quick Test

```bash
curl https://truth-and-dare-2.onrender.com/api/health
```

Should return:
```json
{"status":"UP","service":"truth-dare-backend"}
```

---

## All API Endpoints

### Health Check
- `GET /api/health` ✅

### Room Management
- `POST /api/rooms` - Create room
- `POST /api/rooms/join` - Join room
- `GET /api/rooms/{roomCode}/state` - Get room state

### Game Actions
- `POST /api/game/{roomId}/start` - Start game
- `GET /api/game/{roomId}/question` - Get question
- `POST /api/game/{roomId}/next-turn` - Next turn

### Admin Operations
- `POST /api/admin/{roomId}/inject-question` - Inject question
- `PUT /api/admin/{roomId}/game-mode` - Change game mode
- `POST /api/admin/{roomId}/force-next-turn` - Force next turn

---

**Note:** The `/api/health` endpoint is at the root `/api` path, not under `/api/rooms`.