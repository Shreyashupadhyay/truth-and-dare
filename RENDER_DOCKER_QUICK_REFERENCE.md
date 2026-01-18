# 🐳 Render Docker Configuration - Quick Reference

## ⚡ Exact Settings for Render Web Service

### Basic Settings:
```
Service Type: Web Service
Language: Docker ⚠️ (NOT Java!)
Build Command: (leave EMPTY)
Start Command: (leave EMPTY)
Dockerfile Path: Dockerfile
Branch: main
Region: Your choice (e.g., Oregon)
```

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
CORS_ALLOWED_ORIGINS=*
LOG_LEVEL=INFO
```

### Health Check (Advanced):
```
Health Check Path: /api/health
```

---

## 🎯 Why These Settings?

### Why "Docker" not "Java"?
- Render's "Java" option uses their managed runtime
- We're using Docker for full control
- Dockerfile handles everything

### Why Empty Build/Start Commands?
- Dockerfile has `FROM`, `RUN`, `ENTRYPOINT` that handle everything
- Render will: `docker build` then `docker run`
- No need for manual commands

---

## ✅ Verification Checklist

Before clicking "Deploy":

- [ ] Language = **Docker** (not Java)
- [ ] Build Command = **empty**
- [ ] Start Command = **empty**
- [ ] Dockerfile exists in root
- [ ] Environment variables added
- [ ] Code pushed to GitHub

---

## 🧪 Test Before Deploy

```bash
# Build
docker build -t truth-dare-backend .

# Run
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e PORT=8080 \
  truth-dare-backend

# Test
curl http://localhost:8080/api/health
```

---

## 🚀 That's It!

Click "Deploy Web Service" and wait 5-10 minutes.