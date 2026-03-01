package com.Batman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameId;

    // Snapshot of the price at the time the item was added to the cart
    @Column(nullable = false, precision = 15, scale = 2)
    private Double priceAtAddTime;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private boolean selectedForCheckout = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;
}