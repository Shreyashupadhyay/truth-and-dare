# 🎯 Render Configuration Guide

Based on your Render setup form, here are the exact values to use:

## 📋 Basic Settings (First Screen)

### Name
```
truth-and-dare
```
*(You already have this correct)*

### Project (Optional)
- Leave as is or select your project
- Your selection: `Chat_Randome / Production` ✓

### Language ⚠️ **IMPORTANT - CHANGE THIS!**
**Current:** `Node` ❌  
**Should be:** `Java` ✓

**Action Required:**
1. Click the Language dropdown
2. Select **"Java"** (not Node!)

### Branch
```
main
```
*(You already have this correct)*

### Region
```
Oregon (US West)
```
*(You already have this correct)*

---

## 🔧 Build & Deploy Settings (Second Screen)

### Root Directory (Optional)
```
(Leave empty)
```

### Build Command ⚠️ **IMPORTANT - CHANGE THIS!**
**Current:** `$ yarn` ❌  
**Should be:**
```bash
cd frontend && npm install && npm run build && cd .. && ./gradlew clean build -x test
```

**Alternative (if the above doesn't work):**
```bash
chmod +x build.sh && ./build.sh
```

### Start Command ⚠️ **IMPORTANT - CHANGE THIS!**
**Current:** `$ yarn start` ❌  
**Should be:**
```bash
java -jar build/libs/*-SNAPSHOT.jar
```

**Alternative (more specific):**
```bash
java -jar build/libs/truth-dare-backend-0.0.1-SNAPSHOT.jar
```

---

## 🌍 Environment Variables (Third Screen)

Click **"Add Environment Variable"** and add these:

### Required Variables:

| Variable Name | Value | Description |
|--------------|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Use production profile |
| `CORS_ALLOWED_ORIGINS` | `*` | Allow all origins (update later with your URL) |

### Optional Variables:

| Variable Name | Value | Description |
|--------------|-------|-------------|
| `LOG_LEVEL` | `INFO` | Logging level (INFO, DEBUG, WARN) |
| `FRONTEND_URL` | `https://your-app.onrender.com` | Your Render URL (after deployment) |

**Note:** Render automatically sets `PORT` environment variable, so you don't need to add it.

---

## 🏥 Health Check Path (Advanced Settings)

Click "Advanced" to expand, then set:

**Health Check Path:**
```
/api/health
```

**Or leave empty** - Render will auto-detect.

---

## ✅ Summary - What to Change

### Must Change:

1. **Language:** Change from `Node` → `Java`
2. **Build Command:** Change from `$ yarn` → 
   ```bash
   cd frontend && npm install && npm run build && cd .. && ./gradlew clean build -x test
   ```
3. **Start Command:** Change from `$ yarn start` → 
   ```bash
   java -jar build/libs/*-SNAPSHOT.jar
   ```

### Add Environment Variables:

- `SPRING_PROFILES_ACTIVE` = `prod`
- `CORS_ALLOWED_ORIGINS` = `*`

---

## 🚀 After Configuration

1. Click **"Deploy Web Service"** button (bottom of the page)
2. Wait 5-10 minutes for the first build
3. Watch the logs for any errors
4. Once deployed, you'll get a URL like: `https://truth-and-dare.onrender.com`

---

## 📝 Quick Copy-Paste Commands

### Build Command:
```bash
cd frontend && npm install && npm run build && cd .. && ./gradlew clean build -x test
```

### Start Command:
```bash
java -jar build/libs/*-SNAPSHOT.jar
```

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
CORS_ALLOWED_ORIGINS=*
```

---

## ⚠️ Common Mistakes to Avoid

1. ❌ **Don't leave Language as "Node"** - Must be "Java"
2. ❌ **Don't use `yarn` commands** - Use npm and gradle
3. ❌ **Don't forget to build frontend** - Frontend must be built first
4. ✅ **Do include frontend build in build command**
5. ✅ **Do use the SNAPSHOT jar file name pattern**

---

## 🔍 After First Deployment

Once you get your Render URL (e.g., `https://truth-and-dare-abc123.onrender.com`):

1. **Update CORS** (optional):
   - Go to Environment Variables
   - Update `CORS_ALLOWED_ORIGINS` to your specific URL
   - Or leave as `*` for development

2. **Test your app:**
   - Visit: `https://your-app.onrender.com`
   - Health check: `https://your-app.onrender.com/api/health`
   - Should return: `{"status":"UP","service":"truth-dare-backend"}`

---

**Need help?** Check the build logs in Render dashboard if deployment fails!