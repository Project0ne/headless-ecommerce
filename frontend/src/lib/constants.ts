/** API path constants */

export const API_PATHS = {
  // Auth
  AUTH: {
    REGISTER: "/auth/register",
    LOGIN: "/auth/login",
  },
  // User
  USER: {
    ME: "/users/me",
  },
  // Products
  PRODUCTS: {
    LIST: "/products",
    DETAIL: (id: number) => `/products/${id}`,
    SEARCH: "/products/search",
  },
  // Categories
  CATEGORIES: {
    LIST: "/categories",
  },
  // Cart
  CART: {
    GET: "/cart",
    ADD_ITEM: "/cart/items",
    UPDATE_ITEM: (productId: number) => `/cart/items/${productId}`,
    REMOVE_ITEM: (productId: number) => `/cart/items/${productId}`,
    MERGE: "/cart/merge",
  },
  // Orders
  ORDERS: {
    LIST: "/orders",
    CREATE: "/orders",
    DETAIL: (id: number) => `/orders/${id}`,
    CANCEL: (id: number) => `/orders/${id}/cancel`,
  },
  // Payments
  PAYMENTS: {
    PROCESS: (orderNo: string) => `/payments/${orderNo}`,
  },
  // Admin
  ADMIN: {
    PRODUCTS: "/admin/products",
    PRODUCT_DETAIL: (id: number) => `/admin/products/${id}`,
    PRODUCT_STATUS: (id: number) => `/admin/products/${id}/status`,
    CATEGORIES: "/admin/categories",
    CATEGORY_DETAIL: (id: number) => `/admin/categories/${id}`,
    ORDERS: "/admin/orders",
    ORDER_STATUS: (id: number) => `/admin/orders/${id}/status`,
    UPLOAD: "/admin/upload",
    DASHBOARD: "/admin/dashboard",
  },
} as const;
