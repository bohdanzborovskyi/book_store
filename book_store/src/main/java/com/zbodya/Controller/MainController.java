package com.zbodya.Controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zbodya.Model.Author;
import com.zbodya.Model.Book;
import com.zbodya.Model.Publisher;
import com.zbodya.Model.Repositories.AuthorRepository;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Model.Repositories.PublisherRepository;
import com.zbodya.Service.DBService;

@RestController()
@RequestMapping(name = "/")
public class MainController
{
	
	@Autowired
	private DBService dbservice;
	
	@Autowired
	private	BookRepository bookRepo;
	
	@Autowired
	private AuthorRepository authorRepo;
	
	@Autowired
	private PublisherRepository publisherRepo;

	@RequestMapping(value="")
	public String getBooks() 
	{
		String result = " ";
		Resource resource = new ClassPathResource("static/images/photo.jpg");
		EntityManager manager = dbservice.openDBConnection();
		File f;
		try {
			f = resource.getFile();
			Book b1 = new Book(LocalDate.of(2012, 3, 13),"Black peart","Pirate book",f);
			Book b2 = new Book(LocalDate.of(2012, 3, 13),"Black peart","Pirate book",f);			
			Publisher p1 = new Publisher("Publish UK","Publisher in UK",LocalDate.of(2000, 2, 22));
			Publisher p2 = new Publisher("Publish USA","Publisher in USA",LocalDate.of(2000, 2, 22));
			Author a1 = new Author(LocalDate.of(2000, 2, 22),"Fedor Dostoyevsky","Russian poet",f);
			Author a2 = new Author(LocalDate.of(2000, 2, 22),"Scott Fitzegerald","USA poet",f);
			b1.addPublisher(p1);
			b2.addPublisher(p2);
			b1.addAuthor(a1);
			b2.addAuthor(a2);
			p1.addAuthor(a2);
			p2.addAuthor(a1);
			manager.persist(b2);
			manager.persist(b1);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
//		Book b1 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Black peart","Pirate book"));
//		Book b2 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Dracula","MOnster story"));
//		Book b3 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Washinghton","Book of Washinghton life"));			
		
		dbservice.closeDBConnection();
		return result;
	}
}
