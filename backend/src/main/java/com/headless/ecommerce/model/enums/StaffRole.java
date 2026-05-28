package com.headless.ecommerce.model.enums;

/**
 * Staff role enumeration - RBAC permission levels.
 */
public enum StaffRole {
    SUPER_ADMIN,  // Full access
    ADMIN,        // Most operations
    MANAGER,      // Products, orders, customers
    EDITOR,       // Products and content
    VIEWER        // Read-only access
}
