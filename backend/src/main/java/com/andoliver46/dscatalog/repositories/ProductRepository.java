package com.andoliver46.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT DISTINCT obj "
			+ "FROM Product obj " 
			+ "INNER JOIN obj.categories cat "
			+ "WHERE (COALESCE(:categories) IS NULL OR cat IN :categories) "
			+ "AND "
			+ "(LOWER(obj.name) LIKE CONCAT('%',LOWER(:name),'%'))")
	Page<Product> find(Pageable pageable, List<Category> categories, String name);
}
