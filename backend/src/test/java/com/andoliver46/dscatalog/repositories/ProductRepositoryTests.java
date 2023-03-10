package com.andoliver46.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.andoliver46.dscatalog.entities.Product;
import com.andoliver46.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = Long.MAX_VALUE;
		countTotalProducts = 25L;
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		// Act
		repo.deleteById(existingId);

		// Assert
		Optional<Product> result = repo.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

			repo.deleteById(nonExistingId);
			repo.findById(nonExistingId);
		});
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);

		product = repo.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void findByIdShouldReturnNonNullOptionalWhenIdExists() {
		Optional<Product> result = repo.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnNullOptionalWhenIdDoesntExist() {

		Optional<Product> result = repo.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}

}
