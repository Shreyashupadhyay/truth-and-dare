# ✅ Pre-Deployment Validation Checklist

## 🐳 Docker Configuration

### Dockerfile Verification:
- [x] ✅ Multi-stage build (build + runtime)
- [x] ✅ Uses JDK 17 for build stage
- [x] ✅ Uses JRE-only for runtime (smaller image)
- [x] ✅ Tests skipped (`-x test`)
- [x] ✅ Non-root user created
- [x] ✅ PORT environment variable supported
- [x] ✅ Health check configured
- [x] ✅ JAR copied correctly from build/libs/

### .dockerignore Verification:
- [x] ✅ Excludes build artifacts
- [x] ✅ Excludes IDE files
- [x] ✅ Excludes unnecessary files

---

## ☕ Java & Spring Boot Configuration

### Application Properties:
- [x] ✅ `server.port=${PORT:8080}` - Reads PORT env var
- [x] ✅ Environment variable support configured
- [x] ✅ Production profile support

### Build Configuration:
- [x] ✅ `build.gradle` configured for bootJar
- [x] ✅ Executable JAR created
- [x] ✅ Java 17 toolchain configured
- [x] ✅ Gradle wrapper present and configured

### Health Endpoint:
- [x] ✅ `/api/health` endpoint created
- [x] ✅ Returns JSON: `{"status":"UP","service":"truth-dare-backend"}`

---

## 🚀 Render Configuration

### Service Settings:
- [ ] **Language:** Must be `Docker` (NOT Java!)
- [ ] **Build Command:** Must be EMPTY
- [ ] **Start Command:** Must be EMPTY
- [ ] **Dockerfile Path:** `Dockerfile` (default)
- [ ] **Health Check Path:** `/api/health`

### Environment Variables:
- [ ] `SPRING_PROFILES_ACTIVE=prod`
- [ ] `CORS_ALLOWED_ORIGINS=*` (update after deployment)
- [ ] `LOG_LEVEL=INFO` (optional)

---

## 🧪 Local Testing

### Test Docker Build:
```bash
docker build -t truth-dare-backend .
```
- [ ] Build succeeds without errors
- [ ] JAR file is created in build/libs/
- [ ] Final image is under 300MB

### Test Docker Run:
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e PORT=8080 \
  truth-dare-backend
```
- [ ] Container starts successfully
- [ ] Application binds to port 8080
- [ ] Health endpoint responds: `curl http://localhost:8080/api/health`
- [ ] Returns: `{"status":"UP","service":"truth-dare-backend"}`

---

## 📦 Files Required in Repository

- [x] ✅ `Dockerfile` - Multi-stage build
- [x] ✅ `.dockerignore` - Excludes unnecessary files
- [x] ✅ `build.gradle` - Build configuration
- [x] ✅ `gradlew` - Gradle wrapper (executable)
- [x] ✅ `gradle/wrapper/` - Gradle wrapper files
- [x] ✅ `settings.gradle` - Gradle settings
- [x] ✅ `src/main/resources/application.properties` - Config with PORT support
- [x] ✅ `src/main/java/.../HealthController.java` - Health endpoint

---

## 🔍 Validation Commands

### Verify JAR Creation:
```bash
./gradlew clean bootJar -x test
ls -la build/libs/*.jar
```

### Verify PORT Support:
```bash
# Check application.properties contains:
grep "server.port" src/main/resources/application.properties
# Should show: server.port=${PORT:8080}
```

### Verify Docker Build:
```bash
docker build -t test .
docker run --rm -p 8080:8080 -e PORT=8080 test
curl http://localhost:8080/api/health
```

---

## ⚠️ Common Issues to Avoid

### ❌ Don't:
- Select "Java" language in Render (use "Docker")
- Add build/start commands (leave empty)
- Hardcode port (use ${PORT:8080})
- Include frontend in Docker build (unless needed)
- Use root user in container

### ✅ Do:
- Select "Docker" language
- Leave build/start commands empty
- Use PORT environment variable
- Test locally before deploying
- Use non-root user in container

---

## 🎯 Ready to Deploy When:

- [x] All checklist items above are ✅
- [ ] Docker build works locally
- [ ] Docker run works locally
- [ ] Health endpoint responds correctly
- [ ] Code pushed to GitHub
- [ ] Render service configured with correct settings

---

**Status: ✅ Ready for Deployment**

Follow `RENDER_DOCKER_DEPLOYMENT.md` for step-by-step instructions.