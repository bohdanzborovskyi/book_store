package com.zbodya.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	private String resources_books = "C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\books\\";
	
	
	@PostMapping(value = "/addBook")
	public String addBook(@ModelAttribute("book")@Valid Book book,BindingResult bindingResult,@RequestPart("file")MultipartFile mFile,@RequestPart("pdf")MultipartFile pdfFile, Model model) 
	{
		book.setFileName(mFile.getOriginalFilename());	
		book.setPdfName(pdfFile.getOriginalFilename());
		book = bookRepo.saveAndFlush(book);
		List<Book>books = bookRepo.findAll();			
		model.addAttribute("books", books);
		System.out.println(mFile.getOriginalFilename() + " " + mFile.getSize() + book.getID());
		if(bindingResult.hasErrors()) 
		{
			return "addBookForm";
		}
		else 
		{	
			File image = new File(resources_books + book.getID());			
			try {
				image.mkdirs();
				image = new File(resources_books + book.getID() + "\\" + mFile.getOriginalFilename());
				File pdf = new File(resources_books + book.getID() + "\\" + pdfFile.getOriginalFilename());
				pdfFile.transferTo(pdf);
				image.createNewFile();
				mFile.transferTo(image);
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
	
	@GetMapping(value = "/editBookForm/{id}")
	public String editBookForm(@PathVariable("id")Integer id, Model model) 
	{				
		EntityManager manager = dbService.openDBConnection();		
		System.out.println(id);		
		Book book = (Book) manager.createQuery("select b from Book b where id=" + id).getSingleResult();		
		System.out.println(book.getID());
		model.addAttribute("book", book);
		dbService.closeDBConnection(manager);
		return "editBookForm";
	}
	
	@PostMapping(value="/editBook/{id}")
	public String editBook(@PathVariable("id") Integer id, @Valid @ModelAttribute("book")Book book, BindingResult bindingResult, @RequestPart("file")MultipartFile mfile, Model model) 
	{		
		System.out.println("ID:" + book.getID() + book.getDescribtion());
		if(bindingResult.hasErrors()) 
		{
			return "editBookForm";
		}
		else 
		{
			if(!mfile.isEmpty())
			{	
				File file = new File(resources_books + book.getID());
				if(!file.exists()) file.mkdirs();
				book.setFileName(mfile.getOriginalFilename());	
				file = new File(resources_books + book.getID() + "\\" + mfile.getOriginalFilename());
				try {
					file.createNewFile();
					mfile.transferTo(file);
				} catch (IOException e) {					
					e.printStackTrace();
				}				
			}
			EntityManager manager = dbService.openDBConnection();
			manager.merge(book);					
			dbService.closeDBConnection(manager);
			return "redirect:/book/allBooks";
		}
	}
	
	@GetMapping(value="/deleteBook/{id}")
	public String deleteBook(@PathVariable Integer id) 
	{
		EntityManager manager = dbService.openDBConnection();
		Book book = (Book) manager.createQuery("select b from Book b where id=" + id).getSingleResult();
		File file = new File(resources_books + book.getID());
		file.delete();
		manager.remove(book);					
		dbService.closeDBConnection(manager);
		return "redirect:/book/allBooks";
	}
	
	@GetMapping(value = "/readBook/{id}")
	public String readBook(@PathVariable Integer id, Model model) 
	{
		EntityManager manager = dbService.openDBConnection();
		Book book = (Book) manager.createQuery("select b from Book b where id=" + id).getSingleResult();
		model.addAttribute("book", book);
		dbService.closeDBConnection(manager);
		return "readBook";
	}
	
	@GetMapping(value="/downloadPDF/{id}")
	public ResponseEntity<InputStreamResource> downloadPDF(@PathVariable Integer id) 
	{
		EntityManager manager = dbService.openDBConnection();
		Book book = (Book) manager.createQuery("select b from Book b where id=" + id).getSingleResult();
		File pdf = new File(resources_books + book.getID() + "\\" + book.getPdfName());
		dbService.closeDBConnection(manager);			
		HttpHeaders headers = new HttpHeaders();
		InputStreamResource media = null;
		try {
			media = new InputStreamResource(new FileInputStream(pdf));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().
				header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + pdf.getName()).
				contentType(MediaType.APPLICATION_PDF).
				contentLength(pdf.length()).
				body(media);
	}
	

}
