package com.example.categoriesDemo.repositories;


import com.example.categoriesDemo.models.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByCategory_id(Long id);
}
