"use client";

import { useAuthStore } from "@/stores/auth-store";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import Link from "next/link";

export function AdminHeader() {
  const { user } = useAuthStore();

  return (
    <header className="flex h-16 items-center justify-between border-b border-border/50 bg-background/80 backdrop-blur-sm px-6">
      <div className="text-sm text-muted-foreground">
        <Link href="/" className="hover:text-primary transition-colors duration-200">Home</Link>
        <span className="mx-2 text-border">/</span>
        <span className="font-medium text-foreground">Admin</span>
      </div>
      <div className="flex items-center gap-3">
        <span className="text-sm font-medium">{user?.nickname || user?.username || "Admin"}</span>
        <Avatar className="h-8 w-8 ring-2 ring-primary/20">
          <AvatarFallback className="bg-gradient-to-br from-primary/10 to-primary/5 text-primary text-xs font-semibold">{user?.nickname?.[0] || user?.username?.[0] || "A"}</AvatarFallback>
        </Avatar>
      </div>
    </header>
  );
}
