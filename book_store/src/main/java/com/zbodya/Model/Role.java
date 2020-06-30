package com.zbodya.Model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "role_s")
public class Role implements GrantedAuthority 
{

	@Id
	private Long id;
	
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User>users;
	
	public Role() {}
	
	public Role(Long id) {
		super();
		this.id = id;
	}



	public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}



	@Override
	public String getAuthority() {
		
		return getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	

}
