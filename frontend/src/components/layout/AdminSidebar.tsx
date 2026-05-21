"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Package, FolderTree, ClipboardList } from "lucide-react";
import { cn } from "@/lib/utils";

const sidebarItems = [
  { href: "/admin", label: "Dashboard", icon: LayoutDashboard },
  { href: "/admin/products", label: "Products", icon: Package },
  { href: "/admin/categories", label: "Categories", icon: FolderTree },
  { href: "/admin/orders", label: "Orders", icon: ClipboardList },
];

export function AdminSidebar() {
  const pathname = usePathname();

  return (
    <aside className="hidden md:flex md:w-64 md:flex-col md:border-r border-border/50 bg-gradient-to-b from-background to-muted/20">
      <div className="flex h-16 items-center border-b border-border/50 px-6">
        <Link href="/admin" className="flex items-center gap-2 font-bold text-lg bg-gradient-to-r from-primary to-purple-500 bg-clip-text text-transparent">
          E-Shop Admin
        </Link>
      </div>
      <nav className="flex-1 space-y-1 px-3 py-4">
        {sidebarItems.map((item) => {
          const isActive = pathname === item.href || (item.href !== "/admin" && pathname.startsWith(item.href));
          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-200 border-l-2",
                isActive
                  ? "bg-gradient-to-r from-primary/10 to-primary/5 text-primary font-semibold border-l-primary"
                  : "text-muted-foreground hover:bg-muted/50 hover:text-foreground border-l-transparent hover:border-l-primary/20"
              )}
            >
              <item.icon className="h-4 w-4" />
              {item.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
