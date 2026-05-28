/**
 * Theme configuration system
 * Supports multiple color themes with dark/light mode variants
 */

export type ThemeMode = "light" | "dark" | "system";

export interface ThemeColors {
  background: string;
  foreground: string;
  card: string;
  cardForeground: string;
  primary: string;
  primaryForeground: string;
  secondary: string;
  secondaryForeground: string;
  muted: string;
  mutedForeground: string;
  accent: string;
  accentForeground: string;
  destructive: string;
  destructiveForeground: string;
  border: string;
  input: string;
  ring: string;
}

export interface ThemeConfig {
  id: string;
  name: string;
  nameCn: string;
  description: string;
  preview: string; // Gradient preview
  light: ThemeColors;
  dark: ThemeColors;
}

// Default theme - Classic blue
export const defaultTheme: ThemeConfig = {
  id: "default",
  name: "Classic",
  nameCn: "经典蓝",
  description: "Professional and clean design",
  preview: "linear-gradient(135deg, #1e293b, #3b82f6)",
  light: {
    background: "0 0% 100%",
    foreground: "222.2 84% 4.9%",
    card: "0 0% 100%",
    cardForeground: "222.2 84% 4.9%",
    primary: "222.2 47.4% 11.2%",
    primaryForeground: "210 40% 98%",
    secondary: "210 40% 96.1%",
    secondaryForeground: "222.2 47.4% 11.2%",
    muted: "210 40% 96.1%",
    mutedForeground: "215.4 16.3% 46.9%",
    accent: "210 40% 96.1%",
    accentForeground: "222.2 47.4% 11.2%",
    destructive: "0 84.2% 60.2%",
    destructiveForeground: "210 40% 98%",
    border: "214.3 31.8% 91.4%",
    input: "214.3 31.8% 91.4%",
    ring: "222.2 84% 4.9%",
  },
  dark: {
    background: "222.2 84% 4.9%",
    foreground: "210 40% 98%",
    card: "222.2 84% 6.5%",
    cardForeground: "210 40% 98%",
    primary: "210 40% 98%",
    primaryForeground: "222.2 47.4% 11.2%",
    secondary: "217.2 32.6% 17.5%",
    secondaryForeground: "210 40% 98%",
    muted: "217.2 32.6% 17.5%",
    mutedForeground: "215 20.2% 65.1%",
    accent: "217.2 32.6% 17.5%",
    accentForeground: "210 40% 98%",
    destructive: "0 62.8% 30.6%",
    destructiveForeground: "210 40% 98%",
    border: "217.2 32.6% 17.5%",
    input: "217.2 32.6% 17.5%",
    ring: "212.7 26.8% 83.9%",
  },
};

// Minimal theme - Clean and simple
export const minimalTheme: ThemeConfig = {
  id: "minimal",
  name: "Minimal",
  nameCn: "极简白",
  description: "Clean and distraction-free",
  preview: "linear-gradient(135deg, #f8fafc, #e2e8f0)",
  light: {
    background: "0 0% 98%",
    foreground: "0 0% 9%",
    card: "0 0% 100%",
    cardForeground: "0 0% 9%",
    primary: "0 0% 9%",
    primaryForeground: "0 0% 98%",
    secondary: "0 0% 96%",
    secondaryForeground: "0 0% 9%",
    muted: "0 0% 96%",
    mutedForeground: "0 0% 45%",
    accent: "0 0% 96%",
    accentForeground: "0 0% 9%",
    destructive: "0 84% 60%",
    destructiveForeground: "0 0% 98%",
    border: "0 0% 90%",
    input: "0 0% 90%",
    ring: "0 0% 9%",
  },
  dark: {
    background: "0 0% 7%",
    foreground: "0 0% 95%",
    card: "0 0% 10%",
    cardForeground: "0 0% 95%",
    primary: "0 0% 95%",
    primaryForeground: "0 0% 7%",
    secondary: "0 0% 15%",
    secondaryForeground: "0 0% 95%",
    muted: "0 0% 15%",
    mutedForeground: "0 0% 60%",
    accent: "0 0% 15%",
    accentForeground: "0 0% 95%",
    destructive: "0 63% 31%",
    destructiveForeground: "0 0% 95%",
    border: "0 0% 15%",
    input: "0 0% 15%",
    ring: "0 0% 83%",
  },
};

// Neon theme - Vibrant and modern
export const neonTheme: ThemeConfig = {
  id: "neon",
  name: "Neon",
  nameCn: "霓虹紫",
  description: "Vibrant and eye-catching",
  preview: "linear-gradient(135deg, #7c3aed, #ec4899)",
  light: {
    background: "270 100% 99%",
    foreground: "270 50% 10%",
    card: "270 100% 100%",
    cardForeground: "270 50% 10%",
    primary: "270 91% 60%",
    primaryForeground: "0 0% 100%",
    secondary: "270 100% 96%",
    secondaryForeground: "270 50% 10%",
    muted: "270 100% 96%",
    mutedForeground: "270 20% 50%",
    accent: "270 100% 96%",
    accentForeground: "270 50% 10%",
    destructive: "0 84% 60%",
    destructiveForeground: "0 0% 100%",
    border: "270 30% 90%",
    input: "270 30% 90%",
    ring: "270 91% 60%",
  },
  dark: {
    background: "270 50% 5%",
    foreground: "270 20% 95%",
    card: "270 50% 8%",
    cardForeground: "270 20% 95%",
    primary: "270 91% 65%",
    primaryForeground: "0 0% 100%",
    secondary: "270 30% 15%",
    secondaryForeground: "270 20% 95%",
    muted: "270 30% 15%",
    mutedForeground: "270 20% 60%",
    accent: "270 30% 15%",
    accentForeground: "270 20% 95%",
    destructive: "0 63% 31%",
    destructiveForeground: "0 0% 95%",
    border: "270 30% 15%",
    input: "270 30% 15%",
    ring: "270 91% 65%",
  },
};

