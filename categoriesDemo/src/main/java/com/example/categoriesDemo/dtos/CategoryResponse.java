package com.example.categoriesDemo.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponse extends RepresentationModel<CategoryResponse> {
	private Long id;
	private String name;
	private String description;
}
