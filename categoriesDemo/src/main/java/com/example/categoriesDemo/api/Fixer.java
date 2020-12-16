package com.example.categoriesDemo.api;


import com.example.categoriesDemo.dtos.RateDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://data.fixer.io/api", name = "fixer")
@Cacheable(value = "RateDto")
public interface Fixer {
	@RequestMapping(method = RequestMethod.GET, path = "/latest")
	RateDto getRate(@RequestParam String access_key);
}
