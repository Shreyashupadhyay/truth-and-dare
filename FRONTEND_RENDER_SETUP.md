# 🎯 Frontend Render Deployment - Complete Setup

## ✅ Configuration Applied

I've updated the frontend to work with your deployed backend. Here's what changed:

### 1. API Service (`frontend/src/services/api.js`)
- ✅ Now reads `VITE_API_URL` from environment variables
- ✅ Falls back to `/api` for same-domain deployment
- ✅ Properly handles trailing slashes

### 2. WebSocket Service (`frontend/src/services/websocket.js`)
- ✅ Now reads `VITE_WS_URL` from environment variables
- ✅ Automatically uses `wss://` for HTTPS sites
- ✅ Detects localhost for development
- ✅ Falls back to same host as current page

### 3. Vite Configuration
- ✅ Already configured for production builds
- ✅ Global polyfill for `sockjs-client` fix

---

## 🚀 Render Deployment Steps

### Step 1: Create Static Site on Render

1. Go to: https://dashboard.render.com
2. Click "New +" → "Static Site"
3. Connect your GitHub repository

### Step 2: Configure Settings

**Basic Settings:**
- **Name:** `truth-dare-frontend` (or your choice)
- **Branch:** `main`
- **Root Directory:** `frontend` ⚠️ (if frontend is in a subdirectory)

**Build & Deploy:**
- **Build Command:** 
  ```bash
  npm install && npm run build
  ```
- **Publish Directory:** 
  ```
  dist
  ```

### Step 3: Add Environment Variables

Click "Environment Variables" and add:

| Variable Name | Value | Example |
|--------------|-------|---------|
| `VITE_API_URL` | Your backend API URL | `https://truth-and-dare-2.onrender.com/api` |
| `VITE_WS_URL` | Your backend WebSocket URL | `wss://truth-and-dare-2.onrender.com/ws` |

⚠️ **IMPORTANT:** 
- Replace `truth-and-dare-2.onrender.com` with your actual backend URL!
- Use `wss://` (not `ws://`) for WebSocket URL (required for HTTPS)

### Step 4: Create Static Site

Click "Create Static Site" and wait 3-5 minutes for deployment.

---

## 🔧 Step 4: Update Backend CORS

After getting your frontend URL (e.g., `https://truth-dare-frontend.onrender.com`):

1. Go to your **backend service** on Render
2. Click "Environment" tab
3. Update `CORS_ALLOWED_ORIGINS`:
   ```
   https://truth-dare-frontend.onrender.com,https://truth-and-dare-2.onrender.com
   ```
   (Comma-separated, include both frontend and backend URLs)

4. **Restart the backend service**

---

## ✅ Verification

### Test Your Frontend:

1. **Visit your frontend URL:**
   ```
   https://truth-dare-frontend.onrender.com
   ```

2. **Test creating a room:**
   - Should connect to backend successfully
   - No CORS errors

3. **Test WebSocket:**
   - Open browser console (F12)
   - Join a room
   - Should see "WebSocket connected" message
   - Real-time updates should work

---

## 📋 Quick Reference

### Render Static Site Settings:

```
Service Type: Static Site
Build Command: npm install && npm run build
Publish Directory: dist
Root Directory: frontend (if applicable)

Environment Variables:
- VITE_API_URL=https://your-backend.onrender.com/api
- VITE_WS_URL=wss://your-backend.onrender.com/ws
```

### Backend CORS Update:

```
CORS_ALLOWED_ORIGINS=https://your-frontend.onrender.com,https://your-backend.onrender.com
```

---

## 🔍 How It Works

### Environment Detection:

1. **If `VITE_API_URL` is set:**
   - Uses that URL for all API calls
   - Example: `https://truth-and-dare-2.onrender.com/api`

2. **If `VITE_WS_URL` is set:**
   - Uses that URL for WebSocket connection
   - Example: `wss://truth-and-dare-2.onrender.com/ws`

3. **If environment variables not set:**
   - API: Falls back to `/api` (relative path)
   - WebSocket: Falls back to same host as current page

---

## 🐛 Troubleshooting

### CORS Errors

**Error:** `Access-Control-Allow-Origin header is missing`

**Fix:**
1. Add frontend URL to backend's `CORS_ALLOWED_ORIGINS`
2. Restart backend service
3. Verify both URLs use HTTPS

### WebSocket Won't Connect

**Error:** `WebSocket connection failed`

**Fix:**
1. Use `wss://` (not `ws://`) in `VITE_WS_URL`
2. Verify backend WebSocket endpoint is accessible
3. Check browser console for specific error

### API Calls Return 404

**Error:** `404 Not Found` for API calls

**Fix:**
1. Verify `VITE_API_URL` is correct (include `/api` at the end)
2. Check backend is running
3. Test backend directly: `curl https://your-backend.onrender.com/api/health`

---

## 📝 Files Updated

- ✅ `frontend/src/services/api.js` - Environment variable support
- ✅ `frontend/src/services/websocket.js` - Environment variable support + wss:// detection
- ✅ `frontend/vite.config.js` - Already configured for production

---

## 🎉 Ready to Deploy!

Your frontend is now configured for Render deployment. Follow the steps above and you'll be live in minutes!

For detailed instructions, see `frontend/FRONTEND_DEPLOYMENT.md`