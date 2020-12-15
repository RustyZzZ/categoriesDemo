package com.example.categoriesDemo.models;

import java.math.BigDecimal;
import java.util.Currency;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@Data
public class Product extends Identity {
	@Column(length = 3, nullable = false)
	private final Currency currency = Currency.getInstance("EUR");
	@Column(scale = 2, nullable = false)
	private BigDecimal price;
}
