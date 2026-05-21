import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { User, JwtResponse } from "@/types/user";

/** Auth state interface */
interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
  setAuth: (response: JwtResponse) => void;
  setUser: (user: User) => void;
  logout: () => void;
}

/**
 * Zustand store for authentication state with localStorage persistence.
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,
      isAuthenticated: false,

      setAuth: (response: JwtResponse) => {
        const user: User = {
          id: response.userId,
          username: response.username,
          nickname: response.username,
          avatar: "",
          phone: "",
          address: "",
          role: response.role as "ADMIN" | "BUYER",
          createdAt: new Date().toISOString(),
        };

        if (typeof window !== "undefined") {
          localStorage.setItem("token", response.token);
        }

        set({
          token: response.token,
          user,
          isAuthenticated: true,
        });
      },

      setUser: (user: User) => {
        set({ user });
      },

      logout: () => {
        if (typeof window !== "undefined") {
          localStorage.removeItem("token");
        }
        set({
          token: null,
          user: null,
          isAuthenticated: false,
        });
      },
    }),
    {
      name: "auth-storage",
      partialize: (state) => ({
        token: state.token,
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);
