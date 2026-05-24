#!/bin/bash
# ===================================
# Coachly - Start Script (Linux/Mac)
# ===================================
echo "Starting Coachly..."
echo ""
echo "Building and starting all services with Docker Compose..."
echo ""

docker-compose up --build

echo ""
echo "Coachly is running at http://localhost:8080"
