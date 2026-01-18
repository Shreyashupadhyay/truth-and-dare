# ✅ Frontend Configuration Summary

## Changes Made for Render Deployment

### 1. API Service Configuration ✅

**File:** `frontend/src/services/api.js`

**What Changed:**
- Added `getApiBaseUrl()` function to properly read `VITE_API_URL`
- Handles trailing slashes
- Falls back to `/api` if environment variable not set

**How It Works:**
```javascript
// Reads from: VITE_API_URL environment variable
// Example: https://truth-and-dare-2.onrender.com/api
// Falls back to: /api (relative path)
```

### 2. WebSocket Service Configuration ✅

**File:** `frontend/src/services/websocket.js`

**What Changed:**
- Added `getWebSocketUrl()` function
- Automatically detects HTTPS and uses `wss://`
- Falls back to same host as current page
- Reads `VITE_WS_URL` from environment variables

**How It Works:**
```javascript
// Reads from: VITE_WS_URL environment variable
// Example: wss://truth-and-dare-2.onrender.com/ws
// Auto-detects: wss:// for HTTPS, ws:// for HTTP
```

---

## 🚀 Render Deployment Configuration

### Required Environment Variables:

1. **`VITE_API_URL`**
   - Value: `https://truth-and-dare-2.onrender.com/api`
   - Purpose: Backend API base URL
   - ⚠️ Replace with your actual backend URL!

2. **`VITE_WS_URL`**
   - Value: `wss://truth-and-dare-2.onrender.com/ws`
   - Purpose: Backend WebSocket URL
   - ⚠️ Must use `wss://` (secure WebSocket for HTTPS)
   - ⚠️ Replace with your actual backend URL!

---

## 📋 Render Static Site Settings

```
Service Type: Static Site
Root Directory: frontend (if frontend is in subdirectory)
Build Command: npm install && npm run build
Publish Directory: dist

Environment Variables:
VITE_API_URL=https://your-backend.onrender.com/api
VITE_WS_URL=wss://your-backend.onrender.com/ws
```

---

## 🔧 After Deployment

1. **Get your frontend URL** (e.g., `https://truth-dare-frontend.onrender.com`)

2. **Update backend CORS:**
   - Go to backend service → Environment
   - Update `CORS_ALLOWED_ORIGINS` to include frontend URL
   - Format: `https://your-frontend.onrender.com,https://your-backend.onrender.com`
   - Restart backend

3. **Test:**
   - Visit frontend URL
   - Create a room
   - Verify WebSocket connection (check browser console)

---

## ✅ Status

- ✅ Frontend configured for production
- ✅ Environment variables supported
- ✅ WebSocket automatically uses secure protocol
- ✅ API service configured
- ✅ Ready for Render deployment

**Next:** Deploy on Render using the settings above!