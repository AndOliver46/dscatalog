package com.andoliver46.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andoliver46.dscatalog.dto.CategoryDTO;
import com.andoliver46.dscatalog.dto.ProductDTO;
import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.entities.Product;
import com.andoliver46.dscatalog.repositories.CategoryRepository;
import com.andoliver46.dscatalog.repositories.ProductRepository;
import com.andoliver46.dscatalog.services.exceptions.DatabaseException;
import com.andoliver46.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repo.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repo.findById(id);
		Product entity = obj
				.orElseThrow(() -> new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada."));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repo.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(ProductDTO dto, Long id) {
		try {
			Product entity = repo.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repo.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada.");
		}

	}

	public void delete(Long id) {
		try {
			repo.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada.");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Impossível excluir, Integrity violation");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());

		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepo.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}
