# Start Frontend Development Server
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Truth & Dare Frontend Server" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$frontendDir = "C:\Users\shrey\Downloads\backend\backend\frontend"

if (Test-Path $frontendDir) {
    Set-Location $frontendDir
    Write-Host "Frontend directory: $frontendDir" -ForegroundColor Green
    
    # Check if node_modules exists
    if (-not (Test-Path "node_modules")) {
        Write-Host "Installing dependencies..." -ForegroundColor Yellow
        npm install
        Write-Host ""
    }
    
    Write-Host "Starting development server..." -ForegroundColor Yellow
    Write-Host ""
    npm run dev
} else {
    Write-Host "ERROR: Frontend directory not found at: $frontendDir" -ForegroundColor Red
    Write-Host "Please check the path and try again." -ForegroundColor Red
    Read-Host "Press Enter to exit"
}