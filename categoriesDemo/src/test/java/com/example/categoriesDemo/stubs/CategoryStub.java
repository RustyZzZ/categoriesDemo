package com.example.categoriesDemo.stubs;

import com.example.categoriesDemo.dtos.CategoryRequest;
import com.example.categoriesDemo.models.Category;
import java.util.HashSet;

public final class CategoryStub {

	public static final String CATEGORY_NAME = "categoryName";
	public static final String CATEGORY_DESCRIPTION = "CategoryDescription";

	public static Category generateCategory() {
		var category = new Category();
		category.setId(1L);
		category.setName(CATEGORY_NAME);
		category.setDescription(CATEGORY_DESCRIPTION);
		category.setProducts(new HashSet<>());
		category.setCategory(null);
		return category;
	}

	public static Category generateSubcategory() {
		var category = new Category();
		category.setId(2L);
		category.setName(CATEGORY_NAME);
		category.setDescription(CATEGORY_DESCRIPTION);
		category.setProducts(new HashSet<>());
		category.setCategory(generateCategory());
		return category;
	}

	public static CategoryRequest generateCategoryRequest() {
		return CategoryRequest.builder()
							  .name(CATEGORY_NAME)
							  .description(CATEGORY_DESCRIPTION)
							  .build();
	}
}
