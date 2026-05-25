import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

/**
 * Merges class names with Tailwind CSS conflict resolution.
 *
 * @param inputs the class values to merge
 * @returns the merged class string
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Formats a price value to a localized string.
 *
 * @param price the price number
 * @returns formatted price string
 */
export function formatPrice(price: number): string {
  return new Intl.NumberFormat("zh-CN", {
    style: "currency",
    currency: "CNY",
  }).format(price);
}

/**
 * Formats a date string to a localized format.
 *
 * @param dateStr the ISO date string
 * @returns formatted date string
 */
export function formatDate(dateStr: string | null | undefined): string {
  if (!dateStr) return "N/A";
  return new Intl.DateTimeFormat("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(dateStr));
}
