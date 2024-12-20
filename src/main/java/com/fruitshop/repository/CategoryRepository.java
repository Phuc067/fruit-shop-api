package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	Category save(Category category);
}
