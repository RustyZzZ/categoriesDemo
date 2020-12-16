package com.example.categoriesDemo.services;


import com.example.categoriesDemo.controllers.CategoryController;
import com.example.categoriesDemo.dtos.ProductRequest;
import com.example.categoriesDemo.dtos.ProductResponse;
import com.example.categoriesDemo.models.Product;
import com.example.categoriesDemo.repositories.CategoryRepository;
import com.example.categoriesDemo.repositories.ProductRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
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
public class ProductService {

	public static final String EUR = Currency.getInstance("EUR").getCurrencyCode();
	private final ProductRepository repo;
	private final CategoryRepository categoryRepository;
	private final ModelMapper mm;
	private final CurrencyService currencyService;

	public ProductResponse createProduct(ProductRequest request, Long categoryId) {
		var product = mm.map(request, Product.class);
		product.setPrice(convertToEuros(request.getPrice(), request.getCurrency()));

		var category = categoryRepository.findById(categoryId)
										 .orElseThrow(() -> new IllegalArgumentException("No Category with this ID:" + categoryId));

		product.setCategory(category);
		return mapToProductResponse(repo.save(product));
	}

	public List<ProductResponse> getAllProducts() {
		return repo.findAll().stream()
				   .map(this::mapToProductResponse)
				   .collect(Collectors.toList());
	}

	public ProductResponse getProduct(Long productId) {
		var product = repo.findById(productId)
						  .orElseThrow(() -> new IllegalArgumentException("No Product with this ID: " + productId));
		return mapToProductResponse(product);
	}

	public ProductResponse updateProduct(ProductRequest request, Long productId) {
		var product = repo.findById(productId)
						  .orElseThrow(() -> new IllegalArgumentException("No Product with this ID: " + productId));
		product.setDescription(request.getDescription());
		product.setName(request.getName());
		product.setPrice(convertToEuros(request.getPrice(), request.getCurrency()));

		return mapToProductResponse(repo.save(product));
	}

	public List<ProductResponse> getProductsByCategory(Long categoryId) {
		var category = categoryRepository.findById(categoryId)
										 .orElseThrow(() -> new IllegalArgumentException("No Category with this ID: " + categoryId));
		return category.getProducts().stream()
					   .map(el -> mm.map(el, ProductResponse.class))
					   .collect(Collectors.toList());
	}

	public void deleteById(Long id) {
		repo.deleteById(id);
	}


	private ProductResponse mapToProductResponse(Product product) {
		var response = mm.map(product, ProductResponse.class);
		response.add(generateCategoryUrl(product.getCategory().getId()));
		return response;
	}

	private BigDecimal convertToEuros(BigDecimal price, String currency) {
		if (currency.equals(EUR)) {
			return price;
		}
		return currencyService.getPriceInEuros(price, currency)
							  .setScale(2, RoundingMode.CEILING);
	}

	private Link generateCategoryUrl(Long categoryId) {
		return linkTo(methodOn(CategoryController.class).getCategory(categoryId)).withRel("category");
	}

}
