#!/bin/bash
# Build script for Render deployment
# This script builds both frontend and backend

echo "Building Truth & Dare Application..."

# Build frontend
echo "Building frontend..."
cd frontend
npm install
npm run build
cd ..

# Copy frontend build to backend static directory
echo "Copying frontend build to backend static resources..."
mkdir -p src/main/resources/static
cp -r frontend/dist/* src/main/resources/static/

# Build backend
echo "Building backend..."
./gradlew clean build -x test

echo "Build complete!"
echo "JAR file location: build/libs/truth-dare-backend-*.jar"