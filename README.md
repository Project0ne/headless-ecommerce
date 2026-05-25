# Headless E-Commerce

A modern headless e-commerce platform built with **Spring Boot 3.3** (backend) and **Next.js 15** (frontend), featuring a premium UI with Tailwind CSS v4 and Shadcn UI.

## Tech Stack

### Frontend
- **Next.js 15** — App Router, Server Components
- **Tailwind CSS v4** — `@theme` syntax, CSS-first configuration
- **Shadcn UI** — Radix primitives + cva styling
- **React Query** — Data fetching, caching, and synchronization
- **Zustand** — Client state management (auth, cart)
- **TypeScript** — Strict mode

### Backend
- **Spring Boot 3.3** — REST API with JWT authentication
- **Spring Security** — Role-based access control (BUYER / ADMIN)
- **Spring Data JPA** — ORM with Flyway migrations
- **MySQL 8** — Primary database
- **Redis 7** — Cart storage and session cache

## Project Structure

```
├── frontend/               # Next.js 15 frontend
│   ├── src/
│   │   ├── app/            # App Router pages
│   │   │   ├── (shop)/     # Shop layout group (products, cart, orders, etc.)
│   │   │   ├── admin/      # Admin dashboard
│   │   │   └── auth/       # Login & register
│   │   ├── components/
│   │   │   ├── ui/         # Shadcn UI components
│   │   │   ├── common/     # Shared components (ThemeProvider, ThemeToggle, etc.)
│   │   │   ├── layout/     # Header, Footer, AdminSidebar
│   │   │   ├── product/    # ProductCard, ProductGrid, ProductFilters
│   │   │   ├── cart/       # CartIcon, CartItemRow, CartSummary
│   │   │   └── order/      # OrderItemList, OrderStatusBadge
│   │   ├── hooks/          # React Query hooks
│   │   ├── services/       # API service layer
│   │   ├── stores/         # Zustand stores
│   │   ├── types/          # TypeScript type definitions
│   │   └── lib/            # Utilities, constants, API client
│   └── public/             # Static assets
│
├── backend/                # Spring Boot 3.3 backend
│   └── src/main/java/com/headless/ecommerce/
│       ├── controller/     # REST controllers
│       ├── service/        # Business logic
│       ├── repository/     # Data access
│       ├── model/          # JPA entities
│       ├── dto/            # Request/Response DTOs
│       ├── config/         # Security, CORS, Redis, etc.
│       ├── security/       # JWT filter & token provider
│       └── mapper/         # Entity-DTO mappers
│
└── docs/                   # Documentation
    ├── prd.md              # Product Requirements Document
    ├── architecture.md     # Architecture overview
    ├── class-diagram.mermaid
    └── sequence-diagram.mermaid
```

## Getting Started

### Prerequisites

- **Node.js** 18+ (recommended: 20+)
- **Java** 17+
- **Maven** 3.8+
- **MySQL** 8.0+
- **Redis** 7.0+

### Backend Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE headless_ecommerce;
   ```

2. Configure `backend/src/main/resources/application-dev.yml` with your database credentials.

3. Run the backend:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

### Frontend Setup

1. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```

2. (Optional) Create `.env.local` for custom API URL:
   ```
   NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:3000`.

## API Endpoints

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| POST | `/api/v1/auth/register` | Register a new user | Public |
| POST | `/api/v1/auth/login` | Login | Public |
| GET | `/api/v1/products` | List products (paginated) | Public |
| GET | `/api/v1/products/{id}` | Get product details | Public |
| GET | `/api/v1/products/search` | Search products | Public |
| GET | `/api/v1/categories` | List categories | Public |
| GET | `/api/v1/users/me` | Get current user profile | BUYER/ADMIN |
| PUT | `/api/v1/users/me` | Update profile | BUYER/ADMIN |
| GET | `/api/v1/cart` | Get cart | BUYER/ADMIN |
| POST | `/api/v1/cart/items` | Add item to cart | BUYER/ADMIN |
| PUT | `/api/v1/cart/items/{productId}` | Update cart item | BUYER/ADMIN |
| DELETE | `/api/v1/cart/items/{productId}` | Remove cart item | BUYER/ADMIN |
| POST | `/api/v1/cart/merge` | Merge local cart | BUYER/ADMIN |
| POST | `/api/v1/orders` | Create order | BUYER/ADMIN |
| GET | `/api/v1/orders` | List user orders | BUYER/ADMIN |
| GET | `/api/v1/orders/{id}` | Get order details | BUYER/ADMIN |
| PUT | `/api/v1/orders/{id}/cancel` | Cancel order | BUYER/ADMIN |
| POST | `/api/v1/payments/{orderNo}` | Process payment | BUYER/ADMIN |
| GET | `/api/v1/admin/products` | List all products | ADMIN |
| POST | `/api/v1/admin/products` | Create product | ADMIN |
| PUT | `/api/v1/admin/products/{id}` | Update product | ADMIN |
| DELETE | `/api/v1/admin/products/{id}` | Delete product | ADMIN |
| GET | `/api/v1/admin/orders` | List all orders | ADMIN |
| PUT | `/api/v1/admin/orders/{id}/status` | Update order status | ADMIN |
| GET | `/api/v1/admin/dashboard` | Dashboard stats | ADMIN |

## Features

### Shop
- Product browsing with category filtering and keyword search
- Product detail pages with image gallery, stock status, and quantity selector
- Shopping cart with real-time updates (server-side for authenticated users, local storage for guests)
- Cart merge on login (local items automatically synced to server)
- Checkout flow with order creation
- Order history with status tracking

### Admin
- Product management (CRUD, status toggle)
- Category management (CRUD with tree structure)
- Order management with status updates
- Dashboard with statistics

### UI/UX
- Dark mode with system preference detection and manual toggle
- Glassmorphism header with backdrop blur
- Gradient accents and smooth micro-interactions
- Responsive design (mobile + desktop)
- Accessible Shadcn UI components

## Architecture

The system follows a **headless architecture** where the frontend and backend are fully decoupled:

- **Frontend**: Pure SPA consuming REST APIs, with local state management via Zustand
- **Backend**: Stateless REST API with JWT authentication, Redis-backed cart
- **Communication**: JSON over HTTP, proxied through Next.js rewrites in development

## License

MIT
