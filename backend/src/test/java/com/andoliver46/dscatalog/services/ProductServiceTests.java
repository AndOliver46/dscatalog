package com.andoliver46.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.andoliver46.dscatalog.dto.ProductDTO;
import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.entities.Product;
import com.andoliver46.dscatalog.repositories.CategoryRepository;
import com.andoliver46.dscatalog.repositories.ProductRepository;
import com.andoliver46.dscatalog.services.exceptions.DatabaseException;
import com.andoliver46.dscatalog.services.exceptions.ResourceNotFoundException;
import com.andoliver46.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Mock 
	private CategoryRepository categoryRepository;

	private long exisitingId;
	private long nonExisitingId;
	private long dependentId;
	private Product product;
	private ProductDTO productDTO;
	private Category category;
	private PageImpl<Product> page;

	@BeforeEach
	void setUp() throws Exception {
		exisitingId = 1L;
		nonExisitingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));

		
		Mockito.when(repository.getOne(exisitingId)).thenReturn(product);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getOne(nonExisitingId);
		
		Mockito.when(categoryRepository.getOne(exisitingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExisitingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(exisitingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExisitingId)).thenReturn(Optional.empty());

		Mockito.doNothing().when(repository).deleteById(exisitingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExisitingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(exisitingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(exisitingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesntExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExisitingId);
		});
	
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExisitingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
	
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(exisitingId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findById(exisitingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesntExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExisitingId);
		});
		Mockito.verify(repository).findById(nonExisitingId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		productDTO = service.update(productDTO, exisitingId);
		
		Assertions.assertNotNull(productDTO);
		Mockito.verify(repository).getOne(exisitingId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesntExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productDTO = service.update(productDTO, nonExisitingId);
		});
		Mockito.verify(repository).getOne(nonExisitingId);
	}
}
