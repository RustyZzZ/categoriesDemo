package com.example.categoriesDemo.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;

@Data
public class RateDto {
	private Map<String, BigDecimal> rates;
	private String base;
	private LocalDate date;
}
