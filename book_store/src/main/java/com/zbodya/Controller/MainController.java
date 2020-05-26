package com.zbodya.Controller;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zbodya.Model.Book;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Service.DBService;

@RestController()
@RequestMapping(name = "/")
public class MainController
{
	
	@Autowired
	private DBService dbservice;
	
	@Autowired
	private	BookRepository bookRepo;

	@RequestMapping(value="")
	public String getBooks() 
	{
		String result = " ";
		EntityManager manager = dbservice.openDBConnection();
//		Book b = new Book(LocalDate.of(2012, 3, 13),"Bible","Book of fairytales");
//		Book b1 = new Book(LocalDate.of(2012, 3, 13),"Idiot","Dostoyevsky book");
//		Book b2 = new Book(LocalDate.of(2012, 3, 13),"Master and Margarhita","Book of USSR suspense");
//		manager.persist(b2);
//		manager.persist(b);
//		manager.persist(b1);
//		Query query = manager.createQuery("Select b from Book b ");
//		List<Book> books = (List<Book>)query.getResultList();
		System.out.println(bookRepo.toString());
		Book b1 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Black peart","Pirate book"));
		Book b2 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Dracula","MOnster story"));
		Book b3 = bookRepo.save(new Book(LocalDate.of(2012, 3, 13),"Washinghton","Book of Washinghton life"));			
		List<Book> books = (List<Book>) bookRepo.findAll();
		for(Book book : books) 
		{
			result += "/n " + book.getTitle() + " " + book.getPublishDate() + " " + book.getDescribtion();
		}
		dbservice.closeDBConnection();
		return result;
	}
}
