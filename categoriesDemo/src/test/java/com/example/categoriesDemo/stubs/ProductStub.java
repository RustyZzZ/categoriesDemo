package com.example.categoriesDemo.stubs;

import com.example.categoriesDemo.dtos.ProductRequest;
import com.example.categoriesDemo.models.Product;
import java.math.BigDecimal;

public final class ProductStub {
	public static ProductRequest generateProductRequest() {
		var request = new ProductRequest();
		request.setName("Name");
		request.setDescription("Description");
		request.setPrice(BigDecimal.valueOf(123));
		request.setCurrency("EUR");
		return request;
	}

	public static Product generateProduct() {
		var request = new Product();
		request.setName("Name");
		request.setDescription("Description");
		request.setPrice(BigDecimal.valueOf(123));
		request.setId(100L);
		request.setCategory(CategoryStub.generateCategory());
		return request;
	}
}
