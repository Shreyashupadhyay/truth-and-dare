#!/bin/bash
# Test Docker build and run locally before deploying to Render

echo "=========================================="
echo "Testing Docker Build for Render Deployment"
echo "=========================================="
echo ""

# Build Docker image
echo "Step 1: Building Docker image..."
docker build -t truth-dare-backend:test .

if [ $? -ne 0 ]; then
    echo "❌ Docker build failed!"
    exit 1
fi

echo "✅ Docker build successful!"
echo ""

# Check image size
echo "Step 2: Checking image size..."
docker images truth-dare-backend:test

echo ""
echo "Step 3: Running container..."
echo "Container will run on http://localhost:8080"
echo "Press Ctrl+C to stop"
echo ""

# Run container with production-like settings
docker run -it --rm \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e PORT=8080 \
  -e CORS_ALLOWED_ORIGINS=* \
  -e LOG_LEVEL=INFO \
  truth-dare-backend:test

echo ""
echo "Test complete!"