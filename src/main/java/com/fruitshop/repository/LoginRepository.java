package com.fruitshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Login;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

  Optional<?> findByUsername(String username);

  boolean existsByEmail(String email);

  @Modifying
  @Query("UPDATE Login l SET l.otp = NULL WHERE l.username = :username")
  void removeVerificationCode(@Param("username") String username);
}
