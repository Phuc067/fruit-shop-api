package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, String>{

}
