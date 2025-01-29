package com.deep.electronic.store.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {

	@Id
	private String orderId;
	
	private String orderStatus;
	
	private String paymentStatus;
	
	private int orderAmount;
	
	
	@Column(length = 1000)
	private String billingAddress;
	
	private String billingPhone;
	
	private String billingName;
	
	private Date orderedDate;
	
	private Date deliveredDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@Builder.Default
	private List<OrderItems> orderItems = new ArrayList<>();
	
	
}
