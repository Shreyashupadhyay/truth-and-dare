# 🔧 Static Resource Configuration Fix

## Problem
Error when accessing `/api/rooms/health`:
```
java.io.FileNotFoundException: class path resource [static/index.html] cannot be resolved to URL because it does not exist
```

## Root Causes

1. **Wrong health endpoint path:**
   - You're accessing: `/api/rooms/health`
   - Correct path: `/api/health`

2. **StaticResourceConfig interfering with API routes:**
   - The `/**` pattern was catching ALL requests, including API endpoints
   - It tried to serve `static/index.html` even for API calls
   - Since static directory doesn't exist (backend-only Docker build), it failed

## Solutions Applied

### 1. Fixed StaticResourceConfig
- ✅ Now excludes `/api/**` and `/ws/**` routes
- ✅ Only handles static file requests
- ✅ Returns `null` if static files don't exist (allows proper 404)
- ✅ Won't interfere with API endpoints

### 2. Health Endpoint Path
The health endpoint is at:
- ✅ **Correct:** `/api/health`
- ❌ **Wrong:** `/api/rooms/health`

## Testing

### Test the correct health endpoint:
```bash
curl https://truth-and-dare-2.onrender.com/api/health
```

**Expected Response:**
```json
{"status":"UP","service":"truth-dare-backend"}
```

### Test API endpoints:
```bash
# Create room
curl -X POST https://truth-and-dare-2.onrender.com/api/rooms \
  -H "Content-Type: application/json" \
  -d '{"gameMode":"TRUTH_AND_DARE","playerName":"Test"}'

# Get room state
curl https://truth-and-dare-2.onrender.com/api/rooms/{roomCode}/state
```

## Next Steps

1. **Commit and push the fix:**
   ```bash
   git add src/main/java/com/truthdare/backend/config/StaticResourceConfig.java
   git commit -m "Fix StaticResourceConfig to not interfere with API routes"
   git push
   ```

2. **Render will auto-redeploy** - Wait for deployment

3. **Test with correct endpoint:**
   - Use `/api/health` (not `/api/rooms/health`)
   - Should return: `{"status":"UP","service":"truth-dare-backend"}`

## Summary

- ✅ Fixed: StaticResourceConfig now excludes API routes
- ✅ Fixed: Properly handles missing static files
- ✅ Note: Health endpoint is at `/api/health` (not `/api/rooms/health`)

---

**Status:** ✅ Fixed - Ready to redeploy!