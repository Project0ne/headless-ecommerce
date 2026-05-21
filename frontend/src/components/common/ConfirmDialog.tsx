"use client";

import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogTitle } from "@radix-ui/react-alert-dialog";
import { Button } from "@/components/ui/button";

interface ConfirmDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  description: string;
  onConfirm: () => void;
  confirmLabel?: string;
  cancelLabel?: string;
}

export function ConfirmDialog({ open, onOpenChange, title, description, onConfirm, confirmLabel = "Confirm", cancelLabel = "Cancel" }: ConfirmDialogProps) {
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent className="fixed left-[50%] top-[50%] z-50 grid w-full max-w-lg translate-x-[-50%] translate-y-[-50%] gap-4 border bg-background p-6 shadow-lg rounded-lg">
        <div className="flex flex-col space-y-2 text-center sm:text-left">
          <AlertDialogTitle className="text-lg font-semibold">{title}</AlertDialogTitle>
          <AlertDialogDescription className="text-sm text-muted-foreground">{description}</AlertDialogDescription>
        </div>
        <div className="flex flex-col-reverse sm:flex-row sm:justify-end gap-2">
          <AlertDialogCancel asChild><Button variant="outline" onClick={() => onOpenChange(false)}>{cancelLabel}</Button></AlertDialogCancel>
          <AlertDialogAction asChild><Button onClick={onConfirm}>{confirmLabel}</Button></AlertDialogAction>
        </div>
      </AlertDialogContent>
    </AlertDialog>
  );
}
