import { useQuery } from "@tanstack/react-query";
import { getCategories } from "@/services/category-service";

/**
 * Hook for fetching the category tree.
 */
export function useCategories() {
  return useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
    select: (response) => response.data,
    staleTime: 5 * 60 * 1000, // Categories rarely change
  });
}
