package com.zbodya.Controller;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbodya.Model.Book;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Service.DBService;

@Controller()
@RequestMapping(name = "/")
public class MainController
{
	
	@Autowired
	static DBService dbservice;
	
	@Autowired
	static 	BookRepository bookRepo;

	@RequestMapping(value="/")
	public static String getBooks() 
	{
		String result = " ";
		EntityManager manager = dbservice.openDBConnection();
		Book b = new Book(LocalDate.of(2012, 3, 13),"Bible","Book of fairytales", null, null, null);
		Book b1 = new Book(LocalDate.of(2012, 3, 13),"Idiot","Dostoyevsky book", null, null, null);
		Book b2 = new Book(LocalDate.of(2012, 3, 13),"Master and Margarhita","Book of USSR suspense", null, null, null);
		manager.persist(b2);
		manager.persist(b);
		manager.persist(b1);
		List<Book> books = (List<Book>) bookRepo.findAll();
		for(Book book : books) 
		{
			result += "/n " + book.getTitle() + " " + book.getPublishDate() + " " + book.getDescribtion();
		}
		return result;
	}
}
