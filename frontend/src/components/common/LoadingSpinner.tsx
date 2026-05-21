import { Loader2 } from "lucide-react";

interface LoadingSpinnerProps {
  className?: string;
  text?: string;
}

export function LoadingSpinner({ className, text = "Loading..." }: LoadingSpinnerProps) {
  return (
    <div className={`flex flex-col items-center justify-center py-12 ${className || ""}`}>
      <div className="relative">
        <div className="absolute inset-0 h-10 w-10 rounded-full bg-primary/20 animate-ping opacity-20" />
        <Loader2 className="relative h-10 w-10 animate-spin text-primary" />
      </div>
      <p className="mt-3 text-sm text-muted-foreground">{text}</p>
    </div>
  );
}
