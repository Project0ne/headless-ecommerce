# Headless E-Commerce System

[中文](#中文) | English

---

# Headless E-Commerce System

A WooCommerce-level headless e-commerce system with theme switching support, built with Spring Boot 3.3 + Next.js 15.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)
![Next.js](https://img.shields.io/badge/Next.js-15-black)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Features

### Core E-commerce
- 🛍️ Product management with multi-image gallery
- 🛒 Shopping cart with persistent storage
- 📦 Order management with status tracking
- 👤 User authentication (JWT + OAuth2)
- ⭐ Product reviews and ratings
- 🏷️ Coupon and promotion system
- 📊 Sales analytics dashboard

### Theme System
- 🎨 5 preset themes (Classic Blue, Minimalist White, Neon Purple, Warm Orange, Forest Green)
- 🌙 Dark/Light mode toggle
- 💾 Theme persistence via localStorage
- 🔄 Real-time theme preview

### Admin Settings (17 Modules)
| Module | Description |
|--------|-------------|
| Basic Info | Store name, logo, timezone, contact |
| Payment | Alipay, WeChat Pay, Credit Card, PayPal |
| Shipping | Shipping methods, free shipping threshold |
| Tax | Tax rates by country/region |
| Currency | Multi-currency support (CNY/USD/EUR/GBP/JPY/HKD) |
| Domain | Custom domain binding |
| Language | Multi-language support (i18n) |
| Customer Login | Registration and login settings |
| Checkout | Checkout flow configuration |
| Notifications | Email notification templates |
| Policies | Privacy, refund, terms, shipping policies |
| Staff | RBAC permission system (5 levels) |
| Audit Logs | Operation logging |
| Media Library | File and image management |
| Partners | Third-party integrations |
| Merchant Notifications | Admin notification settings |
| Tip | Tipping system configuration |

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.3
- **Database**: MySQL 8
- **Cache**: Redis 7
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway (11 migrations)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven

### Frontend
- **Framework**: Next.js 15 (App Router)
- **Styling**: Tailwind CSS 4
- **UI Components**: Shadcn UI
- **State Management**: Zustand
- **Data Fetching**: TanStack Query (React Query)
- **Charts**: Chart.js
- **Language**: TypeScript

## Quick Start

### Prerequisites
- Node.js 18+
- Java 17+
- Docker & Docker Compose
- Git

### 1. Clone Repository

```bash
git clone https://github.com/Project0ne/headless-ecommerce.git
cd headless-ecommerce
```

### 2. Environment Configuration

```bash
# Copy environment template
cp .env.example .env

# Edit .env file with your settings
# Windows (PowerShell):
Copy-Item .env.example .env
notepad .env
```

**Required environment variables:**

```env
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce
DB_USERNAME=root
DB_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=7200000

# Frontend
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. Start with Docker Compose (Recommended)

```bash
# Start all services (MySQL, Redis, Backend, Frontend)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### 4. Manual Start (Development)

#### Start Backend

```bash
cd backend

# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

Backend will start at: http://localhost:8080

#### Start Frontend

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will start at: http://localhost:3000

### 5. Access the Application

| Service | URL |
|---------|-----|
| Frontend Store | http://localhost:3000 |
| Admin Dashboard | http://localhost:3000/admin |
| Admin Settings | http://localhost:3000/admin/settings |
| Backend API | http://localhost:8080/api/v1 |
| API Docs (Swagger) | http://localhost:8080/swagger-ui.html |

## Project Structure

```
headless-ecommerce/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/headless/ecommerce/
│   │   ├── controller/              # REST Controllers
│   │   │   ├── admin/               # Admin API endpoints
│   │   │   └── *.java               # Public API endpoints
│   │   ├── model/                   # JPA Entities
│   │   │   └── enums/               # Enum types
│   │   ├── repository/              # Data Repositories
│   │   ├── service/                 # Business Logic
│   │   │   └── impl/                # Service implementations
│   │   ├── dto/                     # Data Transfer Objects
│   │   │   ├── request/             # Request DTOs
│   │   │   └── response/            # Response DTOs
│   │   ├── config/                  # Configuration classes
│   │   ├── security/                # Security configuration
│   │   └── scheduler/               # Scheduled tasks
│   ├── src/main/resources/
│   │   ├── db/migration/            # Flyway migrations
│   │   ├── application.yml          # Main config
│   │   ├── application-dev.yml      # Dev profile
│   │   └── application-prod.yml     # Prod profile
│   └── pom.xml
│
├── frontend/                        # Next.js Frontend
│   ├── src/
│   │   ├── app/                     # App Router pages
│   │   │   ├── (shop)/              # Store pages
│   │   │   ├── admin/               # Admin pages
│   │   │   └── api/                 # API routes
│   │   ├── components/              # React components
│   │   │   ├── ui/                  # Shadcn UI components
│   │   │   ├── admin/               # Admin components
│   │   │   ├── product/             # Product components
│   │   │   ├── cart/                # Cart components
│   │   │   ├── layout/              # Layout components
│   │   │   └── common/              # Shared components
│   │   ├── services/                # API service layer
│   │   ├── stores/                  # Zustand stores
│   │   ├── lib/                     # Utilities (themes, etc.)
│   │   └── types/                   # TypeScript types
│   ├── public/                      # Static assets
│   └── package.json
│
├── .env.example                     # Environment template
├── docker-compose.yml               # Docker orchestration
└── README.md
```

## API Endpoints

### Public API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/products` | Product list |
| GET | `/api/v1/products/{id}` | Product detail |
| GET | `/api/v1/categories` | Category list |
| POST | `/api/v1/auth/register` | User registration |
| POST | `/api/v1/auth/login` | User login |
| GET | `/api/v1/cart` | Get cart |
| POST | `/api/v1/cart/items` | Add to cart |
| POST | `/api/v1/orders` | Create order |

### Admin API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/admin/store/config` | Store config |
| PUT | `/api/v1/admin/store/config` | Update store config |
| GET | `/api/v1/admin/payment-methods` | Payment methods |
| GET | `/api/v1/admin/shipping-methods` | Shipping methods |
| GET | `/api/v1/admin/tax-rates` | Tax rates |
| GET | `/api/v1/admin/currencies` | Currencies |
| GET | `/api/v1/admin/policies` | Policies |
| GET | `/api/v1/admin/staff` | Staff list |
| GET | `/api/v1/admin/audit-logs` | Audit logs |
| GET | `/api/v1/admin/media` | Media library |

## Default Credentials

### Admin Account
- Username: `admin`
- Password: `admin123`

> ⚠️ Change the default password in production!

## Customization

### Adding New Themes

Edit `frontend/src/lib/themes.ts`:

```typescript
export const themes: Theme[] = [
  // ... existing themes
  {
    id: "custom-theme",
    name: "Custom Theme",
    isDark: true,
    colors: {
      primary: "hsl(220, 90%, 56%)",
      background: "hsl(224, 71%, 4%)",
      card: "hsl(224, 71%, 8%)",
      // ... more colors
    },
  },
];
```

### Adding New Payment Methods

1. Add enum value to `PaymentType.java`
2. Create migration script
3. Add to admin payment settings

## Troubleshooting

### Common Issues

**1. Database connection failed**
```bash
# Check MySQL is running
docker-compose ps mysql

# View MySQL logs
docker-compose logs mysql
```

**2. Port already in use**
```bash
# Kill process on port 8080
# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux:
lsof -ti:8080 | xargs kill -9
```

**3. Frontend build errors**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run build
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- GitHub: [@Project0ne](https://github.com/Project0ne)
- Repository: https://github.com/Project0ne/headless-ecommerce

---

# 中文

一个支持主题切换的 WooCommerce 级别 Headless 电商系统，基于 Spring Boot 3.3 + Next.js 15 构建。

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)
![Next.js](https://img.shields.io/badge/Next.js-15-black)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 功能特性

### 核心电商功能
- 🛍️ 商品管理，支持多图展示
- 🛒 购物车，数据持久化
- 📦 订单管理，状态追踪
- 👤 用户认证（JWT + OAuth2）
- ⭐ 商品评价和评分
- 🏷️ 优惠券和促销系统
- 📊 销售数据分析看板

### 主题系统
- 🎨 5 套预设主题（经典蓝、极简白、霓虹紫、暖阳橙、森林绿）
- 🌙 明暗模式切换
- 💾 localStorage 主题持久化
- 🔄 实时主题预览

### 后台设置（17 个模块）
| 模块 | 说明 |
|------|------|
| 基本信息 | 店铺名称、Logo、时区、联系方式 |
| 收款 | 支付宝、微信支付、信用卡、PayPal |
| 物流配送 | 物流方式、包邮门槛 |
| 税费 | 按国家/地区设置税率 |
| 货币 | 多货币支持（CNY/USD/EUR/GBP/JPY/HKD） |
| 设置域名 | 自定义域名绑定 |
| 店铺语言 | 多语言支持（i18n） |
| 客户登录 | 注册和登录设置 |
| 结账 | 结账流程配置 |
| 消息通知 | 邮件通知模板 |
| 政策条款 | 隐私政策、退款政策、服务条款、配送政策 |
| 员工账号 | RBAC 权限系统（5 级） |
| 操作日志 | 操作审计日志 |
| 素材库 | 文件和图片管理 |
| 合作伙伴 | 第三方集成 |
| 商家通知 | 管理员通知设置 |
| 小费 | 小费功能配置 |

## 技术栈

### 后端
- **框架**: Spring Boot 3.3
- **数据库**: MySQL 8
- **缓存**: Redis 7
- **ORM**: Spring Data JPA + Hibernate
- **迁移**: Flyway（11 个迁移脚本）
- **安全**: Spring Security + JWT
- **构建工具**: Maven

### 前端
- **框架**: Next.js 15（App Router）
- **样式**: Tailwind CSS 4
- **UI 组件**: Shadcn UI
- **状态管理**: Zustand
- **数据获取**: TanStack Query（React Query）
- **图表**: Chart.js
- **语言**: TypeScript

## 快速开始

### 环境要求
- Node.js 18+
- Java 17+
- Docker & Docker Compose
- Git

### 1. 克隆仓库

```bash
git clone https://github.com/Project0ne/headless-ecommerce.git
cd headless-ecommerce
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，填入你的配置
# Windows（PowerShell）:
Copy-Item .env.example .env
notepad .env
```

**必需的环境变量：**

```env
# 数据库
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce
DB_USERNAME=root
DB_PASSWORD=你的密码

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=你的JWT密钥
JWT_EXPIRATION=7200000

# 前端
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. 使用 Docker Compose 启动（推荐）

```bash
# 启动所有服务（MySQL、Redis、后端、前端）
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止所有服务
docker-compose down
```

### 4. 手动启动（开发环境）

#### 启动后端

```bash
cd backend

# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或直接使用 Maven
mvn spring-boot:run
```

后端启动地址：http://localhost:8080

#### 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端启动地址：http://localhost:3000

### 5. 访问应用

| 服务 | 地址 |
|------|------|
| 前台商店 | http://localhost:3000 |
| 管理后台 | http://localhost:3000/admin |
| 后台设置 | http://localhost:3000/admin/settings |
| 后端 API | http://localhost:8080/api/v1 |
| API 文档（Swagger） | http://localhost:8080/swagger-ui.html |

## 项目结构

```
headless-ecommerce/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/headless/ecommerce/
│   │   ├── controller/              # REST 控制器
│   │   │   ├── admin/               # 管理 API
│   │   │   └── *.java               # 公开 API
│   │   ├── model/                   # JPA 实体
│   │   │   └── enums/               # 枚举类型
│   │   ├── repository/              # 数据仓库
│   │   ├── service/                 # 业务逻辑
│   │   │   └── impl/                # 服务实现
│   │   ├── dto/                     # 数据传输对象
│   │   │   ├── request/             # 请求 DTO
│   │   │   └── response/            # 响应 DTO
│   │   ├── config/                  # 配置类
│   │   ├── security/                # 安全配置
│   │   └── scheduler/               # 定时任务
│   ├── src/main/resources/
│   │   ├── db/migration/            # Flyway 迁移脚本
│   │   ├── application.yml          # 主配置
│   │   ├── application-dev.yml      # 开发环境配置
│   │   └── application-prod.yml     # 生产环境配置
│   └── pom.xml
│
├── frontend/                        # Next.js 前端
│   ├── src/
│   │   ├── app/                     # App Router 页面
│   │   │   ├── (shop)/              # 商店页面
│   │   │   ├── admin/               # 管理页面
│   │   │   └── api/                 # API 路由
│   │   ├── components/              # React 组件
│   │   │   ├── ui/                  # Shadcn UI 组件
│   │   │   ├── admin/               # 管理组件
│   │   │   ├── product/             # 商品组件
│   │   │   ├── cart/                # 购物车组件
│   │   │   ├── layout/              # 布局组件
│   │   │   └── common/              # 共享组件
│   │   ├── services/                # API 服务层
│   │   ├── stores/                  # Zustand 状态
│   │   ├── lib/                     # 工具类（主题等）
│   │   └── types/                   # TypeScript 类型
│   ├── public/                      # 静态资源
│   └── package.json
│
├── .env.example                     # 环境变量模板
├── docker-compose.yml               # Docker 编排
└── README.md
```

## API 端点

### 公开 API
| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/v1/products` | 商品列表 |
| GET | `/api/v1/products/{id}` | 商品详情 |
| GET | `/api/v1/categories` | 分类列表 |
| POST | `/api/v1/auth/register` | 用户注册 |
| POST | `/api/v1/auth/login` | 用户登录 |
| GET | `/api/v1/cart` | 获取购物车 |
| POST | `/api/v1/cart/items` | 添加到购物车 |
| POST | `/api/v1/orders` | 创建订单 |

### 管理 API
| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/v1/admin/store/config` | 店铺配置 |
| PUT | `/api/v1/admin/store/config` | 更新店铺配置 |
| GET | `/api/v1/admin/payment-methods` | 收款方式 |
| GET | `/api/v1/admin/shipping-methods` | 物流方式 |
| GET | `/api/v1/admin/tax-rates` | 税率 |
| GET | `/api/v1/admin/currencies` | 货币 |
| GET | `/api/v1/admin/policies` | 政策条款 |
| GET | `/api/v1/admin/staff` | 员工列表 |
| GET | `/api/v1/admin/audit-logs` | 操作日志 |
| GET | `/api/v1/admin/media` | 素材库 |

## 默认账户

### 管理员账号
- 用户名：`admin`
- 密码：`admin123`

> ⚠️ 生产环境请务必修改默认密码！

## 自定义配置

### 添加新主题

编辑 `frontend/src/lib/themes.ts`：

```typescript
export const themes: Theme[] = [
  // ... 现有主题
  {
    id: "custom-theme",
    name: "自定义主题",
    isDark: true,
    colors: {
      primary: "hsl(220, 90%, 56%)",
      background: "hsl(224, 71%, 4%)",
      card: "hsl(224, 71%, 8%)",
      // ... 更多颜色
    },
  },
];
```

### 添加新收款方式

1. 在 `PaymentType.java` 添加枚举值
2. 创建迁移脚本
3. 添加到管理后台收款设置

## 常见问题

### 1. 数据库连接失败
```bash
# 检查 MySQL 是否运行
docker-compose ps mysql

# 查看 MySQL 日志
docker-compose logs mysql
```

### 2. 端口被占用
```bash
# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux:
lsof -ti:8080 | xargs kill -9
```

### 3. 前端构建错误
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run build
```

## 贡献

1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/amazing-feature`）
3. 提交更改（`git commit -m 'Add amazing feature'`）
4. 推送到分支（`git push origin feature/amazing-feature`）
5. 开启 Pull Request

## 许可证

本项目基于 MIT 许可证开源 - 详见 [LICENSE](LICENSE) 文件。

## 联系方式

- GitHub: [@Project0ne](https://github.com/Project0ne)
- 仓库地址: https://github.com/Project0ne/headless-ecommerce
