package com.example.categoriesDemo.dtos;

import lombok.Data;
import lombok.Value;

@Data
public class LoginRequest {
	private String username;
	private String password;
}
