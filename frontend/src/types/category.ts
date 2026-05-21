/** Category type definitions matching backend DTOs */

export interface Category {
  id: number;
  name: string;
  icon: string;
  sortOrder: number;
  parentId: number | null;
  children: Category[];
  createdAt: string;
}

export interface CategoryCreateRequest {
  name: string;
  icon?: string;
  sortOrder?: number;
  parentId?: number;
}
