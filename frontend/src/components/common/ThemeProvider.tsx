"use client";

import { createContext, useContext, useEffect } from "react";
import { useThemeStore } from "@/stores/theme-store";
import type { ThemeMode } from "@/lib/themes";

interface ThemeContextType {
  themeId: string;
  mode: ThemeMode;
  setThemeId: (id: string) => void;
  setMode: (mode: ThemeMode) => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const { themeId, mode, setTheme, setMode, initialize } = useThemeStore();

  useEffect(() => {
    initialize();
  }, [initialize]);

  return (
    <ThemeContext.Provider
      value={{
        themeId,
        mode,
        setThemeId: setTheme,
        setMode,
      }}
    >
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error("useTheme must be used within a ThemeProvider");
  }
  return context;
}
