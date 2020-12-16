package com.example.categoriesDemo.services;

import com.example.categoriesDemo.repositories.CategoryRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.categoriesDemo.stubs.CategoryStub.generateCategory;
import static com.example.categoriesDemo.stubs.CategoryStub.generateCategoryRequest;
import static com.example.categoriesDemo.stubs.CategoryStub.generateSubcategory;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class CategoryServiceTest {

	public static final Long PARENT_ID = 1L;
	@MockBean
	private CategoryRepository repo;
	@Autowired
	private CategoryService catService;

	@Test
	void should_successfully_create_category_from_request() {
		var expCat = generateCategory();
		when(repo.save(any())).thenReturn(expCat);
		var resultResponse = catService.createCategory(generateCategoryRequest());
		assertAll(() -> {
			assertEquals(expCat.getId(), resultResponse.getId());
			assertEquals(expCat.getName(), resultResponse.getName());
			assertEquals(expCat.getDescription(), resultResponse.getDescription());
			assertTrue(resultResponse.getLinks().hasSize(2));
		});
	}

	@Test
	void should_successfully_create_subcategory_from_request() {
		var expSub = generateSubcategory();
		when(repo.findById(PARENT_ID)).thenReturn(Optional.of(generateCategory()));
		when(repo.save(any())).thenReturn(expSub);
		var resultResponse = catService.createSubcategory(generateCategoryRequest(), PARENT_ID);
		assertAll(() -> {
			assertEquals(expSub.getId(), resultResponse.getId());
			assertEquals(expSub.getName(), resultResponse.getName());
			assertEquals(expSub.getDescription(), resultResponse.getDescription());
			assertTrue(resultResponse.getLinks().hasSize(2));
		});
	}

	@Test
	void should_throw_exception_no_category_during_creation_subcategory() {
		when(repo.findById(PARENT_ID)).thenReturn(Optional.empty());
		var e = assertThrows(IllegalArgumentException.class,
				() -> catService.createSubcategory(generateCategoryRequest(), PARENT_ID));

		assertTrue(e.getMessage().contains("No Category with this ID"));
	}

	@Test
	void should_successfully_update_category_from_request() {
		var expCat = generateSubcategory();
		when(repo.findById(PARENT_ID)).thenReturn(Optional.of(generateCategory()));
		when(repo.save(any())).thenReturn(expCat);
		var resultResponse = catService.updateCategory(generateCategoryRequest(), PARENT_ID);
		assertAll(() -> {
			assertEquals(expCat.getId(), resultResponse.getId());
			assertEquals(expCat.getName(), resultResponse.getName());
			assertEquals(expCat.getDescription(), resultResponse.getDescription());
			assertTrue(resultResponse.getLinks().hasSize(2));
		});
	}

	@Test
	void should_throw_exception_no_category_during_update_category() {
		when(repo.findById(PARENT_ID)).thenReturn(Optional.empty());
		var e = assertThrows(IllegalArgumentException.class,
				() -> catService.updateCategory(generateCategoryRequest(), PARENT_ID));

		assertTrue(e.getMessage().contains("No Category with this ID"));
	}

	@Test
	void should_successfully_get_category_by_id() {
		var expCat = generateCategory();
		when(repo.findById(PARENT_ID)).thenReturn(Optional.of(expCat));
		var resultResponse = catService.getCategory(PARENT_ID);
		assertAll(() -> {
			assertEquals(expCat.getId(), resultResponse.getId());
			assertEquals(expCat.getName(), resultResponse.getName());
			assertEquals(expCat.getDescription(), resultResponse.getDescription());
			assertTrue(resultResponse.getLinks().hasSize(2));
		});
	}

	@Test
	void should_throw_exception_no_category_during_get_category_by_id() {
		when(repo.findById(PARENT_ID)).thenReturn(Optional.empty());
		var e = assertThrows(IllegalArgumentException.class,
				() -> catService.getCategory(PARENT_ID));

		assertTrue(e.getMessage().contains("No Category with this ID"));
	}

	@Test
	void should_successfully_get_all_categories() {
		var expCat0 = generateCategory();
		var expCat1 = generateSubcategory();
		when(repo.findAll()).thenReturn(Arrays.asList(expCat0, expCat1));
		var resultResponse = catService.getAllCategories();
		assertAll(() -> {
			assertEquals(2, resultResponse.size());
			var result0 = resultResponse.get(0);

			assertEquals(expCat0.getId(), result0.getId());
			assertEquals(expCat0.getName(), result0.getName());
			assertEquals(expCat0.getDescription(), result0.getDescription());
			assertTrue(result0.getLinks().hasSize(2));

			var result1 = resultResponse.get(1);

			assertEquals(expCat1.getId(), result1.getId());
			assertEquals(expCat1.getName(), result1.getName());
			assertEquals(expCat1.getDescription(), result1.getDescription());
			assertTrue(result1.getLinks().hasSize(2));
		});
	}

	@Test
	void should_successfully_get_all_0_categories() {
		when(repo.findAll()).thenReturn(Collections.emptyList());
		var resultResponse = catService.getAllCategories();
		assertEquals(0, resultResponse.size());
	}

	@Test
	void should_successfully_delete_category() {
		var deleteCaptor = ArgumentCaptor.forClass(Long.class);
		catService.deleteCategory(PARENT_ID);
		verify(repo, atLeast(1)).deleteById(deleteCaptor.capture());
		assertEquals(PARENT_ID, deleteCaptor.getValue());
	}

	@Test
	void should_successfully_get_all_subcategories() {
		var expCat0 = generateCategory();
		var expCat1 = generateSubcategory();
		when(repo.findAllByCategory_id(any())).thenReturn(Arrays.asList(expCat0, expCat1));
		var resultResponse = catService.getSubcategories(PARENT_ID);
		assertAll(() -> {
			assertEquals(2, resultResponse.size());
			var result0 = resultResponse.get(0);

			assertEquals(expCat0.getId(), result0.getId());
			assertEquals(expCat0.getName(), result0.getName());
			assertEquals(expCat0.getDescription(), result0.getDescription());
			assertTrue(result0.getLinks().hasSize(2));

			var result1 = resultResponse.get(1);

			assertEquals(expCat1.getId(), result1.getId());
			assertEquals(expCat1.getName(), result1.getName());
			assertEquals(expCat1.getDescription(), result1.getDescription());
			assertTrue(result1.getLinks().hasSize(2));
		});
	}

	@Test
	void should_successfully_get_all_0_subcategories() {
		when(repo.findAllByCategory_id(any())).thenReturn(Collections.emptyList());
		var resultResponse = catService.getSubcategories(PARENT_ID);
		assertEquals(0, resultResponse.size());
	}
//	@TestConfiguration
//	static class CategoryServiceTestConfiguration {
//
//		@Bean
//		public CategoryService catService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
//			return new CategoryService(categoryRepository, modelMapper);
//		}
//	}
}
