"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";
import type { ProductImage } from "@/types/product";

interface ImageGalleryProps {
  images: ProductImage[];
  fallbackImageUrl?: string;
  productName: string;
}

export function ImageGallery({ images, fallbackImageUrl, productName }: ImageGalleryProps) {
  const [selectedIndex, setSelectedIndex] = useState(0);

  // Build image list: use images array if available, fallback to single imageUrl
  const imageList =
    images && images.length > 0
      ? images
      : fallbackImageUrl
        ? [{ id: 0, imageUrl: fallbackImageUrl, sortOrder: 0, isPrimary: true }]
        : [];

  if (imageList.length === 0) {
    return (
      <div className="flex h-96 items-center justify-center rounded-xl bg-muted text-muted-foreground">
        No Image
      </div>
    );
  }

  const selectedImage = imageList[selectedIndex];

  return (
    <div className="flex flex-col gap-4">
      {/* Main Image */}
      <div className="relative aspect-square overflow-hidden rounded-xl bg-muted">
        <img
          src={selectedImage.imageUrl}
          alt={`${productName} - Image ${selectedIndex + 1}`}
          className="h-full w-full object-cover transition-opacity duration-300"
        />
        {imageList.length > 1 && (
          <div className="absolute bottom-3 right-3 rounded-full bg-black/60 px-3 py-1 text-xs text-white backdrop-blur-sm">
            {selectedIndex + 1} / {imageList.length}
          </div>
        )}
      </div>

      {/* Thumbnails */}
      {imageList.length > 1 && (
        <div className="flex gap-2 overflow-x-auto pb-1">
          {imageList.map((image, index) => (
            <button
              key={image.id}
              onClick={() => setSelectedIndex(index)}
              className={cn(
                "relative h-20 w-20 flex-shrink-0 overflow-hidden rounded-lg border-2 transition-all duration-200",
                selectedIndex === index
                  ? "border-primary ring-2 ring-primary/20 scale-105"
                  : "border-transparent opacity-60 hover:opacity-100"
              )}
            >
              <img
                src={image.imageUrl}
                alt={`${productName} - Thumbnail ${index + 1}`}
                className="h-full w-full object-cover"
              />
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
