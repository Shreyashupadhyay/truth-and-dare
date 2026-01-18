# 🚀 Quick Start Guide - Truth & Dare

## ✅ Step-by-Step Instructions

### Step 1: Start Backend

**Open PowerShell/Terminal Window 1:**

```powershell
cd C:\Users\shrey\Downloads\backend\backend
.\gradlew.bat bootRun
```

**Wait for:** `Started TruthDareBackendApplication`

**✅ Backend is running on http://localhost:8080**

---

### Step 2: Start Frontend

**Open NEW PowerShell/Terminal Window 2:**

```powershell
cd C:\Users\shrey\Downloads\backend\backend\frontend
npm run dev
```

**Wait for:** 
```
VITE v5.0.8  ready in XXX ms
➜  Local:   http://localhost:3000/
```

**✅ Frontend is running on http://localhost:3000**

---

### Step 3: Open Browser

1. Open Chrome/Firefox/Edge
2. Go to: **http://localhost:3000**
3. You should see the Truth & Dare landing page!

---

## 🔍 If You See Blank Screen

### Check Browser Console (F12)

1. **Press F12** to open Developer Tools
2. Click **Console** tab
3. Look for **RED error messages**
4. **Tell me what errors you see!**

### Common Issues:

**❌ "Failed to fetch" or CORS errors**
- Backend is not running
- Solution: Make sure Step 1 is complete

**❌ "Cannot find module" errors**
- Dependencies not installed
- Solution: Run `npm install` in frontend directory

**❌ "404 Not Found" errors**
- Files missing or wrong path
- Solution: Check that all files exist in `frontend/src/`

**❌ No errors, just blank screen**
- React might not be rendering
- Check Network tab - are files loading?

---

## 🧪 Test React is Working

1. Open browser console (F12)
2. Type this and press Enter:
   ```javascript
   document.getElementById('root')
   ```
3. If you see `null` → Check index.html
4. If you see `<div id="root">...</div>` → React should mount here

---

## 📋 Verification Checklist

- [ ] Backend terminal shows "Started TruthDareBackendApplication"
- [ ] Frontend terminal shows "VITE ready" message
- [ ] Browser console (F12) shows no red errors
- [ ] Can see "Truth & Dare" text on page
- [ ] Can see form with "Create Room" and "Join Room" tabs

---

## 🆘 Still Stuck?

**Share with me:**
1. Screenshot of the blank screen
2. Screenshot of browser console (F12 → Console tab)
3. Output from both terminals (backend and frontend)
4. Any error messages you see

This will help me identify the exact issue!