// Warm theme - Cozy and inviting
export const warmTheme: ThemeConfig = {
  id: "warm",
  name: "Warm",
  nameCn: "暖阳橙",
  description: "Cozy and inviting atmosphere",
  preview: "linear-gradient(135deg, #ea580c, #f59e0b)",
  light: {
    background: "30 100% 98%",
    foreground: "20 50% 10%",
    card: "30 100% 100%",
    cardForeground: "20 50% 10%",
    primary: "20 91% 55%",
    primaryForeground: "0 0% 100%",
    secondary: "30 100% 96%",
    secondaryForeground: "20 50% 10%",
    muted: "30 100% 96%",
    mutedForeground: "20 20% 50%",
    accent: "30 100% 96%",
    accentForeground: "20 50% 10%",
    destructive: "0 84% 60%",
    destructiveForeground: "0 0% 100%",
    border: "30 30% 90%",
    input: "30 30% 90%",
    ring: "20 91% 55%",
  },
  dark: {
    background: "20 50% 5%",
    foreground: "30 20% 95%",
    card: "20 50% 8%",
    cardForeground: "30 20% 95%",
    primary: "20 91% 60%",
    primaryForeground: "0 0% 100%",
    secondary: "20 30% 15%",
    secondaryForeground: "30 20% 95%",
    muted: "20 30% 15%",
    mutedForeground: "30 20% 60%",
    accent: "20 30% 15%",
    accentForeground: "30 20% 95%",
    destructive: "0 63% 31%",
    destructiveForeground: "0 0% 95%",
    border: "20 30% 15%",
    input: "20 30% 15%",
    ring: "20 91% 60%",
  },
};

// Forest theme - Natural and calm
export const forestTheme: ThemeConfig = {
  id: "forest",
  name: "Forest",
  nameCn: "森林绿",
  description: "Natural and calming vibes",
  preview: "linear-gradient(135deg, #166534, #22c55e)",
  light: {
    background: "140 100% 98%",
    foreground: "140 50% 10%",
    card: "140 100% 100%",
    cardForeground: "140 50% 10%",
    primary: "142 71% 45%",
    primaryForeground: "0 0% 100%",
    secondary: "140 100% 96%",
    secondaryForeground: "140 50% 10%",
    muted: "140 100% 96%",
    mutedForeground: "140 20% 50%",
    accent: "140 100% 96%",
    accentForeground: "140 50% 10%",
    destructive: "0 84% 60%",
    destructiveForeground: "0 0% 100%",
    border: "140 30% 90%",
    input: "140 30% 90%",
    ring: "142 71% 45%",
  },
  dark: {
    background: "140 50% 5%",
    foreground: "140 20% 95%",
    card: "140 50% 8%",
    cardForeground: "140 20% 95%",
    primary: "142 71% 50%",
    primaryForeground: "0 0% 100%",
    secondary: "140 30% 15%",
    secondaryForeground: "140 20% 95%",
    muted: "140 30% 15%",
    mutedForeground: "140 20% 60%",
    accent: "140 30% 15%",
    accentForeground: "140 20% 95%",
    destructive: "0 63% 31%",
    destructiveForeground: "0 0% 95%",
    border: "140 30% 15%",
    input: "140 30% 15%",
    ring: "142 71% 50%",
  },
};

// All available themes
export const themes: ThemeConfig[] = [
  defaultTheme,
  minimalTheme,
  neonTheme,
  warmTheme,
  forestTheme,
];

// Get theme by ID
export function getThemeById(id: string): ThemeConfig {
  return themes.find((t) => t.id === id) || defaultTheme;
}

// Apply theme to document
export function applyTheme(themeId: string, mode: ThemeMode) {
  const theme = getThemeById(themeId);
  const root = document.documentElement;

  // Determine actual mode
  let actualMode: "light" | "dark" = "light";
  if (mode === "system") {
    actualMode = window.matchMedia("(prefers-color-scheme: dark)").matches
      ? "dark"
      : "light";
  } else {
    actualMode = mode;
  }

  // Apply mode class
  root.classList.remove("light", "dark");
  root.classList.add(actualMode);

  // Get colors for the mode
  const colors = actualMode === "dark" ? theme.dark : theme.light;

  // Apply CSS variables
  root.style.setProperty("--color-background", colors.background);
  root.style.setProperty("--color-foreground", colors.foreground);
  root.style.setProperty("--color-card", colors.card);
  root.style.setProperty("--color-card-foreground", colors.cardForeground);
  root.style.setProperty("--color-primary", colors.primary);
  root.style.setProperty("--color-primary-foreground", colors.primaryForeground);
  root.style.setProperty("--color-secondary", colors.secondary);
  root.style.setProperty("--color-secondary-foreground", colors.secondaryForeground);
  root.style.setProperty("--color-muted", colors.muted);
  root.style.setProperty("--color-muted-foreground", colors.mutedForeground);
  root.style.setProperty("--color-accent", colors.accent);
  root.style.setProperty("--color-accent-foreground", colors.accentForeground);
  root.style.setProperty("--color-destructive", colors.destructive);
  root.style.setProperty("--color-destructive-foreground", colors.destructiveForeground);
  root.style.setProperty("--color-border", colors.border);
  root.style.setProperty("--color-input", colors.input);
  root.style.setProperty("--color-ring", colors.ring);
}
