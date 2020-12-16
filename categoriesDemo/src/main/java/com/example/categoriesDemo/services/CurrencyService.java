package com.example.categoriesDemo.services;


import com.example.categoriesDemo.api.Fixer;
import com.example.categoriesDemo.dtos.RateDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

	private final Fixer rateApi;
	@Value("${fixer.access.key}")
	private String accessKey;

	public BigDecimal getPriceInEuros(BigDecimal price, String currency) {
		RateDto dto = rateApi.getRate(accessKey);
		var rate = dto.getRates().get(currency);
		return price.divide(rate, RoundingMode.CEILING);
	}

}
