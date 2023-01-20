package com.andoliver46.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andoliver46.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
}
