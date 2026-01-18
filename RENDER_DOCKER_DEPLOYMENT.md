# 🐳 Render Docker Deployment Guide

Complete step-by-step guide for deploying Spring Boot backend using Docker on Render.

---

## 📋 Prerequisites

- ✅ Spring Boot application with Gradle
- ✅ Dockerfile created (already done)
- ✅ Application configured to read PORT environment variable
- ✅ GitHub repository with code

---

## 🔧 Step 1: Verify Dockerfile

Your `Dockerfile` is production-ready with:
- ✅ Multi-stage build (build + runtime stages)
- ✅ JRE-only final image (smaller size)
- ✅ Tests skipped during build
- ✅ Non-root user for security
- ✅ Health check configured
- ✅ PORT environment variable support

---

## 📝 Step 2: Render Web Service Configuration

### Service Type
**Select:** `Web Service`

### Language
**Select:** `Docker`

⚠️ **Important:** Do NOT select "Java" - select "Docker"

### Build Command
**Leave EMPTY** (Docker handles building)

### Start Command  
**Leave EMPTY** (Dockerfile CMD/ENTRYPOINT handles starting)

### Root Directory (Optional)
**Leave EMPTY** (unless your Dockerfile is in a subdirectory)

### Branch
```
main
```
*(Or your default branch)*

### Region
Choose your preferred region (e.g., `Oregon (US West)`)

### Dockerfile Path (if not in root)
**Default:** `Dockerfile`  
**Only change if:** Your Dockerfile is in a different location

---

## 🌍 Step 3: Environment Variables

Click "Add Environment Variable" and add:

### Required:

| Variable Name | Value | Description |
|--------------|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Production profile |
| `PORT` | `8080` | **Note:** Render sets this automatically, but you can add for clarity |

### Recommended:

| Variable Name | Value | Description |
|--------------|-------|-------------|
| `CORS_ALLOWED_ORIGINS` | `*` | CORS origins (update after deployment) |
| `LOG_LEVEL` | `INFO` | Logging level |

**Important:** Render automatically sets `PORT` environment variable, but your app will use it via `${PORT:8080}` in application.properties.

---

## 🏥 Step 4: Health Check (Advanced)

Expand "Advanced" section:

**Health Check Path:**
```
/api/health
```

**Or leave empty** - Docker healthcheck will handle it.

---

## 🚀 Step 5: Deploy

1. Click **"Create Web Service"**
2. Render will:
   - Build the Docker image
   - Run the container
   - Expose it on the internet
3. First deployment takes **5-10 minutes**
4. Watch the logs for any errors

---

## ✅ Step 6: Verify Deployment

After deployment, you'll get a URL like:
```
https://truth-dare-backend-abc123.onrender.com
```

### Test Health Endpoint:
```bash
curl https://your-app.onrender.com/api/health
```

**Expected Response:**
```json
{"status":"UP","service":"truth-dare-backend"}
```

### Test API:
```bash
curl https://your-app.onrender.com/api/rooms
```

---

## 🔍 How It Works

### Build Process:
1. Render clones your repository
2. Builds Docker image using `Dockerfile`
3. Stage 1: Runs `./gradlew build -x test` (builds JAR)
4. Stage 2: Copies JAR to lightweight JRE image
5. Creates and runs container

### Runtime:
1. Container starts with JRE only (no build tools)
2. Application reads `PORT` from environment variable
3. Starts Spring Boot application
4. Health check verifies `/api/health` endpoint

---

## 📊 Render Settings Summary

```
Service Type: Web Service
Language: Docker
Build Command: (empty)
Start Command: (empty)
Dockerfile Path: Dockerfile
Health Check Path: /api/health

Environment Variables:
- SPRING_PROFILES_ACTIVE=prod
- CORS_ALLOWED_ORIGINS=*
- LOG_LEVEL=INFO
```

---

## 🧪 Test Locally Before Deploy

### Build Docker Image:
```bash
docker build -t truth-dare-backend .
```

### Run Container:
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e PORT=8080 \
  -e CORS_ALLOWED_ORIGINS=* \
  truth-dare-backend
```

### Test:
```bash
curl http://localhost:8080/api/health
```

---

## 🔧 Troubleshooting

### Build Fails - Gradle Not Found

**Issue:** `./gradlew: not found`

**Solution:** Ensure `gradlew` and `gradle/wrapper/` are in your repository and committed to git.

### Build Fails - JAR Not Created

**Issue:** Build completes but no JAR in `build/libs/`

**Solution:** 
- Check `build.gradle` has `bootJar` task enabled
- Verify no errors in build logs
- Ensure `settings.gradle` exists

### Application Won't Start

**Issue:** Container exits immediately

**Solution:**
- Check logs: `Render Dashboard → Your Service → Logs`
- Verify `application.properties` has `server.port=${PORT:8080}`
- Ensure JAR is copied correctly in Dockerfile

### Port Issues

**Issue:** "Port already in use" or app won't bind

**Solution:**
- Render sets `PORT` automatically - don't hardcode it
- Verify `server.port=${PORT:8080}` in `application.properties`
- Check Dockerfile ENTRYPOINT uses `$PORT`

### Health Check Fails

**Issue:** Health check returns error

**Solution:**
- Verify `/api/health` endpoint exists (HealthController)
- Check application is actually running
- Ensure health check waits long enough (start_period: 40s)

---

## 🔐 Security Notes

✅ **Implemented:**
- Non-root user in container
- Multi-stage build (no build tools in final image)
- JRE only (no JDK in production)
- Environment variables for secrets

⚠️ **Consider:**
- Update `CORS_ALLOWED_ORIGINS` to your specific domain after deployment
- Use Render's secret management for sensitive values
- Enable HTTPS (Render provides this automatically)

---

## 📦 Docker Image Size

- **Build stage:** ~800MB (includes JDK, Gradle)
- **Runtime stage:** ~200MB (JRE only)
- **Final image:** ~250MB (with application JAR)

This is optimized for production deployment.

---

## 🔄 Continuous Deployment

Render automatically:
1. Detects pushes to your main branch
2. Rebuilds Docker image
3. Deploys new version
4. Runs health checks
5. Switches traffic to new version (zero downtime)

---

## 📞 Next Steps After Deployment

1. **Update CORS:**
   - Get your Render URL
   - Update `CORS_ALLOWED_ORIGINS` environment variable
   - Restart service

2. **Connect Frontend:**
   - Update frontend API URL to your Render URL
   - Test WebSocket connection (use `wss://` for secure)

3. **Monitor:**
   - Check logs regularly
   - Monitor health check status
   - Set up alerts if needed

---

## ✅ Checklist

Before deploying:
- [ ] Dockerfile is in repository root
- [ ] `.dockerignore` is present
- [ ] `application.properties` has `server.port=${PORT:8080}`
- [ ] Health endpoint `/api/health` exists
- [ ] `build.gradle` creates executable JAR
- [ ] Tested Docker build locally
- [ ] Code pushed to GitHub

---

**Ready to deploy! 🚀**

Follow the settings above exactly, and your first deployment should succeed.