package com.andoliver46.dscatalog.tests;

import java.time.Instant;

import com.andoliver46.dscatalog.dto.CategoryDTO;
import com.andoliver46.dscatalog.dto.ProductDTO;
import com.andoliver46.dscatalog.entities.Category;
import com.andoliver46.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png",
				Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		Category category = new Category(1L, "Electronics");
		return category;
	}

	public static CategoryDTO createCategoryDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}

}
