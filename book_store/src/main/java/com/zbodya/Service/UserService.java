package com.zbodya.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zbodya.Model.Role;
import com.zbodya.Model.User;
import com.zbodya.Model.Repositories.RoleRepository;
import com.zbodya.Model.Repositories.UserRepository;


@Service
public class UserService implements UserDetailsService 
{
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepo.findByUsername(username);
		
		if(user==null) 
		{
			throw new UsernameNotFoundException("User not found");
		}
		
		return user;
	}
	
	public User findUserById(Long userId) 
	{	
		Optional<User> user = userRepo.findById(userId);
		return user.orElse(new User());
	}
	
	public List<User> allUsers()
	{
		return userRepo.findAll();
	}
	
	public boolean saveUser(User user) 
	{
		User userFromDB = userRepo.findByUsername(user.getUsername());
		
		if(userFromDB != null) 
		{
			return false;
		}
		
		user.setRoles(Collections.singleton(new Role(1L,"ROLE_USER")));
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return true;
	}
	
	public boolean deleteUser(Long userId) 
	{
		if(userRepo.findById(userId).isPresent()) 
		{
			userRepo.deleteById(userId);
			return true;
		}
		return false;
	}
	
	

}
