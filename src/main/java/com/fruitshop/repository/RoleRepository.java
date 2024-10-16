package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
