package com.Batman.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;
	
	@Column(nullable = false)
	private Integer userId;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "cart_items",joinColumns = @JoinColumn(name="cart_id",referencedColumnName = "cartId"))
	private Set<String> cartItems;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "selected_cart_items",joinColumns = @JoinColumn(name="cart_id",referencedColumnName = "cartId"))
	private Set<String> selectedCartItems;
	
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private Double totalPrice;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
	
	
}
