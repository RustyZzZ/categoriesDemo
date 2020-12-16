package com.example.categoriesDemo.dtos;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductResponse extends RepresentationModel<ProductResponse> {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String currency;
}
