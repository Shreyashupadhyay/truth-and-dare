# Truth & Dare - Complete Setup Guide

## Step-by-Step Instructions

### Prerequisites Check
- ✅ Java 17 or higher installed
- ✅ Node.js 18 or higher installed
- ✅ npm or yarn installed

### Step 1: Start the Backend (Terminal 1)

1. Open a terminal/PowerShell window
2. Navigate to the backend directory:
   ```powershell
   cd C:\Users\shrey\Downloads\backend\backend
   ```

3. Build the project (first time only):
   ```powershell
   .\gradlew.bat build
   ```
   Wait for this to complete (downloads dependencies on first run)

4. Start the backend server:
   ```powershell
   .\gradlew.bat bootRun
   ```

5. You should see:
   ```
   Started TruthDareBackendApplication in X.XXX seconds
   ```
   The backend is now running on **http://localhost:8080**

6. **Keep this terminal open** - don't close it

---

### Step 2: Start the Frontend (Terminal 2)

1. Open a **NEW** terminal/PowerShell window
2. Navigate to the frontend directory:
   ```powershell
   cd C:\Users\shrey\Downloads\backend\backend\frontend
   ```

3. Install dependencies (first time only):
   ```powershell
   npm install
   ```
   Wait for this to complete

4. Start the frontend development server:
   ```powershell
   npm run dev
   ```

5. You should see:
   ```
   VITE v5.0.8  ready in XXX ms

   ➜  Local:   http://localhost:3000/
   ➜  Network: use --host to expose
   ```

6. **Keep this terminal open** - don't close it

---

### Step 3: Open in Browser

1. Open your web browser (Chrome, Firefox, Edge, etc.)
2. Go to: **http://localhost:3000**
3. You should see the Truth & Dare landing page

---

### Step 4: Test the Application

1. **Create a Room:**
   - Enter your name (e.g., "Admin")
   - Select a game mode (e.g., "Truth & Dare")
   - Click "Create Room"
   - **Save the Admin Token** that appears!

2. **Join the Room (optional - open in another browser/tab):**
   - Enter the room code you received
   - Enter your name
   - Click "Join Room"

3. **Start the Game:**
   - Click "Start Game" button (as admin)
   - The game will begin!

---

## Troubleshooting

### Issue: Blank White Screen

**Check 1: Is the frontend server running?**
- Look at Terminal 2 - do you see the Vite server output?
- If not, run `npm run dev` again

**Check 2: Open Browser Console (F12)**
- Press F12 to open Developer Tools
- Click on the "Console" tab
- Look for any red error messages
- **Tell me what errors you see!**

**Check 3: Check Network Tab**
- In Developer Tools, click "Network" tab
- Refresh the page (F5)
- Look for any files with red status (404, 500, etc.)
- Check if `main.jsx` or other files are loading

**Check 4: Verify files exist**
```powershell
cd C:\Users\shrey\Downloads\backend\backend\frontend
dir src\main.jsx
dir src\App.jsx
dir index.html
```

### Issue: Backend won't start

**Check 1: Is Java installed?**
```powershell
java -version
```
Should show version 17 or higher

**Check 2: Is port 8080 available?**
- Close any other applications using port 8080
- Or change the port in `application.properties`

**Check 3: Check backend logs**
- Look at Terminal 1 for error messages
- Common issues: missing dependencies, port already in use

### Issue: Frontend can't connect to backend

**Check 1: Is backend running?**
- Go to http://localhost:8080 in browser
- Should see some response (even if it's an error page)

**Check 2: Check CORS settings**
- Backend should allow connections from localhost:3000
- This is already configured in SecurityConfig.java

---

## Quick Test

If you see a blank screen, let's test if React is working:

1. Open browser console (F12)
2. Type this and press Enter:
   ```javascript
   document.getElementById('root')
   ```
3. If it returns `null`, the root element doesn't exist (check index.html)
4. If it returns an element, React should be mounting to it

---

## Still Having Issues?

1. **Share the browser console errors** - This is the most helpful!
2. **Share terminal output** - Both backend and frontend terminals
3. **Take a screenshot** - Of the blank screen and console

The ErrorBoundary component I added will show errors on the screen instead of blank, so if you see an error page, that's progress!