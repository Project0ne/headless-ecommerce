@echo off
chcp 65001 >nul
title Headless E-Commerce - One-Click Installer
color 0A

echo.
echo  ╔══════════════════════════════════════════════════════════════╗
echo  ║                                                              ║
echo  ║         🛒 Headless E-Commerce System Installer              ║
echo  ║                                                              ║
echo  ║         A WooCommerce-level headless ecommerce               ║
echo  ║         with theme switching support                         ║
echo  ║                                                              ║
echo  ╚══════════════════════════════════════════════════════════════╝
echo.

:: Check Docker
echo [1/4] Checking Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo  ❌ Docker is not installed!
    echo.
    echo  Please install Docker Desktop first:
    echo  https://www.docker.com/products/docker-desktop
    echo.
    pause
    exit /b 1
)
echo  ✅ Docker is installed

:: Check Docker Compose
echo [2/4] Checking Docker Compose...
docker compose version >nul 2>&1
if %errorlevel% neq 0 (
    docker-compose --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo.
        echo  ❌ Docker Compose is not installed!
        echo.
        pause
        exit /b 1
    )
)
echo  ✅ Docker Compose is installed

:: Check if Docker is running
echo [3/4] Checking Docker daemon...
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo  ❌ Docker daemon is not running!
    echo  Please start Docker Desktop and try again.
    echo.
    pause
    exit /b 1
)
echo  ✅ Docker daemon is running

:: Create .env if not exists
echo [4/4] Setting up environment...
if not exist .env (
    copy .env.example .env >nul
    echo  ✅ Created .env file with default settings
) else (
    echo  ℹ️  .env file already exists, skipping
)

echo.
echo ══════════════════════════════════════════════════════════════
echo  Starting Headless E-Commerce System...
echo ══════════════════════════════════════════════════════════════
echo.

:: Pull and start services
echo [1/3] Pulling Docker images (this may take a few minutes)...
docker compose pull

echo.
echo [2/3] Building and starting services...
docker compose up -d --build

echo.
echo [3/3] Waiting for services to be ready...
timeout /t 10 /nobreak >nul

:: Check service status
echo.
docker compose ps

echo.
echo ══════════════════════════════════════════════════════════════
echo.
echo  🎉 Installation Complete!
echo.
echo ══════════════════════════════════════════════════════════════
echo.
echo  📦 Access URLs:
echo  ─────────────────────────────────────────────────────────────
echo  🌐 Store:      http://localhost:3000
echo  ⚙️  Admin:      http://localhost:3000/admin
echo  📊 Settings:   http://localhost:3000/admin/settings
echo  🔧 API:        http://localhost:8080/api/v1
echo  📚 API Docs:   http://localhost:8080/swagger-ui.html
echo.
echo  🔐 Default Admin Account:
echo  ─────────────────────────────────────────────────────────────
echo  Username: admin
echo  Password: admin123
echo.
echo  ⚠️  Please change the default password after first login!
echo.
echo ══════════════════════════════════════════════════════════════
echo.
echo  Useful Commands:
echo  ─────────────────────────────────────────────────────────────
echo  View logs:     docker compose logs -f
echo  Stop:          docker compose down
echo  Restart:       docker compose restart
echo  Update:        docker compose pull ^&^& docker compose up -d
echo.
echo ══════════════════════════════════════════════════════════════
echo.

:: Open browser
set /p open_browser="Open browser now? (Y/N): "
if /i "%open_browser%"=="Y" (
    start http://localhost:3000
)

echo.
echo Press any key to exit...
pause >nul
