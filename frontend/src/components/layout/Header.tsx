"use client";

import Link from "next/link";
import { useState } from "react";
import { Search, Menu, LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useAuthStore } from "@/stores/auth-store";
import { CartIcon } from "@/components/cart/CartIcon";
import { ThemeToggle } from "@/components/common/ThemeToggle";

export function Header() {
  const { isAuthenticated, user, logout } = useAuthStore();
  const [searchQuery, setSearchQuery] = useState("");

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      window.location.href = `/products?keyword=${encodeURIComponent(searchQuery.trim())}`;
    }
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b border-border/50 bg-background/80 backdrop-blur-xl shadow-sm">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        {/* Logo */}
        <Link href="/" className="flex items-center gap-2">
          <span className="text-xl font-bold bg-gradient-to-r from-primary to-blue-600 bg-clip-text text-transparent">E-Shop</span>
        </Link>

        {/* Desktop Search */}
        <form onSubmit={handleSearch} className="hidden md:flex items-center gap-2 max-w-sm flex-1 mx-8">
          <Input placeholder="Search products..." value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} className="flex-1 bg-background/50 border-border/50 shadow-inner" />
          <Button type="submit" size="icon" variant="ghost"><Search className="h-4 w-4" /></Button>
        </form>

        {/* Desktop Actions */}
        <div className="hidden md:flex items-center gap-2">
          <Link href="/products"><Button variant="ghost">Products</Button></Link>
          <CartIcon />
          <ThemeToggle />
          {isAuthenticated && user ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" className="relative h-8 w-8 rounded-full">
                  <Avatar className="h-8 w-8"><AvatarFallback>{user.nickname?.[0] || user.username[0]}</AvatarFallback></Avatar>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuLabel>{user.nickname || user.username}</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem asChild><Link href="/user">Profile</Link></DropdownMenuItem>
                <DropdownMenuItem asChild><Link href="/orders">Orders</Link></DropdownMenuItem>
                {user.role === "ADMIN" && <DropdownMenuItem asChild><Link href="/admin">Admin</Link></DropdownMenuItem>}
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={logout}><LogOut className="mr-2 h-4 w-4" />Logout</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Link href="/auth/login"><Button variant="outline" size="sm">Sign In</Button></Link>
          )}
        </div>

        {/* Mobile Menu */}
        <div className="flex md:hidden items-center gap-2">
          <CartIcon />
          <ThemeToggle />
          <Sheet>
            <SheetTrigger asChild><Button variant="ghost" size="icon"><Menu className="h-5 w-5" /></Button></SheetTrigger>
            <SheetContent side="right">
              <nav className="flex flex-col gap-4 mt-8">
                <form onSubmit={handleSearch} className="flex items-center gap-2">
                  <Input placeholder="Search..." value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} />
                  <Button type="submit" size="icon"><Search className="h-4 w-4" /></Button>
                </form>
                <Link href="/products" className="text-lg font-medium">Products</Link>
                {isAuthenticated ? (
                  <>
                    <Link href="/user" className="text-lg font-medium">Profile</Link>
                    <Link href="/orders" className="text-lg font-medium">Orders</Link>
                    <Link href="/cart" className="text-lg font-medium">Cart</Link>
                    {user?.role === "ADMIN" && <Link href="/admin" className="text-lg font-medium">Admin</Link>}
                    <Button variant="outline" onClick={logout}>Logout</Button>
                  </>
                ) : (
                  <Link href="/auth/login"><Button className="w-full">Sign In</Button></Link>
                )}
              </nav>
            </SheetContent>
          </Sheet>
        </div>
      </div>
    </header>
  );
}
