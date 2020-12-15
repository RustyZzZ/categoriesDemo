package com.example.categoriesDemo.security.repositories;

import com.example.categoriesDemo.security.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(Role.ERole name);
}
