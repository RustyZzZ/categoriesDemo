package com.example.categoriesDemo.services;


import com.example.categoriesDemo.controllers.CategoryController;
import com.example.categoriesDemo.controllers.ProductsController;
import com.example.categoriesDemo.dtos.CategoryRequest;
import com.example.categoriesDemo.dtos.CategoryResponse;
import com.example.categoriesDemo.models.Category;
import com.example.categoriesDemo.repositories.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository repo;
	private final ModelMapper mm;

	public CategoryResponse createCategory(CategoryRequest request) {
		var category = mm.map(request, Category.class);
		return mapCategoryToResponse(repo.save(category));
	}

	public CategoryResponse createSubcategory(CategoryRequest request, long id) {
		var subcategory = mm.map(request, Category.class);
		repo.findById(id)
			.ifPresentOrElse(
					subcategory::setCategory,
					() -> {throw new IllegalArgumentException("No Category with this ID: " + id);}
			);
		return mapCategoryToResponse(repo.save(subcategory));
	}

	public CategoryResponse updateCategory(CategoryRequest request, Long id) {
		var category = repo.findById(id)
						   .orElseThrow(() -> new IllegalArgumentException("No Category with this ID: " + id));
		category.setDescription(request.getDescription());
		category.setName(request.getName());
		return mapCategoryToResponse(repo.save(category));
	}

	public CategoryResponse getCategory(Long id) {
		var category = repo.findById(id)
						   .orElseThrow(() -> new IllegalArgumentException("No Category with this ID: " + id));
		return mapCategoryToResponse(category);
	}

	public List<CategoryResponse> getAllCategories() {
		return repo.findAll().stream()
				   .map(this::mapCategoryToResponse)
				   .collect(Collectors.toList());
	}

	public void deleteCategory(Long id) {
		repo.deleteById(id);
	}

	public List<CategoryResponse> getSubcategories(Long categoryId) {
		return repo.findAllByCategory_id(categoryId).stream()
				   .map(this::mapCategoryToResponse)
				   .collect(Collectors.toList());
	}

	private CategoryResponse mapCategoryToResponse(Category category) {
		var response = mm.map(category, CategoryResponse.class);
		response.add(generateSubcategoriesUrl(response.getId()));
		response.add(generateProductsUrl(response.getId()));
		return response;
	}


	private Link generateSubcategoriesUrl(Long id) {
		return linkTo(methodOn(CategoryController.class).getSubcategory(id)).withRel("subcategories");
	}

	private Link generateProductsUrl(Long id) {
		return linkTo(methodOn(ProductsController.class).getProducts(id)).withRel("products");
	}
}
