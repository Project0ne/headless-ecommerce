#!/bin/bash

# Headless E-Commerce - One-Click Installer
# GitHub: https://github.com/Project0ne/headless-ecommerce

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Banner
echo -e "${CYAN}"
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║                                                              ║"
echo "║         🛒 Headless E-Commerce System Installer              ║"
echo "║                                                              ║"
echo "║         A WooCommerce-level headless ecommerce               ║"
echo "║         with theme switching support                         ║"
echo "║                                                              ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo -e "${NC}"

# Check Docker
echo -e "${BLUE}[1/4]${NC} Checking Docker..."
if ! command -v docker &> /dev/null; then
    echo -e "${RED}  ❌ Docker is not installed!${NC}"
    echo ""
    echo "  Please install Docker first:"
    echo "  https://docs.docker.com/get-docker/"
    exit 1
fi
echo -e "${GREEN}  ✅ Docker is installed${NC}"

# Check Docker Compose
echo -e "${BLUE}[2/4]${NC} Checking Docker Compose..."
if ! docker compose version &> /dev/null && ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}  ❌ Docker Compose is not installed!${NC}"
    exit 1
fi
echo -e "${GREEN}  ✅ Docker Compose is installed${NC}"

# Check if Docker is running
echo -e "${BLUE}[3/4]${NC} Checking Docker daemon..."
if ! docker info &> /dev/null; then
    echo -e "${RED}  ❌ Docker daemon is not running!${NC}"
    echo "  Please start Docker and try again."
    exit 1
fi
echo -e "${GREEN}  ✅ Docker daemon is running${NC}"

# Create .env if not exists
echo -e "${BLUE}[4/4]${NC} Setting up environment..."
if [ ! -f .env ]; then
    cp .env.example .env
    echo -e "${GREEN}  ✅ Created .env file with default settings${NC}"
else
    echo -e "${YELLOW}  ℹ️  .env file already exists, skipping${NC}"
fi

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo -e "  ${CYAN}Starting Headless E-Commerce System...${NC}"
echo "═══════════════════════════════════════════════════════════════"
echo ""

# Detect docker compose command
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
else
    COMPOSE_CMD="docker-compose"
fi

# Pull images
echo -e "${BLUE}[1/3]${NC} Pulling Docker images (this may take a few minutes)..."
$COMPOSE_CMD pull

# Build and start
echo ""
echo -e "${BLUE}[2/3]${NC} Building and starting services..."
$COMPOSE_CMD up -d --build

# Wait for services
echo ""
echo -e "${BLUE}[3/3]${NC} Waiting for services to be ready..."
sleep 15

# Check status
echo ""
$COMPOSE_CMD ps

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo -e "  ${GREEN}🎉 Installation Complete!${NC}"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "  📦 Access URLs:"
echo "  ─────────────────────────────────────────────────────────────"
echo "  🌐 Store:      http://localhost:3000"
echo "  ⚙️  Admin:      http://localhost:3000/admin"
echo "  📊 Settings:   http://localhost:3000/admin/settings"
echo "  🔧 API:        http://localhost:8080/api/v1"
echo "  📚 API Docs:   http://localhost:8080/swagger-ui.html"
echo ""
echo "  🔐 Default Admin Account:"
echo "  ─────────────────────────────────────────────────────────────"
echo "  Username: admin"
echo "  Password: admin123"
echo ""
echo -e "  ${YELLOW}⚠️  Please change the default password after first login!${NC}"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "  Useful Commands:"
echo "  ─────────────────────────────────────────────────────────────"
echo "  View logs:     $COMPOSE_CMD logs -f"
echo "  Stop:          $COMPOSE_CMD down"
echo "  Restart:       $COMPOSE_CMD restart"
echo "  Update:        $COMPOSE_CMD pull && $COMPOSE_CMD up -d"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""

# Ask to open browser
read -p "Open browser now? (Y/N): " open_browser
if [[ "$open_browser" =~ ^[Yy]$ ]]; then
    if command -v xdg-open &> /dev/null; then
        xdg-open http://localhost:3000
    elif command -v open &> /dev/null; then
        open http://localhost:3000
    else
        echo "  Please open http://localhost:3000 in your browser"
    fi
fi

echo ""
echo "Done!"
