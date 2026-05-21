"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useRegister } from "@/hooks/useAuth";
import { RegisterForm } from "@/components/auth/RegisterForm";
import { Card, CardContent } from "@/components/ui/card";
import type { RegisterRequest } from "@/types/user";

/**
 * Register page component.
 */
export default function RegisterPage() {
  const router = useRouter();
  const registerMutation = useRegister();
  const [error, setError] = useState<string>("");

  const handleSubmit = async (data: RegisterRequest) => {
    try {
      setError("");
      const response = await registerMutation.mutateAsync(data);
      if (response.code === 200) {
        router.push("/");
      } else {
        setError(response.message || "Registration failed");
      }
    } catch {
      setError("Registration failed. Please try again.");
    }
  };

  return (
    <div className="flex min-h-screen">
      {/* Left decorative panel - desktop only */}
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-emerald-600 via-teal-600 to-cyan-700 items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(at_60%_30%,rgba(255,255,255,0.1),transparent_50%)]" />
        <div className="absolute inset-0 bg-[radial-gradient(at_20%_70%,rgba(255,255,255,0.05),transparent_50%)]" />
        <div className="relative z-10 text-white text-center px-12">
          <h1 className="text-4xl font-bold mb-4">Join E-Shop</h1>
          <p className="text-lg text-white/80">Create your account and start shopping today</p>
        </div>
      </div>

      {/* Right form panel */}
      <div className="flex-1 flex items-center justify-center px-4 bg-gradient-to-br from-background to-muted/30">
        <div className="w-full max-w-md space-y-6 animate-fade-in">
          <div className="text-center">
            <h1 className="text-3xl font-bold">Create Account</h1>
            <p className="mt-2 text-muted-foreground">
              Sign up to start shopping.
            </p>
          </div>
          {error && (
            <div className="rounded-xl bg-destructive/10 p-3 text-sm text-destructive ring-1 ring-destructive/20">
              {error}
            </div>
          )}
          <Card className="rounded-xl shadow-lifted border-border/50">
            <CardContent className="p-6">
              <RegisterForm
                onSubmit={handleSubmit}
                isLoading={registerMutation.isPending}
              />
            </CardContent>
          </Card>
          <p className="text-center text-sm text-muted-foreground">
            Already have an account?{" "}
            <a href="/auth/login" className="font-medium text-primary hover:underline underline-offset-4">
              Sign in
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
