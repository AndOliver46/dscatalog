package com.andoliver46.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repo;

	public List<Category> findAll(){
		return repo.findAll();
	}
	
}
