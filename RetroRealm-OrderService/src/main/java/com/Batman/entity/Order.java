package com.Batman.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.Batman.annotations.OrderId;
import com.Batman.enums.OrderStatus;
import com.Batman.enums.OrderType;
import com.Batman.enums.PaymentType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
@Table(name = "orders")
public class Order {

	@Id
	@OrderId
	private String orderId;

	@Column(nullable = false)
	private Integer userId;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "orderId"))
	private Set<Integer> orderItems;

	@Column(nullable = false)
	private Double orderPrice;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "order_payments", joinColumns = @JoinColumn(name = "payment_id", referencedColumnName = "orderId"))
	private List<Integer> paymentIds;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderType orderType;

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

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", userId=" + userId + ", orderItems=" + orderItems + ", orderPrice="
				+ orderPrice + ", paymentIds=" + paymentIds + ", orderStatus=" + orderStatus + ", paymentType="
				+ paymentType + ", orderType=" + orderType + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + "]";
	}
	
	

}
