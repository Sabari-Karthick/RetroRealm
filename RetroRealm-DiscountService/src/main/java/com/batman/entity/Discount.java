package com.batman.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.batman.enums.DiscountStatus;
import com.batman.enums.DiscountType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class Discount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer discountId;

	@Enumerated(EnumType.STRING)
	private DiscountType discountType;
	
	@Enumerated(EnumType.STRING)
	private DiscountStatus discountStatus;
	
	@ElementCollection(targetClass = Integer.class,fetch = FetchType.EAGER)
	@CollectionTable(name = "game_ids",joinColumns = @JoinColumn(name="discount_id",referencedColumnName = "discountId"))
	private Set<Integer> gameIds;

	private LocalDate fromDate;

	private LocalTime fromTime;

	private LocalDate toDate;

	private LocalTime toTime;

	private Double discountValue;

	private Boolean isExpired;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@Column(nullable = false)
	private LocalDateTime updatedDate;

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
