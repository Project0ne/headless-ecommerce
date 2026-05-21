import { PackageOpen } from "lucide-react";
import Link from "next/link";
import { Button } from "@/components/ui/button";

interface EmptyStateProps {
  title: string;
  description?: string;
  actionLabel?: string;
  actionHref?: string;
}

export function EmptyState({ title, description, actionLabel, actionHref }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">
      <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-primary/10 to-primary/5 mb-5 ring-1 ring-primary/10">
        <PackageOpen className="h-10 w-10 text-primary" />
      </div>
      <h3 className="text-lg font-semibold">{title}</h3>
      {description && <p className="mt-2 text-sm text-muted-foreground max-w-md leading-relaxed">{description}</p>}
      {actionLabel && actionHref && (
        <Link href={actionHref} className="mt-6">
          <Button className="shadow-button">{actionLabel}</Button>
        </Link>
      )}
    </div>
  );
}
