# 🚀 Frontend Deployment Guide for Render

Complete guide to deploy the React frontend on Render as a Static Site.

---

## 📋 Prerequisites

- ✅ Backend deployed on Render (e.g., `https://truth-and-dare-2.onrender.com`)
- ✅ GitHub repository with frontend code
- ✅ Render account

---

## 🎯 Deployment Steps

### Step 1: Push Frontend Code to GitHub

Make sure your frontend code is in a separate directory or branch:
```bash
cd frontend
git add .
git commit -m "Frontend ready for Render deployment"
git push
```

---

### Step 2: Create Static Site on Render

1. **Go to Render Dashboard:**
   - Visit: https://dashboard.render.com
   - Click "New +" → "Static Site"

2. **Connect Repository:**
   - Connect your GitHub account (if not already)
   - Select your repository
   - Choose the branch (usually `main`)

3. **Configure Build Settings:**

   **Basic Settings:**
   - **Name:** `truth-dare-frontend` (or your preferred name)
   - **Branch:** `main`
   - **Root Directory:** `frontend` (if frontend is in a subdirectory)
   
   **Build Settings:**
   - **Build Command:** 
     ```bash
     npm install && npm run build
     ```
   
   - **Publish Directory:** 
     ```
     dist
     ```

4. **Add Environment Variables:**

   Click "Advanced" → "Environment Variables" and add:

   | Variable Name | Value | Description |
   |--------------|-------|-------------|
   | `VITE_API_URL` | `https://truth-and-dare-2.onrender.com/api` | Your backend API URL |
   | `VITE_WS_URL` | `wss://truth-and-dare-2.onrender.com/ws` | Your backend WebSocket URL (use `wss://` for HTTPS) |

   ⚠️ **Important:** Replace `truth-and-dare-2.onrender.com` with your actual backend URL!

5. **Click "Create Static Site"**

---

### Step 3: Wait for Deployment

- First deployment takes 3-5 minutes
- Watch the build logs for any errors
- Once deployed, you'll get a URL like: `https://truth-dare-frontend.onrender.com`

---

### Step 4: Update CORS on Backend

After getting your frontend URL, update backend CORS settings:

1. Go to Backend service on Render
2. Environment Variables
3. Update `CORS_ALLOWED_ORIGINS` to include your frontend URL:
   ```
   https://truth-dare-frontend.onrender.com,https://truth-and-dare-2.onrender.com
   ```
   (comma-separated for multiple origins)

4. Restart the backend service

---

## ✅ Verification

### Test Frontend:

1. **Visit your frontend URL:**
   ```
   https://truth-dare-frontend.onrender.com
   ```

2. **Test creating a room:**
   - Should connect to backend API
   - Should work without CORS errors

3. **Test WebSocket:**
   - Join a room
   - Check browser console for WebSocket connection
   - Should see real-time updates

---

## 🔧 Configuration Details

### Environment Variables

The frontend uses these environment variables (set in Render):

- **`VITE_API_URL`**: Backend API base URL
  - Example: `https://truth-and-dare-2.onrender.com/api`
  
- **`VITE_WS_URL`**: Backend WebSocket URL
  - Must use `wss://` for HTTPS sites
  - Example: `wss://truth-and-dare-2.onrender.com/ws`

### How It Works

1. **API Calls:**
   - Frontend reads `VITE_API_URL` from environment
   - Makes requests to: `${VITE_API_URL}/rooms`, etc.

2. **WebSocket:**
   - Frontend reads `VITE_WS_URL` from environment
   - Connects to WebSocket endpoint
   - Automatically uses `wss://` if page is served over HTTPS

---

## 🐛 Troubleshooting

### CORS Errors

**Problem:** `Access-Control-Allow-Origin` errors

**Solution:**
- Add frontend URL to backend's `CORS_ALLOWED_ORIGINS`
- Restart backend service
- Verify both URLs use HTTPS

### WebSocket Connection Fails

**Problem:** WebSocket won't connect

**Solution:**
- Verify `VITE_WS_URL` uses `wss://` (not `ws://`)
- Check backend WebSocket endpoint is accessible
- Ensure backend CORS allows WebSocket connections

### API Calls Fail

**Problem:** 404 or network errors for API calls

**Solution:**
- Verify `VITE_API_URL` is correct
- Check backend is running
- Ensure backend URL is accessible

### Blank Screen

**Problem:** Frontend shows blank page

**Solution:**
- Check browser console for errors
- Verify build completed successfully
- Check that `dist/index.html` exists

---

## 📝 Render Settings Summary

```
Service Type: Static Site
Build Command: npm install && npm run build
Publish Directory: dist
Root Directory: frontend (if applicable)

Environment Variables:
- VITE_API_URL=https://your-backend.onrender.com/api
- VITE_WS_URL=wss://your-backend.onrender.com/ws
```

---

## 🔄 Continuous Deployment

Render automatically:
- Detects pushes to your branch
- Rebuilds the static site
- Deploys new version
- Updates the URL (if same service)

---

## 🌐 Custom Domain (Optional)

You can add a custom domain:
1. Go to your Static Site settings
2. Click "Custom Domains"
3. Add your domain
4. Update DNS as instructed

---

**Your frontend is now ready for deployment! 🎉**

Follow the steps above to deploy on Render.