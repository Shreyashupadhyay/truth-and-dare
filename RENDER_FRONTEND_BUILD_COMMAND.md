# ⚡ Render Frontend Build Command Fix

## ❌ Current Build Command (Not Working):
```
npm install; npm run build
```

## ✅ Correct Build Command (Use This):
```
npm install && npx vite build
```

**Why?**
- `npx vite build` explicitly finds vite in node_modules
- Works even if PATH isn't set correctly
- More reliable in Render's build environment

---

## 📋 Complete Render Static Site Configuration

### Basic Settings:
- **Name:** `truth-dare-frontend`
- **Service Type:** Static Site
- **Root Directory:** `frontend` (if frontend is in subdirectory)

### Build Settings:
- **Build Command:** 
  ```bash
  npm install && npx vite build
  ```
- **Publish Directory:** 
  ```
  dist
  ```

### Environment Variables:
```
VITE_API_URL=https://truth-and-dare-2.onrender.com/api
VITE_WS_URL=wss://truth-and-dare-2.onrender.com/ws
```

⚠️ **Replace with your actual backend URL!**

---

## 🔄 Steps to Fix

1. Go to your Static Site on Render
2. Click "Settings"
3. Scroll to "Build Command"
4. Change to: `npm install && npx vite build`
5. Click "Save Changes"
6. Trigger a new deploy

---

**That's it! The build should now succeed. 🎉**