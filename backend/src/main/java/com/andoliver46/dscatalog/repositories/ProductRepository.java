package com.andoliver46.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andoliver46.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
