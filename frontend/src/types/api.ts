/** Generic API response type matching backend ApiResponse<T> */
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

/** Paginated response matching backend PageResponse<T> */
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

/** Pagination request parameters */
export interface PageParams {
  page?: number;
  size?: number;
  sort?: string;
}
