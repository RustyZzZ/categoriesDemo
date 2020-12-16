package com.example.categoriesDemo.controllers;


import com.example.categoriesDemo.dtos.ProductRequest;
import com.example.categoriesDemo.dtos.ProductResponse;
import com.example.categoriesDemo.services.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProductsController {
	private final ProductService service;

	@PostMapping("/categories/{categoryId}/products")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request, @PathVariable Long categoryId) {
		return ResponseEntity.ok(service.createProduct(request, categoryId));

	}

	@GetMapping("/categories/{categoryId}/products")
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable Long categoryId) {
		return ResponseEntity.ok(service.getProductsByCategory(categoryId));
	}

	@PutMapping("/products/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable Long productId) {
		return ResponseEntity.ok(service.updateProduct(request, productId));

	}

	@GetMapping("/products/{productId}")
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
		return ResponseEntity.ok(service.getProduct(productId));

	}

	@GetMapping("/products")
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(service.getAllProducts());

	}

	@DeleteMapping("/products/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		service.deleteById(productId);
		return ResponseEntity.ok("Product was successfully deleted");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> exceptionHandling() {
		return ResponseEntity.badRequest().build();
	}
}
