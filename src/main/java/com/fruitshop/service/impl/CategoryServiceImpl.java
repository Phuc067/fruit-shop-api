package com.fruitshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.entity.Category;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CategoryRepository;
import com.fruitshop.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ResponseObject getAll() {
		
		List<Category> categories = categoryRepository.findAll();
		return new ResponseObject(HttpStatus.OK, "Lấy danh sách các loại trái cây thành công", categories);
	}
	
}
