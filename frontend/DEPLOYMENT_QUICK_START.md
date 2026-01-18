# ⚡ Quick Frontend Deployment Guide

## 🚀 Deploy on Render (5 minutes)

### Step 1: Create Static Site

1. Go to: https://dashboard.render.com
2. Click "New +" → "Static Site"
3. Connect your GitHub repo

### Step 2: Configure

**Settings:**
- **Name:** `truth-dare-frontend`
- **Root Directory:** `frontend` (if frontend is in subdirectory)
- **Build Command:** `npm install && npm run build`
- **Publish Directory:** `dist`

**Environment Variables:**
```
VITE_API_URL=https://truth-and-dare-2.onrender.com/api
VITE_WS_URL=wss://truth-and-dare-2.onrender.com/ws
```

⚠️ **Replace with your actual backend URL!**

### Step 3: Deploy!

Click "Create Static Site" and wait 3-5 minutes.

### Step 4: Update Backend CORS

1. Go to your backend service on Render
2. Environment Variables
3. Add frontend URL to `CORS_ALLOWED_ORIGINS`:
   ```
   https://your-frontend-url.onrender.com
   ```
4. Restart backend

### Step 5: Test

Visit your frontend URL and test creating a room!

---

## ✅ Quick Checklist

- [ ] Backend deployed and working
- [ ] Frontend code pushed to GitHub
- [ ] Environment variables set correctly
- [ ] CORS updated on backend
- [ ] WebSocket uses `wss://` (not `ws://`)

---

**Done! 🎉**