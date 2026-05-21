"use client";

interface ProductImageGalleryProps {
  imageUrl: string | null;
  name: string;
}

export function ProductImageGallery({ imageUrl, name }: ProductImageGalleryProps) {
  return (
    <div className="aspect-square overflow-hidden rounded-lg bg-muted">
      {imageUrl ? (
        <img src={imageUrl} alt={name} className="h-full w-full object-cover" />
      ) : (
        <div className="flex h-full items-center justify-center text-muted-foreground">
          No Image Available
        </div>
      )}
    </div>
  );
}
