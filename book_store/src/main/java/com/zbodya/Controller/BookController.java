package com.zbodya.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.zbodya.Model.Book;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Service.DBService;


@Controller
@RequestMapping(value = "/book")
public class BookController {
	
	@Autowired 
	DBService dbService;
	
	@Autowired
	BookRepository bookRepo;
	
	@GetMapping(value = "/addBookForm")
	public String addBookForm(Book book) 
	{
		return "addBookForm";
	}
	
	@PostMapping(value = "/addBook")
	public String addBook(@ModelAttribute("book")@Valid Book book,BindingResult bindingResult,@RequestPart("file")MultipartFile mFile, Model model) 
	{
		book.setFileName(mFile.getOriginalFilename());			
		book = bookRepo.saveAndFlush(book);
//		EntityManager entityManager = dbService.openDBConnection();
//		entityManager.persist(book);
//		dbService.closeDBConnection();
		List<Book>books = bookRepo.findAll();			
		model.addAttribute("books", books);
		System.out.println(mFile.getOriginalFilename() + " " + mFile.getSize() + book.getID());
		if(bindingResult.hasErrors()) 
		{
			return "addBookForm";
		}
		else 
		{	
			File file = new File("C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\books\\" + book.getID());
			try {
				file.mkdirs();
				file = new File("C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\books\\" + book.getID() + "\\" + mFile.getOriginalFilename());
				file.createNewFile();
				mFile.transferTo(file);
			} catch (IOException e) 
			{				
				e.printStackTrace();
			}			
			return "allBooks";
		}
	}
	
	@GetMapping(value="/allBooks")
	public String allBooks(Model model)
	{
		List<Book>books = bookRepo.findAll();
		
		model.addAttribute("books", books);
		return "allBooks";
	}

}
