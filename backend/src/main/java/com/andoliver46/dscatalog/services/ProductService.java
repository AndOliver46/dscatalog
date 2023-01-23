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

import com.andoliver46.dscatalog.dto.ProductDTO;
import com.andoliver46.dscatalog.entities.Product;
import com.andoliver46.dscatalog.repositories.ProductRepository;
import com.andoliver46.dscatalog.services.exceptions.DatabaseException;
import com.andoliver46.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repo.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repo.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada."));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product obj = new Product();
		//obj.setName(dto.getName());
		obj = repo.save(obj);
		return new ProductDTO(obj);
	}

	@Transactional
	public ProductDTO update(ProductDTO dto, Long id) {
		try {
			Product obj = repo.getOne(id);
			//obj.setName(dto.getName());
			obj = repo.save(obj);
			return new ProductDTO(obj);
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

}
