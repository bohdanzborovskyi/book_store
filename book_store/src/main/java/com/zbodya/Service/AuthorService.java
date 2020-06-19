package com.zbodya.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zbodya.Model.Author;
import com.zbodya.Model.Repositories.AuthorRepository;

@Service
public class AuthorService 
{
	@Autowired
	AuthorRepository repo;
	
	public Page<Author> getAuthors(Integer pageNo, Integer pageSize, String sortBy)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Author> page = repo.findAll(pageable);
		return page;
	}
	
	public Page<Author> findAuthors(Integer pageNo, Integer pageSize, String sortBy, String findBy, String findKey)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Author> page;
		if(findBy.equals("name")) {page = repo.findByNameContainingIgnoreCase(pageable,findKey);}
		else if(findBy.equals("description")) {page = repo.findByDescriptionContainingIgnoreCase(pageable,findKey);}
		else page = repo.findAll(pageable);
		return page;
	}
}
