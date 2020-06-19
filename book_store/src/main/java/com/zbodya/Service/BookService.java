package com.zbodya.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zbodya.Model.Book;
import com.zbodya.Model.Repositories.BookRepository;

@Service
public class BookService 
{
	@Autowired
	BookRepository repo;
	
	public Page<Book> getBooks(Integer pageNo, Integer pageSize, String sortBy)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Book> page = repo.findAll(pageable);
		return page;
	}
	
	public Page<Book> findBooks(Integer pageNo, Integer pageSize, String sortBy, String findBy, String findKey)
	{
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Book> page;
		if(findBy.equals("title")) {page = repo.findByTitleContainingIgnoreCase(pageable,findKey);}
		else if(findBy.equals("describtion")) {page = repo.findByDescribtionContainingIgnoreCase(pageable,findKey);}
		else page = repo.findAll(pageable);
		return page;
	}
}
