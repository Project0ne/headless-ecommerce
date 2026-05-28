"use client";

import { useState } from "react";
import { Tag, CheckCircle, XCircle } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { validateCoupon } from "@/services/coupon-service";
import { formatPrice } from "@/lib/utils";
import type { Coupon } from "@/services/coupon-service";

interface CouponInputProps {
  orderAmount: number;
  onCouponApplied: (discount: number, couponCode: string | null) => void;
}

export function CouponInput({ orderAmount, onCouponApplied }: CouponInputProps) {
  const [code, setCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [appliedCoupon, setAppliedCoupon] = useState<Coupon | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleApply = async () => {
    if (!code.trim()) return;

    setLoading(true);
    setError(null);

    try {
      const res = await validateCoupon(code, orderAmount);
      if (res.data.valid) {
        setAppliedCoupon(res.data);
        onCouponApplied(res.data.discountAmount, res.data.code);
      } else {
        setError(res.data.message);
        setAppliedCoupon(null);
        onCouponApplied(0, null);
      }
    } catch {
      setError("Invalid coupon code");
      setAppliedCoupon(null);
      onCouponApplied(0, null);
    } finally {
      setLoading(false);
    }
  };

  const handleRemove = () => {
    setAppliedCoupon(null);
    setCode("");
    setError(null);
    onCouponApplied(0, null);
  };

  if (appliedCoupon) {
    return (
      <div className="flex items-center justify-between rounded-lg border border-green-200 bg-green-50 p-3 dark:border-green-900 dark:bg-green-950">
        <div className="flex items-center gap-2">
          <CheckCircle className="h-4 w-4 text-green-600" />
          <div>
            <p className="text-sm font-medium text-green-800 dark:text-green-200">
              {appliedCoupon.code} applied
            </p>
            <p className="text-xs text-green-600">
              -{formatPrice(appliedCoupon.discountAmount)}
            </p>
          </div>
        </div>
        <Button variant="ghost" size="sm" onClick={handleRemove}>
          Remove
        </Button>
      </div>
    );
  }

  return (
    <div className="space-y-2">
      <div className="flex items-center gap-2">
        <div className="relative flex-1">
          <Tag className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={code}
            onChange={(e) => setCode(e.target.value.toUpperCase())}
            placeholder="Enter coupon code"
            className="pl-9"
          />
        </div>
        <Button onClick={handleApply} disabled={loading || !code.trim()}>
          {loading ? "..." : "Apply"}
        </Button>
      </div>
      {error && (
        <div className="flex items-center gap-1 text-sm text-destructive">
          <XCircle className="h-3 w-3" />
          {error}
        </div>
      )}
    </div>
  );
}
