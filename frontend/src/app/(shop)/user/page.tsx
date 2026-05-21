"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useCurrentUser, useUpdateUser } from "@/hooks/useAuth";
import { useAuthStore } from "@/stores/auth-store";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useToast } from "@/hooks/use-toast";
import Link from "next/link";

const profileSchema = z.object({
  nickname: z.string().min(1, "Nickname is required"),
  phone: z.string().optional(),
  address: z.string().optional(),
});

type ProfileFormValues = z.infer<typeof profileSchema>;

export default function UserPage() {
  const { isAuthenticated, user } = useAuthStore();
  const { data: userData, isLoading } = useCurrentUser();
  const updateUserMutation = useUpdateUser();
  const { toast } = useToast();

  const currentUser = userData?.data || user;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ProfileFormValues>({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      nickname: currentUser?.nickname || "",
      phone: currentUser?.phone || "",
      address: currentUser?.address || "",
    },
  });

  const onSubmit = async (data: ProfileFormValues) => {
    try {
      await updateUserMutation.mutateAsync(data);
      toast({
        title: "Profile updated",
        description: "Your profile has been updated successfully.",
      });
    } catch {
      toast({
        title: "Error",
        description: "Failed to update profile.",
        variant: "destructive",
      });
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold mb-4">Please sign in</h1>
        <Link href="/auth/login">
          <Button>Sign In</Button>
        </Link>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-20 flex justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 tracking-tight">My Profile</h1>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Profile Card */}
        <div>
          <Card className="border-border/50">
            <CardContent className="p-6 flex flex-col items-center text-center">
              <Avatar className="h-24 w-24 mb-4 ring-4 ring-primary/10">
                <AvatarFallback className="text-2xl bg-gradient-to-br from-primary/10 to-primary/5 text-primary font-semibold">
                  {currentUser?.nickname?.[0] || currentUser?.username?.[0] || "U"}
                </AvatarFallback>
              </Avatar>
              <h2 className="text-lg font-semibold">
                {currentUser?.nickname || currentUser?.username}
              </h2>
              <p className="text-sm text-muted-foreground">
                @{currentUser?.username}
              </p>
              <p className="text-xs text-muted-foreground mt-1">
                Member since {currentUser?.createdAt ? new Date(currentUser.createdAt).toLocaleDateString() : "N/A"}
              </p>
              <Separator className="my-4 bg-border/50" />
              <div className="w-full space-y-2">
                <Link href="/orders" className="block">
                  <Button variant="outline" className="w-full">
                    My Orders
                  </Button>
                </Link>
                <Link href="/cart" className="block">
                  <Button variant="outline" className="w-full">
                    My Cart
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Edit Profile */}
        <div className="lg:col-span-2">
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Edit Profile</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div>
                  <label className="text-sm font-medium">Username</label>
                  <Input
                    value={currentUser?.username || ""}
                    disabled
                    className="mt-1.5 bg-muted/50"
                  />
                </div>
                <div>
                  <label className="text-sm font-medium">Nickname</label>
                  <Input {...register("nickname")} placeholder="Enter your nickname" className="mt-1.5" />
                  {errors.nickname && (
                    <p className="text-sm text-destructive mt-1">
                      {errors.nickname.message}
                    </p>
                  )}
                </div>
                <div>
                  <label className="text-sm font-medium">Phone</label>
                  <Input {...register("phone")} placeholder="Enter your phone" className="mt-1.5" />
                </div>
                <div>
                  <label className="text-sm font-medium">Address</label>
                  <Input {...register("address")} placeholder="Enter your address" className="mt-1.5" />
                </div>
                <Button type="submit" disabled={updateUserMutation.isPending} className="shadow-button">
                  {updateUserMutation.isPending ? "Saving..." : "Save Changes"}
                </Button>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
