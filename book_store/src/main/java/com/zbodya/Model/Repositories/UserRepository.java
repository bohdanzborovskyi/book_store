package com.zbodya.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbodya.Model.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
	User  findByUsername(String username);
	
	
}
