# ✅ Fix Applied: global is not defined

## Problem
The error `Uncaught ReferenceError: global is not defined` was occurring because `sockjs-client` expects a `global` variable (which exists in Node.js but not in browsers).

## Solution Applied
1. ✅ Updated `vite.config.js` to define `global` as `globalThis`
2. ✅ Added a polyfill script in `index.html` as a fallback

## Next Steps

### IMPORTANT: Restart the Frontend Server

1. **Stop the current frontend server:**
   - Go to the terminal where `npm run dev` is running
   - Press `Ctrl + C` to stop it

2. **Start it again:**
   ```powershell
   cd C:\Users\shrey\Downloads\backend\backend\frontend
   npm run dev
   ```

3. **Refresh your browser:**
   - Go to http://localhost:3000
   - Press `Ctrl + Shift + R` (hard refresh) to clear cache
   - Or open in incognito/private window

4. **Check the console:**
   - Press F12
   - The error should be gone!
   - You should now see the Truth & Dare landing page

## What Changed

**File: `frontend/vite.config.js`**
- Added `define: { global: 'globalThis' }` to polyfill the global variable

**File: `frontend/index.html`**
- Added a script to ensure global is defined before React loads

## If It Still Doesn't Work

1. Make sure you restarted the dev server (this is required!)
2. Hard refresh the browser (Ctrl + Shift + R)
3. Check the console for any new errors
4. Make sure the backend is running on port 8080