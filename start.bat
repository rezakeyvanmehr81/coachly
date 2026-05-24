@echo off
REM ===================================
REM Coachly - Start Script (Windows)
REM ===================================
echo Starting Coachly...
echo.
echo Building and starting all services with Docker Compose...
echo.

docker-compose up --build

echo.
echo Coachly is running at http://localhost:8080
pause
