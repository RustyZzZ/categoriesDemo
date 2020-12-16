package com.example.categoriesDemo.controllers;

import com.example.categoriesDemo.dtos.ProductRequest;
import com.example.categoriesDemo.repositories.CategoryRepository;
import com.example.categoriesDemo.repositories.ProductRepository;
import com.example.categoriesDemo.stubs.CategoryStub;
import com.example.categoriesDemo.stubs.ProductStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryRepository categoryRepository;

	@MockBean
	private ProductRepository productRepository;

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// create category
	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_save_in_euro() throws Exception {
		var request = ProductStub.generateProductRequest();
		var expectedProduct = ProductStub.generateProduct();
		when(categoryRepository.findById(any())).thenReturn(Optional.of(CategoryStub.generateCategory()));
		when(productRepository.save(any())).thenReturn(expectedProduct);
		mockMvc.perform(postRequest("/api/categories/1/products", request))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_save_in_usd() throws Exception {
		var request = ProductStub.generateProductRequest();
		request.setCurrency("USD");
		var expectedProduct = ProductStub.generateProduct();
		when(categoryRepository.findById(any())).thenReturn(Optional.of(CategoryStub.generateCategory()));
		when(productRepository.save(any())).thenReturn(expectedProduct);
		mockMvc.perform(postRequest("/api/categories/1/products", request))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_return_403_save_in_euro() throws Exception {
		var request = ProductStub.generateProductRequest();
		mockMvc.perform(postRequest("/api/categories/1/products", request))
			   .andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	void should_return_401_save_in_euro() throws Exception {
		var request = ProductStub.generateProductRequest();

		mockMvc.perform(postRequest("/api/categories/1/products", request))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_return_400_save_in_euro() throws Exception {
		var request = ProductStub.generateProductRequest();
		when(categoryRepository.findById(any())).thenReturn(Optional.empty());
		mockMvc.perform(postRequest("/api/categories/100/products", request))
			   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	// get products by category
	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_get_by_category_admin() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		var category = CategoryStub.generateCategory();
		category.getProducts().add(expectedProduct);
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		mockMvc.perform(getRequest("/api/categories/1/products"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_successfully_get_by_category_user() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		var category = CategoryStub.generateCategory();
		category.getProducts().add(expectedProduct);
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		mockMvc.perform(getRequest("/api/categories/1/products"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	void should_return_401_get_by_category() throws Exception {
		mockMvc.perform(getRequest("/api/categories/100/products"))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	// get product by id

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_return_400_get_by_category() throws Exception {
		when(categoryRepository.findById(any())).thenReturn(Optional.empty());
		mockMvc.perform(getRequest("/api/categories/1/products"))
			   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_get_by_id_admin() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		when(productRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
		mockMvc.perform(getRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_successfully_get_by_id_user() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		when(productRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
		mockMvc.perform(getRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	void should_return_401_get_by_id() throws Exception {
		mockMvc.perform(getRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_return_400_get_by_id() throws Exception {
		when(productRepository.findById(any())).thenReturn(Optional.empty());
		mockMvc.perform(getRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	// get all products
	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_get_all_admin() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedProduct));
		mockMvc.perform(getRequest("/api/products"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_successfully_get_all_user() throws Exception {
		var expectedProduct = ProductStub.generateProduct();
		when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedProduct));
		mockMvc.perform(getRequest("/api/products"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	void should_return_401_get_all() throws Exception {
		mockMvc.perform(getRequest("/api/products"))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_return_empty_array_get_all() throws Exception {
		when(productRepository.findAll()).thenReturn(Collections.emptyList());
		mockMvc.perform(getRequest("/api/products"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string("[]"));
	}

	// update product

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_update_by_id() throws Exception {
		var request = ProductStub.generateProductRequest();
		request.setName("Name1");
		var expectedProduct = ProductStub.generateProduct();
		when(productRepository.findById(any())).thenReturn(Optional.of(expectedProduct));
		when(productRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
		mockMvc.perform(putRequest("/api/products/1", request))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andExpect(MockMvcResultMatchers.content().string(containsString(request.getName())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getDescription())))
			   .andExpect(MockMvcResultMatchers.content().string(containsString(expectedProduct.getCurrency().toString())));
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_return_403_update_by_id() throws Exception {
		var request = ProductStub.generateProductRequest();
		mockMvc.perform(putRequest("/api/products/1", request))
			   .andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	void should_return_401_update_by_id() throws Exception {
		var request = ProductStub.generateProductRequest();
		mockMvc.perform(putRequest("/api/products/1", request))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_return_400_update_by_id() throws Exception {
		var request = ProductStub.generateProductRequest();
		when(productRepository.findById(any())).thenReturn(Optional.empty());
		mockMvc.perform(putRequest("/api/products/1", request))
			   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}


	// delete product

	@Test
	@WithMockUser(roles = "ADMIN")
	void should_successfully_deleted_by_id() throws Exception {
		mockMvc.perform(deleteRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isOk());
		verify(productRepository, atLeast(1)).deleteById(1L);
	}

	@Test
	@WithMockUser(roles = "USER")
	void should_return_403_deleted_by_id() throws Exception {
		mockMvc.perform(deleteRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	void should_return_401_deleted_by_id() throws Exception {
		var request = ProductStub.generateProductRequest();
		mockMvc.perform(deleteRequest("/api/products/1"))
			   .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}


	private MockHttpServletRequestBuilder postRequest(String url, ProductRequest request) {
		return post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(request));
	}

	private MockHttpServletRequestBuilder putRequest(String url, ProductRequest request) {
		return put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(request));
	}

	private MockHttpServletRequestBuilder getRequest(String url) {
		return get(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
	}

	private MockHttpServletRequestBuilder deleteRequest(String url) {
		return delete(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
	}
}