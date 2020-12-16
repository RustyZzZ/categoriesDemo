package com.example.categoriesDemo.controllers;


import com.example.categoriesDemo.dtos.CategoryRequest;
import com.example.categoriesDemo.dtos.CategoryResponse;
import com.example.categoriesDemo.services.CategoryService;
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
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService service;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
		return ResponseEntity.ok(service.createCategory(request));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<List<CategoryResponse>> getAllCategories() {
		return ResponseEntity.ok(service.getAllCategories());
	}

	@GetMapping("/{categoryId}")
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(service.getCategory(categoryId));
	}

	@GetMapping("/{categoryId}/subcategories")
	@PreAuthorize("hasAnyRole('ADMIN, USER')")
	public ResponseEntity<List<CategoryResponse>> getSubcategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(service.getSubcategories(categoryId));
	}

	@PostMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponse> createSubcategory(@RequestBody CategoryRequest request, @PathVariable Long categoryId) {
		return ResponseEntity.ok(service.createSubcategory(request, categoryId));
	}

	@PutMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable Long categoryId) {
		return ResponseEntity.ok(service.updateCategory(request, categoryId));
	}

	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteCategory(@PathVariable Long category) {
		service.deleteCategory(category);
		return ResponseEntity.ok("Successfully deleted");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> exceptionHandling() {
		return ResponseEntity.badRequest().build();
	}
}
