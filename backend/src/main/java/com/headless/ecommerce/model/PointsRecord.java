package com.headless.ecommerce.model;

import com.headless.ecommerce.model.enums.PointsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Points record entity for tracking user points history.
 */
@Entity
@Table(name = "t_points_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointsType type;

    @Column(length = 200)
    private String description;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
