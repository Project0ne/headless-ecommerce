"use client";

import { useState } from "react";
import { Check, Palette, Sun, Moon, Monitor } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { cn } from "@/lib/utils";
import { themes } from "@/lib/themes";
import type { ThemeMode } from "@/lib/themes";
import { useThemeStore } from "@/stores/theme-store";

const modeOptions = [
  { value: "light" as ThemeMode, label: "Light", icon: Sun },
  { value: "dark" as ThemeMode, label: "Dark", icon: Moon },
  { value: "system" as ThemeMode, label: "System", icon: Monitor },
];

export function ThemeSwitcher() {
  const { themeId, mode, setTheme, setMode } = useThemeStore();
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon" className="h-9 w-9">
          <Palette className="h-4 w-4" />
          <span className="sr-only">Switch theme</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Choose Theme</DialogTitle>
          <DialogDescription>
            Customize your shopping experience with different themes and modes.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* Mode Selection */}
          <div>
            <h4 className="text-sm font-medium mb-3">Appearance</h4>
            <div className="grid grid-cols-3 gap-2">
              {modeOptions.map((option) => (
                <Button
                  key={option.value}
                  variant={mode === option.value ? "default" : "outline"}
                  size="sm"
                  onClick={() => setMode(option.value)}
                  className="flex items-center gap-2"
                >
                  <option.icon className="h-4 w-4" />
                  {option.label}
                </Button>
              ))}
            </div>
          </div>

          {/* Theme Selection */}
          <div>
            <h4 className="text-sm font-medium mb-3">Color Theme</h4>
            <div className="grid grid-cols-2 gap-3">
              {themes.map((theme) => (
                <button
                  key={theme.id}
                  onClick={() => setTheme(theme.id)}
                  className={cn(
                    "relative flex flex-col items-center gap-2 rounded-lg border-2 p-3 transition-all hover:scale-[1.02]",
                    themeId === theme.id
                      ? "border-primary shadow-md"
                      : "border-border hover:border-primary/50"
                  )}
                >
                  {/* Preview */}
                  <div
                    className="h-16 w-full rounded-md shadow-sm"
                    style={{ background: theme.preview }}
                  />

                  {/* Info */}
                  <div className="text-center">
                    <p className="text-sm font-medium">{theme.nameCn}</p>
                    <p className="text-xs text-muted-foreground">
                      {theme.description}
                    </p>
                  </div>

                  {/* Selected indicator */}
                  {themeId === theme.id && (
                    <div className="absolute -top-1 -right-1 h-5 w-5 rounded-full bg-primary flex items-center justify-center">
                      <Check className="h-3 w-3 text-primary-foreground" />
                    </div>
                  )}
                </button>
              ))}
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
