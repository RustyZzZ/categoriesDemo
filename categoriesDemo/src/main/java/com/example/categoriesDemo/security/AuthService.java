package com.example.categoriesDemo.security;

import com.example.categoriesDemo.dtos.LoginRequest;
import com.example.categoriesDemo.dtos.LoginResponse;
import com.example.categoriesDemo.dtos.SignUpRequest;
import com.example.categoriesDemo.security.models.Role;
import com.example.categoriesDemo.security.models.User;
import com.example.categoriesDemo.security.repositories.RoleRepository;
import com.example.categoriesDemo.security.repositories.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;

	public LoginResponse authenticateRequest(LoginRequest request) {
		var auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		var jwt = jwtUtils.generateJwtToken(auth);
		var details = (UserDetailsImpl) auth.getPrincipal();
		var roles = details.getAuthorities().stream()
						   .map(GrantedAuthority::getAuthority)
						   .collect(Collectors.toList());

		return LoginResponse.builder()
							.jwt(jwt)
							.username(details.getUsername())
							.roles(roles)
							.build();
	}

	public String signUpUser(SignUpRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Username " + request.getUsername() + " already exist");
		}
		var user = User.builder()
					   .username(request.getUsername())
					   .password(encoder.encode(request.getPassword()))
					   .roles(mapRoles(request)).build();

		userRepository.save(user);

		return "User was successfully created";
	}


	private Set<Role> mapRoles(SignUpRequest request) {
		return request.getRoles()
					  .stream()
					  .map(this::mapStringToRole)
					  .collect(Collectors.toSet());
	}

	private Role mapStringToRole(String roleString) {
		return roleRepository.findByName(Role.ERole.valueOf(roleString))
							 .orElseThrow(() -> new IllegalArgumentException("Wrong name of role" + roleString));
	}
}
