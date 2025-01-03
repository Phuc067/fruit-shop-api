package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Login;
import com.fruitshop.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByLogin(Login login);

}
