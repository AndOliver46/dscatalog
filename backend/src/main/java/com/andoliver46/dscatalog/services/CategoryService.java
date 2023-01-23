package com.andoliver46.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andoliver46.dscatalog.dto.CategoryDTO;
import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.repositories.CategoryRepository;
import com.andoliver46.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repo.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).toList();
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repo.findById(id);
		Category catDTO = obj
				.orElseThrow(() -> new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada."));
		return new CategoryDTO(catDTO);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category cat = new Category();
		cat.setName(dto.getName());
		cat = repo.save(cat);
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO update(CategoryDTO dto, Long id) {
		try {
			Category cat = repo.getOne(id);
			cat.setName(dto.getName());
			cat = repo.save(cat);
			return new CategoryDTO(cat);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("A categoria de Id: " + id + " não foi encontrada.");
		}

	}

}
