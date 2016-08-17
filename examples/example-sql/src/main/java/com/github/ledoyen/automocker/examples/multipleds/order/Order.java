package com.github.ledoyen.automocker.examples.multipleds.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.github.ledoyen.automocker.examples.multipleds.customer.Customer.CustomerId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple domain class representing an {@link Order}
 * 
 * @author Oliver Gierke
 */
@Entity
@EqualsAndHashCode(of = "id")
@Getter
@RequiredArgsConstructor
@ToString
// Needs to be explicitly named as Order is a reserved keyword
@Table(name = "SampleOrder")
public class Order {

	private @Id @GeneratedValue Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	private final List<LineItem> lineItems = new ArrayList<LineItem>();
	private final CustomerId customer;

	Order() {
		this.customer = null;
	}

	/**
	 * Adds a {@link LineItem} to the {@link Order}.
	 * 
	 * @param lineItem must not be {@literal null}.
	 */
	public void add(LineItem lineItem) {

		Assert.notNull(lineItem, "Line item must not be null!");

		this.lineItems.add(lineItem);
	}

	@Entity
	@EqualsAndHashCode(of = "id")
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@Table(name = "LineItem")
	public static class LineItem {

		private @Id @GeneratedValue Long id;
		private String description;
	}
}
