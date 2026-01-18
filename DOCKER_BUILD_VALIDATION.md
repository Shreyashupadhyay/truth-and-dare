# 🔍 Docker Build Validation

## Quick Validation Steps

### 1. Verify Dockerfile Syntax
```bash
docker build --dry-run .
# Or use a linter if available
```

### 2. Test Build
```bash
docker build -t truth-dare-backend:test .
```

**Expected Output:**
- ✅ Build succeeds
- ✅ Shows "JAR file: truth-dare-backend-0.0.1-SNAPSHOT.jar"
- ✅ Final image created
- ✅ No errors

### 3. Test Run
```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e PORT=8080 \
  -e CORS_ALLOWED_ORIGINS=* \
  truth-dare-backend:test
```

**Expected Output:**
- ✅ Container starts
- ✅ Shows Spring Boot banner
- ✅ Application starts on port 8080
- ✅ No errors

### 4. Test Health Endpoint
```bash
# In another terminal
curl http://localhost:8080/api/health
```

**Expected Response:**
```json
{"status":"UP","service":"truth-dare-backend"}
```

### 5. Check Image Size
```bash
docker images truth-dare-backend:test
```

**Expected:**
- Final image should be ~250-300MB
- Build stage is larger but not in final image

---

## ✅ Success Criteria

- [ ] Docker build completes without errors
- [ ] JAR file is created and copied
- [ ] Container runs successfully
- [ ] Application binds to PORT environment variable
- [ ] Health endpoint responds correctly
- [ ] Final image size is reasonable (< 500MB)

---

If all checks pass, you're ready for Render deployment! 🚀