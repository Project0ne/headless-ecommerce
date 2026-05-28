import api from "./api";

export interface StoreConfig {
  id?: number;
  storeName: string;
  storeLogo?: string;
  storeDescription?: string;
  contactEmail?: string;
  contactPhone?: string;
  contactAddress?: string;
  timezone: string;
  currencyCode: string;
  currencySymbol: string;
  language: string;
  customDomain?: string;
  faviconUrl?: string;
  footerText?: string;
}

export interface PaymentMethod {
  id?: number;
  name: string;
  type: string;
  iconUrl?: string;
  description?: string;
  isEnabled: boolean;
  sortOrder: number;
  minAmount?: number;
  maxAmount?: number;
  configJson?: string;
}

export interface ShippingMethod {
  id?: number;
  name: string;
  description?: string;
  iconUrl?: string;
  baseFee: number;
  freeShippingThreshold?: number;
  estimatedDaysMin?: number;
  estimatedDaysMax?: number;
  isEnabled: boolean;
  sortOrder: number;
  trackingUrlTemplate?: string;
}

export interface TaxRate {
  id?: number;
  name: string;
  countryCode: string;
  stateCode?: string;
  cityCode?: string;
  postalCode?: string;
  taxRate: number;
  isCompound: boolean;
  isEnabled: boolean;
  priority: number;
}

export interface Currency {
  id?: number;
  code: string;
  name: string;
  symbol: string;
  exchangeRate: number;
  isDefault: boolean;
  isEnabled: boolean;
  decimalPlaces: number;
  symbolPosition: string;
  thousandsSeparator: string;
  decimalSeparator: string;
}

export interface Policy {
  id?: number;
  title: string;
  policyType: string;
  content?: string;
  isEnabled: boolean;
  isRequired: boolean;
  sortOrder: number;
  version?: string;
  effectiveDate?: string;
}

export interface Staff {
  id?: number;
  username: string;
  displayName: string;
  password?: string;
  email?: string;
  phone?: string;
  role: string;
  avatarUrl?: string;
  isActive: boolean;
  lastLoginAt?: string;
  lastLoginIp?: string;
}

export interface AuditLog {
  id: number;
  operatorType: string;
  operatorId?: number;
  operatorName?: string;
  action: string;
  entityType?: string;
  entityId?: number;
  entityName?: string;
  details?: string;
  ipAddress?: string;
  createdAt: string;
}

export interface Media {
  id: number;
  originalName: string;
  storedName: string;
  filePath: string;
  fileUrl: string;
  fileType: string;
  mimeType: string;
  fileSize: number;
  width?: number;
  height?: number;
  altText?: string;
  folder: string;
  createdAt: string;
}

const settingsService = {
  // Store Config
  getStoreConfig: () => api.get<StoreConfig>("/admin/store/config"),
  updateStoreConfig: (data: StoreConfig) => api.put<StoreConfig>("/admin/store/config", data),

  // Payment Methods
  getPaymentMethods: () => api.get<PaymentMethod[]>("/admin/payment-methods"),
  createPaymentMethod: (data: PaymentMethod) => api.post<PaymentMethod>("/admin/payment-methods", data),
  updatePaymentMethod: (id: number, data: PaymentMethod) => api.put<PaymentMethod>(`/admin/payment-methods/${id}`, data),
  deletePaymentMethod: (id: number) => api.delete(`/admin/payment-methods/${id}`),

  // Shipping Methods
  getShippingMethods: () => api.get<ShippingMethod[]>("/admin/shipping-methods"),
  createShippingMethod: (data: ShippingMethod) => api.post<ShippingMethod>("/admin/shipping-methods", data),
  updateShippingMethod: (id: number, data: ShippingMethod) => api.put<ShippingMethod>(`/admin/shipping-methods/${id}`, data),
  deleteShippingMethod: (id: number) => api.delete(`/admin/shipping-methods/${id}`),

  // Tax Rates
  getTaxRates: () => api.get<TaxRate[]>("/admin/tax-rates"),
  createTaxRate: (data: TaxRate) => api.post<TaxRate>("/admin/tax-rates", data),
  updateTaxRate: (id: number, data: TaxRate) => api.put<TaxRate>(`/admin/tax-rates/${id}`, data),
  deleteTaxRate: (id: number) => api.delete(`/admin/tax-rates/${id}`),

  // Currencies
  getCurrencies: () => api.get<Currency[]>("/admin/currencies"),
  createCurrency: (data: Currency) => api.post<Currency>("/admin/currencies", data),
  updateCurrency: (id: number, data: Currency) => api.put<Currency>(`/admin/currencies/${id}`, data),
  deleteCurrency: (id: number) => api.delete(`/admin/currencies/${id}`),
  setDefaultCurrency: (id: number) => api.put(`/admin/currencies/${id}/set-default`),

  // Policies
  getPolicies: () => api.get<Policy[]>("/admin/policies"),
  getPolicy: (id: number) => api.get<Policy>(`/admin/policies/${id}`),
  createPolicy: (data: Policy) => api.post<Policy>("/admin/policies", data),
  updatePolicy: (id: number, data: Policy) => api.put<Policy>(`/admin/policies/${id}`, data),
  deletePolicy: (id: number) => api.delete(`/admin/policies/${id}`),

  // Staff
  getStaffList: () => api.get<Staff[]>("/admin/staff"),
  createStaff: (data: Staff) => api.post<Staff>("/admin/staff", data),
  updateStaff: (id: number, data: Staff) => api.put<Staff>(`/admin/staff/${id}`, data),
  deleteStaff: (id: number) => api.delete(`/admin/staff/${id}`),

  // Audit Logs
  getAuditLogs: (page = 0, size = 20) => api.get<{ content: AuditLog[]; totalPages: number }>(`/admin/audit-logs?page=${page}&size=${size}`),

  // Media
  getMediaList: (page = 0, size = 20, folder?: string) => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    if (folder) params.append("folder", folder);
    return api.get<{ content: Media[]; totalPages: number }>(`/admin/media?${params}`);
  },
  uploadMedia: (file: File, folder = "default") => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("folder", folder);
    return api.post<Media>("/admin/media/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  },
  deleteMedia: (id: number) => api.delete(`/admin/media/${id}`),
};

export default settingsService;
