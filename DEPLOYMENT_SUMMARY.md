# ✅ Deployment Ready - Summary

Your Truth & Dare application is now ready for deployment on Render!

## 📋 What Was Done

### ✅ Backend Configuration
- Updated `application.properties` to use environment variables
- Created production profile configuration
- Updated CORS settings to read from environment variables
- Updated WebSocket configuration for production
- Created `StaticResourceConfig` to serve React build
- Added health check endpoint (`/api/health`)
- Updated `build.gradle` to automatically build frontend and include it in JAR

### ✅ Frontend Configuration
- Updated API service to use relative paths in production
- Updated WebSocket service to detect localhost vs production
- Added production build configuration in `vite.config.js`
- Fixed `global` polyfill issue

### ✅ Deployment Files Created
- `render.yaml` - Render configuration
- `build.sh` - Linux/Mac build script
- `build.bat` - Windows build script
- `.gitignore` - Git ignore rules
- `DEPLOYMENT.md` - Complete deployment guide
- `RENDER_SETUP.md` - Quick deployment guide

---

## 🚀 Quick Deploy Steps

### 1. Push to GitHub
```bash
git init
git add .
git commit -m "Ready for Render deployment"
git remote add origin https://github.com/YOUR_USERNAME/truth-dare.git
git push -u origin main
```

### 2. Create Render Service

1. Go to: https://dashboard.render.com
2. Click "New +" → "Web Service"
3. Connect your GitHub repository

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
- **Health Check Path**: `/api/health`

**Environment Variables:**
- `SPRING_PROFILES_ACTIVE` = `prod`
- `CORS_ALLOWED_ORIGINS` = `*` (update to your domain after deployment)

### 3. Deploy!

Click "Create Web Service" and wait 5-10 minutes.

### 4. After Deployment

1. **Get your Render URL** (e.g., `https://truth-dare-abc123.onrender.com`)

2. **Update CORS** (if needed):
   - Go to Environment Variables
   - Update `CORS_ALLOWED_ORIGINS` to your Render URL
   - Restart the service

3. **Test:**
   - Visit your Render URL - should show Truth & Dare app!
   - Health check: `https://your-app.onrender.com/api/health`

---

## 📁 Key Files

- **`build.gradle`** - Builds both frontend and backend
- **`render.yaml`** - Render service configuration
- **`DEPLOYMENT.md`** - Complete deployment guide
- **`RENDER_SETUP.md`** - Quick setup guide

---

## 🔧 How It Works

1. **Build Process:**
   - `build.gradle` builds React frontend (`npm run build`)
   - Copies frontend `dist/` to `src/main/resources/static/`
   - Builds Spring Boot JAR with frontend included

2. **Runtime:**
   - Spring Boot serves static files from `/static/`
   - API endpoints at `/api/**`
   - WebSocket at `/ws`
   - All routes fall back to `index.html` (SPA routing)

3. **Environment Variables:**
   - `PORT` - Set automatically by Render
   - `CORS_ALLOWED_ORIGINS` - Configure your allowed domains
   - `SPRING_PROFILES_ACTIVE` - Use `prod` for production

---

## 🎯 Testing Locally Before Deploy

```bash
# Build everything
cd frontend && npm install && npm run build && cd ..
./gradlew clean build

# Run the JAR
java -jar build/libs/*-SNAPSHOT.jar

# Visit http://localhost:8080
```

---

## 📝 Notes

- **Single Service**: Frontend is bundled with backend (one JAR)
- **Auto-Build**: Gradle automatically builds frontend during backend build
- **Health Check**: `/api/health` endpoint for Render monitoring
- **CORS**: Configurable via environment variables
- **WebSocket**: Works with Render (uses `wss://` in production)

---

## 🆘 Troubleshooting

**Build fails?**
- Check Render logs
- Ensure Node.js is available (Render includes it)
- Verify `build.gradle` frontend task works

**404 on frontend?**
- Verify `src/main/resources/static/index.html` exists after build
- Check `StaticResourceConfig.java` is in place

**Need help?**
- Check `DEPLOYMENT.md` for detailed guide
- Check Render logs for specific errors

---

**You're all set! 🎉**

Ready to deploy? Follow the steps in `RENDER_SETUP.md` or `DEPLOYMENT.md`!