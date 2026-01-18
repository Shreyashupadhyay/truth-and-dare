# Start Backend Server
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Truth & Dare Backend Server" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$backendDir = "C:\Users\shrey\Downloads\backend\backend"

if (Test-Path $backendDir) {
    Set-Location $backendDir
    Write-Host "Backend directory: $backendDir" -ForegroundColor Green
    Write-Host "Building and starting server..." -ForegroundColor Yellow
    Write-Host ""
    .\gradlew.bat bootRun
} else {
    Write-Host "ERROR: Backend directory not found at: $backendDir" -ForegroundColor Red
    Write-Host "Please check the path and try again." -ForegroundColor Red
    Read-Host "Press Enter to exit"
}