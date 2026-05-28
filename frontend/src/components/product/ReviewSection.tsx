"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Star, User, Trash2 } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { useAuthStore } from "@/stores/auth-store";
import { useToast } from "@/hooks/use-toast";
import {
  createReview,
  getProductReviews,
  getReviewSummary,
  deleteReview,
} from "@/services/review-service";
import type { ReviewSummary } from "@/services/review-service";

interface ReviewSectionProps {
  productId: number;
}

export function ReviewSection({ productId }: ReviewSectionProps) {
  const { isAuthenticated } = useAuthStore();
  const { toast } = useToast();
  const queryClient = useQueryClient();
  const [rating, setRating] = useState(5);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [showForm, setShowForm] = useState(false);

  // Fetch review summary
  const { data: summary } = useQuery({
    queryKey: ["review-summary", productId],
    queryFn: async () => {
      const res = await getReviewSummary(productId);
      return res.data as ReviewSummary;
    },
  });

  // Fetch reviews
  const { data: reviewsData, isLoading } = useQuery({
    queryKey: ["reviews", productId],
    queryFn: async () => {
      const res = await getProductReviews(productId, 0, 20);
      return res.data;
    },
  });

  // Create review mutation
  const createMutation = useMutation({
    mutationFn: () =>
      createReview(productId, {
        rating,
        title: title || undefined,
        content: content || undefined,
      }),
    onSuccess: () => {
      toast({ title: "Review submitted successfully!" });
      setShowForm(false);
      setRating(5);
      setTitle("");
      setContent("");
      queryClient.invalidateQueries({ queryKey: ["reviews", productId] });
      queryClient.invalidateQueries({ queryKey: ["review-summary", productId] });
    },
    onError: () => {
      toast({
        title: "Failed to submit review",
        description: "You may have already reviewed this product.",
        variant: "destructive",
      });
    },
  });

  // Delete review mutation
  const deleteMutation = useMutation({
    mutationFn: (reviewId: number) => deleteReview(productId, reviewId),
    onSuccess: () => {
      toast({ title: "Review deleted" });
      queryClient.invalidateQueries({ queryKey: ["reviews", productId] });
      queryClient.invalidateQueries({ queryKey: ["review-summary", productId] });
    },
  });

  const reviews = reviewsData?.content || [];

  return (
    <div className="space-y-6">
      {/* Summary */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <span>Customer Reviews</span>
            {isAuthenticated && !showForm && (
              <Button size="sm" onClick={() => setShowForm(true)}>
                Write a Review
              </Button>
            )}
          </CardTitle>
        </CardHeader>
        <CardContent>
          {summary && summary.totalReviews > 0 ? (
            <div className="flex items-center gap-4">
              <div className="text-center">
                <div className="text-4xl font-bold">{summary.averageRating}</div>
                <div className="flex items-center gap-0.5 mt-1">
                  {[1, 2, 3, 4, 5].map((i) => (
                    <Star
                      key={i}
                      className={`h-4 w-4 ${
                        i <= Math.round(summary.averageRating)
                          ? "fill-yellow-400 text-yellow-400"
                          : "text-muted-foreground"
                      }`}
                    />
                  ))}
                </div>
                <p className="text-sm text-muted-foreground mt-1">
                  {summary.totalReviews} reviews
                </p>
              </div>
            </div>
          ) : (
            <p className="text-muted-foreground">No reviews yet. Be the first to review!</p>
          )}
        </CardContent>
      </Card>

      {/* Review Form */}
      {showForm && (
        <Card>
          <CardHeader>
            <CardTitle>Write Your Review</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* Star Rating */}
            <div>
              <label className="text-sm font-medium">Rating</label>
              <div className="flex items-center gap-1 mt-1">
                {[1, 2, 3, 4, 5].map((i) => (
                  <button
                    key={i}
                    type="button"
                    onClick={() => setRating(i)}
                    className="p-0.5"
                  >
                    <Star
                      className={`h-6 w-6 transition-colors ${
                        i <= rating
                          ? "fill-yellow-400 text-yellow-400"
                          : "text-muted-foreground hover:text-yellow-300"
                      }`}
                    />
                  </button>
                ))}
              </div>
            </div>

            {/* Title */}
            <div>
              <label className="text-sm font-medium">Title (optional)</label>
              <Input
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Summarize your review"
                className="mt-1"
              />
            </div>

            {/* Content */}
            <div>
              <label className="text-sm font-medium">Review (optional)</label>
              <Textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Share your experience with this product"
                rows={4}
                className="mt-1"
              />
            </div>

            <div className="flex gap-2">
              <Button
                onClick={() => createMutation.mutate()}
                disabled={createMutation.isPending}
              >
                {createMutation.isPending ? "Submitting..." : "Submit Review"}
              </Button>
              <Button variant="outline" onClick={() => setShowForm(false)}>
                Cancel
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Reviews List */}
      {isLoading ? (
        <LoadingSpinner />
      ) : reviews.length > 0 ? (
        <div className="space-y-4">
          {reviews.map((review) => (
            <Card key={review.id}>
              <CardContent className="pt-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-10 w-10 rounded-full bg-muted flex items-center justify-center">
                      <User className="h-5 w-5 text-muted-foreground" />
                    </div>
                    <div>
                      <p className="font-medium">{review.username}</p>
                      <div className="flex items-center gap-1">
                        {[1, 2, 3, 4, 5].map((i) => (
                          <Star
                            key={i}
                            className={`h-3 w-3 ${
                              i <= review.rating
                                ? "fill-yellow-400 text-yellow-400"
                                : "text-muted-foreground"
                            }`}
                          />
                        ))}
                        <span className="text-xs text-muted-foreground ml-2">
                          {new Date(review.createdAt).toLocaleDateString()}
                        </span>
                      </div>
                    </div>
                  </div>
                  <Button
                    variant="ghost"
                    size="icon"
                    className="h-8 w-8 text-muted-foreground hover:text-destructive"
                    onClick={() => deleteMutation.mutate(review.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
                {review.title && (
                  <h4 className="font-semibold mt-3">{review.title}</h4>
                )}
                {review.content && (
                  <p className="text-sm text-muted-foreground mt-2">
                    {review.content}
                  </p>
                )}
              </CardContent>
            </Card>
          ))}
        </div>
      ) : null}
    </div>
  );
}
