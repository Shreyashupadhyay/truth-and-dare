# Truth & Dare Frontend

React frontend for the Truth & Dare multiplayer game.

## 🚀 Quick Start

### Local Development

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Start development server:**
   ```bash
   npm run dev
   ```

3. **Open browser:**
   - Go to: http://localhost:3000
   - Backend should be running on http://localhost:8080

### Production Build

```bash
npm run build
```

Build output will be in `dist/` directory.

---

## 🌍 Environment Variables

Create a `.env.local` file for local development:

```env
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=http://localhost:8080/ws
```

For production (Render), set these in Render's environment variables:
- `VITE_API_URL` - Backend API URL (e.g., `https://your-backend.onrender.com/api`)
- `VITE_WS_URL` - Backend WebSocket URL (e.g., `wss://your-backend.onrender.com/ws`)

---

## 📦 Deployment

See `FRONTEND_DEPLOYMENT.md` for complete deployment guide.

### Quick Deploy on Render:

1. Create Static Site
2. Set build command: `npm install && npm run build`
3. Set publish directory: `dist`
4. Add environment variables (see above)
5. Deploy!

---

## 🔧 Configuration

The frontend automatically detects:
- **Local development**: Uses localhost URLs
- **Production**: Uses environment variables
- **Same-domain**: Falls back to relative paths

### API Service

Located in: `src/services/api.js`
- Reads `VITE_API_URL` environment variable
- Falls back to `/api` for same-domain deployment

### WebSocket Service

Located in: `src/services/websocket.js`
- Reads `VITE_WS_URL` environment variable
- Automatically uses `wss://` for HTTPS sites
- Falls back to same host as current page

---

## 📁 Project Structure

```
frontend/
├── src/
│   ├── components/      # React components
│   │   ├── Landing.jsx
│   │   ├── GameRoom.jsx
│   │   └── AdminPanel.jsx
│   ├── services/        # API and WebSocket services
│   │   ├── api.js
│   │   └── websocket.js
│   ├── styles/          # CSS files
│   └── App.jsx          # Main app component
├── .env.example         # Environment variable template
└── vite.config.js       # Vite configuration
```

---

## 🔗 Connecting to Backend

### Development:
- Backend: http://localhost:8080
- Frontend: http://localhost:3000
- Vite proxy handles API routing

### Production:
- Set `VITE_API_URL` to your backend URL
- Set `VITE_WS_URL` to your WebSocket URL (use `wss://` for HTTPS)
- Ensure backend CORS allows your frontend domain

---

## 🐛 Troubleshooting

### CORS Errors
- Check backend CORS settings
- Verify frontend URL is allowed

### WebSocket Won't Connect
- Use `wss://` for HTTPS sites
- Check backend WebSocket endpoint

### API Calls Fail
- Verify `VITE_API_URL` is correct
- Check backend is running

---

For detailed deployment instructions, see `FRONTEND_DEPLOYMENT.md`