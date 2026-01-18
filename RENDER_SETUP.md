# 🚀 Quick Render Deployment Guide

## Fastest Way to Deploy

### 1. Push to GitHub
```bash
git init
git add .
git commit -m "Ready for Render deployment"
git remote add origin https://github.com/YOUR_USERNAME/truth-dare.git
git push -u origin main
```

### 2. Create Web Service on Render

1. Go to https://dashboard.render.com
2. Click "New +" → "Web Service"
3. Connect your GitHub repo
4. Use these settings:

**Settings:**
- **Name**: `truth-dare-backend`
- **Environment**: `Java`
- **Build Command**: 
  ```bash
  cd frontend && npm install && npm run build && cd .. && ./gradlew clean build -x test
  ```
- **Start Command**: 
  ```bash
  java -jar build/libs/*-SNAPSHOT.jar
  ```
- **Health Check Path**: `/api/rooms/health`

**Environment Variables:**
- `SPRING_PROFILES_ACTIVE` = `prod`
- `CORS_ALLOWED_ORIGINS` = `*` (or your domain)

### 3. Deploy!

Click "Create Web Service" and wait 5-10 minutes.

### 4. Update CORS (After deployment)

Once you get your URL (e.g., `https://truth-dare-abc123.onrender.com`):
1. Go to Environment Variables
2. Update `CORS_ALLOWED_ORIGINS` to your URL
3. Restart the service

### 5. Test

Visit your Render URL - you should see the Truth & Dare app!

---

## Common Issues

**Build fails?**
- Check logs in Render dashboard
- Ensure `build.gradle` has frontend build task
- Verify Node.js is available (Render includes it)

**404 on frontend?**
- Check that `src/main/resources/static/index.html` exists after build
- Verify `StaticResourceConfig.java` is in place

**WebSocket not working?**
- Update CORS settings
- Use `wss://` (secure WebSocket) in production

---

**Need help?** Check `DEPLOYMENT.md` for detailed instructions.