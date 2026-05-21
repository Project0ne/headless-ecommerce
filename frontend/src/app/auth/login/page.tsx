"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useLogin } from "@/hooks/useAuth";
import { LoginForm } from "@/components/auth/LoginForm";
import type { LoginRequest } from "@/types/user";

/**
 * Login page component.
 */
export default function LoginPage() {
  const router = useRouter();
  const loginMutation = useLogin();
  const [error, setError] = useState<string>("");

  const handleSubmit = async (data: LoginRequest) => {
    try {
      setError("");
      const response = await loginMutation.mutateAsync(data);
      if (response.code === 200) {
        router.push("/");
      } else {
        setError(response.message || "Login failed");
      }
    } catch {
      setError("Login failed. Please check your credentials.");
    }
  };

  return (
    <div className="flex min-h-screen">
      {/* Left decorative panel - desktop only */}
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-primary via-blue-600 to-purple-700 items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(at_30%_20%,rgba(255,255,255,0.1),transparent_50%)]" />
        <div className="absolute inset-0 bg-[radial-gradient(at_70%_80%,rgba(255,255,255,0.05),transparent_50%)]" />
        <div className="relative z-10 text-white text-center px-12">
          <h1 className="text-4xl font-bold mb-4">Welcome to E-Shop</h1>
          <p className="text-lg text-white/80">Discover amazing products at the best prices</p>
        </div>
      </div>

      {/* Right form panel */}
      <div className="flex-1 flex items-center justify-center px-4 bg-gradient-to-br from-background to-muted/30">
        <div className="w-full max-w-md space-y-6 animate-fade-in">
          <div className="text-center">
            <h1 className="text-3xl font-bold">Sign In</h1>
            <p className="mt-2 text-muted-foreground">
              Welcome back! Please sign in to your account.
            </p>
          </div>
          {error && (
            <div className="rounded-md bg-destructive/15 p-3 text-sm text-destructive">
              {error}
            </div>
          )}
          <LoginForm
            onSubmit={handleSubmit}
            isLoading={loginMutation.isPending}
          />
          <p className="text-center text-sm text-muted-foreground">
            Don&apos;t have an account?{" "}
            <a href="/auth/register" className="text-primary hover:underline">
              Sign up
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
