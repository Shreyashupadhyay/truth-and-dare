@echo off
REM Build script for Windows
REM Builds both frontend and backend for deployment

echo Building Truth ^& Dare Application...

REM Build frontend
echo Building frontend...
cd frontend
call npm install
if %errorlevel% neq 0 (
    echo Frontend npm install failed!
    exit /b %errorlevel%
)

call npm run build
if %errorlevel% neq 0 (
    echo Frontend build failed!
    exit /b %errorlevel%
)
cd ..

REM Copy frontend build to backend static directory
echo Copying frontend build to backend static resources...
if exist src\main\resources\static rmdir /s /q src\main\resources\static
mkdir src\main\resources\static
xcopy /E /I /Y frontend\dist\* src\main\resources\static\

REM Build backend
echo Building backend...
call gradlew.bat clean build -x test
if %errorlevel% neq 0 (
    echo Backend build failed!
    exit /b %errorlevel%
)

echo.
echo Build complete!
echo JAR file location: build\libs\truth-dare-backend-*.jar
pause