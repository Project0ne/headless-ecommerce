/** User type definitions matching backend DTOs */

export interface User {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  phone: string;
  address: string;
  role: "ADMIN" | "BUYER";
  createdAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  nickname?: string;
}

export interface JwtResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string;
  role: string;
}

export interface UserUpdateRequest {
  nickname?: string;
  avatar?: string;
  phone?: string;
  address?: string;
}
