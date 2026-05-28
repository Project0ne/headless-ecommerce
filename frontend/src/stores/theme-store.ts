"use client";

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { ThemeMode } from "@/lib/themes";
import { applyTheme } from "@/lib/themes";

interface ThemeState {
  themeId: string;
  mode: ThemeMode;
  setTheme: (themeId: string) => void;
  setMode: (mode: ThemeMode) => void;
  initialize: () => void;
}

export const useThemeStore = create<ThemeState>()(
  persist(
    (set, get) => ({
      themeId: "default",
      mode: "system",

      setTheme: (themeId: string) => {
        set({ themeId });
        applyTheme(themeId, get().mode);
      },

      setMode: (mode: ThemeMode) => {
        set({ mode });
        applyTheme(get().themeId, mode);
      },

      initialize: () => {
        const { themeId, mode } = get();
        applyTheme(themeId, mode);

        // Listen for system theme changes
        if (mode === "system") {
          const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
          const handleChange = () => {
            applyTheme(get().themeId, "system");
          };
          mediaQuery.addEventListener("change", handleChange);
        }
      },
    }),
    {
      name: "theme-storage",
    }
  )
);
