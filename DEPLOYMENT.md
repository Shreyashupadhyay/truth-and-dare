# 🚀 Deployment Guide - Truth & Dare on Render

This guide will help you deploy the Truth & Dare application to Render.

## 📋 Prerequisites

1. **Render Account**: Sign up at [render.com](https://render.com)
2. **GitHub Account**: Push your code to GitHub (required for Render)
3. **Java 17**: Render supports Java 17 out of the box

---

## 🎯 Deployment Options

### Option 1: Single Service (Recommended)
Deploy backend with frontend bundled (one service, easier to manage)

### Option 2: Separate Services
Deploy backend and frontend as separate services (more complex, better scaling)

This guide covers **Option 1** (recommended).

---

## 📦 Pre-Deployment Checklist

- [ ] Code is pushed to GitHub
- [ ] `build.gradle` includes frontend build task
- [ ] Environment variables configured
- [ ] CORS settings ready

---

## 🔧 Step-by-Step Deployment

### Step 1: Prepare Your Repository

1. **Push code to GitHub:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit - Truth & Dare app"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/truth-dare.git
   git push -u origin main
   ```

2. **Verify these files exist:**
   - ✅ `build.gradle` (with frontend build task)
   - ✅ `build.sh` (build script)
   - ✅ `render.yaml` (optional, for automated setup)

---

### Step 2: Create Render Web Service

1. **Go to Render Dashboard:**
   - Visit: https://dashboard.render.com
   - Click "New +" → "Web Service"

2. **Connect Repository:**
   - Connect your GitHub account (if not already connected)
   - Select your repository: `truth-dare`
   - Click "Connect"

3. **Configure Service:**

   **Basic Settings:**
   - **Name**: `truth-dare-backend` (or your preferred name)
   - **Region**: Choose closest to you (e.g., `Oregon (US West)`)
   - **Branch**: `main` (or your main branch)
   - **Root Directory**: Leave empty (project root)

   **Build & Deploy:**
   - **Environment**: `Java`
   - **Build Command**: 
     ```bash
     chmod +x build.sh && ./build.sh
     ```
     OR manually:
     ```bash
     cd frontend && npm install && npm run build && cd .. && ./gradlew clean build -x test
     ```
   - **Start Command**: 
     ```bash
     java -jar build/libs/*-SNAPSHOT.jar
     ```
   - **Health Check Path**: `/api/rooms/health`

4. **Environment Variables:**

   Click "Advanced" → "Environment Variables" and add:

   | Key | Value | Notes |
   |-----|-------|-------|
   | `SPRING_PROFILES_ACTIVE` | `prod` | Use production profile |
   | `PORT` | `8080` | Render sets this automatically |
   | `CORS_ALLOWED_ORIGINS` | `*` | Or your frontend domain |
   | `LOG_LEVEL` | `INFO` | Production logging level |
   | `FRONTEND_URL` | Your Render URL | After deployment |

5. **Click "Create Web Service"**

---

### Step 3: Wait for Deployment

- Render will automatically:
  1. Clone your repository
  2. Install Node.js (for frontend build)
  3. Build frontend (`npm install && npm run build`)
  4. Build backend (`./gradlew build`)
  5. Copy frontend build to `src/main/resources/static`
  6. Start the application

- **First deployment takes 5-10 minutes**
- Watch the logs for build progress
- Fix any errors shown in logs

---

### Step 4: Configure CORS (After Deployment)

Once deployed, you'll get a URL like: `https://truth-dare-backend.onrender.com`

1. **Update CORS environment variable:**
   - Go to Render Dashboard → Your Service → Environment
   - Update `CORS_ALLOWED_ORIGINS` to:
     ```
     https://truth-dare-backend.onrender.com
     ```
   - Or keep as `*` for development

2. **Restart the service:**
   - Click "Manual Deploy" → "Clear build cache & deploy"

---

### Step 5: Verify Deployment

1. **Health Check:**
   ```
   https://your-app.onrender.com/api/rooms/health
   ```
   Should return: `{"status":"UP","service":"truth-dare-backend"}`

2. **Frontend:**
   ```
   https://your-app.onrender.com
   ```
   Should show the Truth & Dare landing page

3. **API Test:**
   ```bash
   curl https://your-app.onrender.com/api/rooms/health
   ```

---

## 🔍 Troubleshooting

### Build Fails - Frontend Not Found

**Issue**: `npm: command not found` or frontend build fails

**Solution**: 
- Render automatically installs Node.js
- If it doesn't, add to `build.sh`:
  ```bash
  # Install Node.js if needed
  curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
  apt-get install -y nodejs
  ```

### Build Fails - Gradle Error

**Issue**: Gradle build fails

**Solution**:
- Check Java version (Render uses Java 17)
- Ensure `build.gradle` is correct
- Check logs for specific error

### 404 on Frontend Routes

**Issue**: React routes return 404

**Solution**:
- Verify `StaticResourceConfig.java` is in place
- Ensure frontend build is copied to `src/main/resources/static`
- Check that `index.html` is in static directory

### WebSocket Not Working

**Issue**: WebSocket connection fails

**Solution**:
- Update `CORS_ALLOWED_ORIGINS` to include your domain
- Ensure WebSocket endpoint `/ws` is accessible
- Check Render allows WebSocket connections (should work by default)

### Application Crashes

**Issue**: Service crashes after deployment

**Solution**:
- Check logs in Render Dashboard
- Verify all environment variables are set
- Ensure port is configured correctly (Render sets `PORT` automatically)

---

## 🔐 Environment Variables Reference

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | No | `dev` | Spring profile (use `prod`) |
| `PORT` | Auto | `8080` | Server port (Render sets this) |
| `CORS_ALLOWED_ORIGINS` | No | `*` | Comma-separated allowed origins |
| `LOG_LEVEL` | No | `INFO` | Logging level |
| `FRONTEND_URL` | No | `http://localhost:3000` | Frontend URL for CORS |

---

## 📝 Render-Specific Configuration

### Using render.yaml (Optional)

If you prefer automated setup, use the included `render.yaml`:

1. Push `render.yaml` to your repository
2. Go to Render Dashboard
3. Click "New" → "Blueprint"
4. Select your repository
5. Render will auto-configure based on `render.yaml`

**Important**: Update URLs in `render.yaml` after first deployment!

---

## 🔄 Continuous Deployment

Render automatically deploys when you push to your main branch:

1. Push changes to GitHub
2. Render detects changes
3. Builds and deploys automatically
4. Health check verifies deployment

---

## 🎉 Post-Deployment

After successful deployment:

1. ✅ Test the application at your Render URL
2. ✅ Share the URL with your friends!
3. ✅ Monitor logs for any issues
4. ✅ Set up custom domain (optional)

---

## 📞 Support

If you encounter issues:

1. Check Render logs: Dashboard → Your Service → Logs
2. Check application logs in the service
3. Verify all environment variables are set
4. Test locally first: `./build.sh && java -jar build/libs/*.jar`

---

## 🎯 Quick Deploy Commands

**Local Build Test:**
```bash
cd frontend && npm install && npm run build && cd ..
./gradlew clean build
java -jar build/libs/*-SNAPSHOT.jar
```

**Verify Build:**
```bash
ls -la src/main/resources/static/
# Should see index.html and other frontend files
```

---

**Happy Deploying! 🚀**