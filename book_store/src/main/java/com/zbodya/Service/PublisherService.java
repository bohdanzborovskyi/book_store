package com.zbodya.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zbodya.Model.Publisher;
import com.zbodya.Model.Repositories.PublisherRepository;

@Service
public class PublisherService 
{
	@Autowired
	PublisherRepository repo;
	
	public Page<Publisher> getPublishers(Integer pageNo, Integer pageSize, String sortBy)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Publisher> page = repo.findAll(pageable);
		return page;
	}
	
	public Page<Publisher> findPublishers(Integer pageNo, Integer pageSize, String sortBy, String findBy, String findKey)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Publisher> page;
		if(findBy.equals("name")) {page = repo.findByNameContainingIgnoreCase(pageable,findKey);}
		else if(findBy.equals("description")) {page = repo.findByDescriptionContainingIgnoreCase(pageable,findKey);}
		else page = repo.findAll(pageable);
		return page;
	}
}
