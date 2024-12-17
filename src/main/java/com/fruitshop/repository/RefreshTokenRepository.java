package com.fruitshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	RefreshToken findByLogin(Login login);

	RefreshToken findByToken(String token);

  void deleteByToken(String refreshToken);
}
