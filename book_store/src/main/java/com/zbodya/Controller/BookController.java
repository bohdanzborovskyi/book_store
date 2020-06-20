package com.zbodya.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.zbodya.Model.Author;
import com.zbodya.Model.Book;
import com.zbodya.Model.Publisher;
import com.zbodya.Model.Repositories.AuthorRepository;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Model.Repositories.PublisherRepository;
import com.zbodya.Service.BookService;
import com.zbodya.Service.DBService;


@Controller
@RequestMapping(value = "/book")
public class BookController {
	
	@Autowired 
	DBService dbService;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired 
	AuthorRepository authorRepo;
	
	@Autowired 
	PublisherRepository publisherRepo;
	
	@Autowired
	BookService bookService;	
	
	
	@GetMapping(value = "/addBookForm")
	public String addBookForm(Book book) 
	{
		return "book/addBookForm";
	}
	
	private final String resources_books = "C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\books\\";
	
	
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
			return "book/addBookForm";
		}
		else 
		{	
			File image = new File(resources_books + book.getID());			
			image.mkdirs();
			if(!mFile.isEmpty())
			{			
			try {				
				image = new File(resources_books + book.getID() + "\\" + mFile.getOriginalFilename());				
				image.createNewFile();
				mFile.transferTo(image);
			} catch (IOException e) 
			{				
				e.printStackTrace();
			}
			}
			else if(!mFile.isEmpty()) 
			{
				try 
				{
				File pdf = new File(resources_books + book.getID() + "\\" + pdfFile.getOriginalFilename());
				pdf.createNewFile();
				pdfFile.transferTo(pdf);
				}catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			return "redirect:/book/allBooks";
		}
	}
	
	@GetMapping(value="/allBooks")
	public String allBooks(@RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "4") Integer size,
			@RequestParam(defaultValue = "title") String sort, @RequestParam(required = false) String findBy,@RequestParam (required = false) String findKey, Model model, HttpServletRequest request)
	{
		Page<Book>books;
		System.out.println(request.getParameter("findReq") + " " + request.getParameter("findBy"));
		if(request.getParameter("findReq")!=null)
		{	
			sort = "title";
			findBy=null;
			findKey=null;
		}
		System.out.println(findBy + " " + findKey);
		if((findBy!=null) && (findKey!=null)) 
		{
			books = bookService.findBooks(pageNo-1, size, sort, findBy, findKey);					
			model.addAttribute("findBy", findBy);
			model.addAttribute("findKey", findKey);			
		}
		else
		{
			books = bookService.getBooks(pageNo-1, size, sort);				
		}		
		model.addAttribute("sort", sort);
		model.addAttribute("books", books);
		if(books.getTotalPages()>0) 
		{
			System.out.println("Pages: " + books.getTotalPages() + " " + books.getNumber());
			List<Integer> pageNumbers = IntStream.rangeClosed(1, books.getTotalPages()).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}		
		return "book/allBooks";
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
		return "book/editBookForm";
	}
	
	@PostMapping(value="/editBook/{id}")
	public String editBook(@PathVariable("id") Integer id, @Valid @ModelAttribute("book")Book book, BindingResult bindingResult, @RequestPart("file")MultipartFile mfile,@RequestPart("pdf")MultipartFile pdfFile, Model model) 
	{	
		book.setID(id);
		String pdfName = bookRepo.findByID(id).get().getPdfName();
		String fileName = bookRepo.findByID(id).get().getFileName();
		System.out.println("mfile: " +  mfile.isEmpty() + " pdffile: " + pdfFile.isEmpty() + " " + book.getFileName() + " " + book.getPdfName());
		if(bindingResult.hasErrors()) 
		{
			return "book/editBookForm";
		}
		else 
		{				
				File file = new File(resources_books + book.getID());				
				if(!file.exists()) file.mkdirs();
					if(!mfile.isEmpty())
					{
						System.out.println("Mfile: " + mfile.isEmpty() + "  " + mfile.getSize() + book.getPdfName());
						File delete = new File(resources_books + book.getID() + "\\" + book.getFileName());
						if(delete.exists()) delete.delete();
						book.setFileName(mfile.getOriginalFilename());
						if(pdfFile.isEmpty())book.setPdfName(pdfName);
						file = new File(resources_books + book.getID() + "\\" + mfile.getOriginalFilename());
						try
						{
							file.createNewFile();
							mfile.transferTo(file);
						} catch (IOException e) {					
							e.printStackTrace();
						}
					}
					if(!pdfFile.isEmpty())
					{
						System.out.println("pdfFile: " + pdfFile.isEmpty() + "  " + pdfFile.getSize());
						File delete = new File(resources_books + book.getID() + "\\" + book.getPdfName());
						if(delete.exists()) delete.delete();
						book.setPdfName(pdfFile.getOriginalFilename());
						if(mfile.isEmpty())book.setFileName(fileName);
						File pdf = new File(resources_books + book.getID() + "\\"  + pdfFile.getOriginalFilename());
						try
						{
							pdf.createNewFile();
							pdfFile.transferTo(pdf);					
						} catch (IOException e) {					
							e.printStackTrace();
						}																	
					}
					if(pdfFile.isEmpty() && mfile.isEmpty()) 
					{
						book.setFileName(fileName);
						book.setPdfName(pdfName);
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
		return "redirect:/book/allBooks?sort=title&pageNo=1&size=4";
	}
	
	@GetMapping(value = "/readBook/{id}")
	public String readBook(@PathVariable Integer id, Model model) 
	{
		EntityManager manager = dbService.openDBConnection();
		Book book = (Book) manager.createQuery("select b from Book b where id=" + id).getSingleResult();
		model.addAttribute("book", book);
		dbService.closeDBConnection(manager);
		return "book/readBook";
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
	
	@GetMapping(value = "/addBookToAuthorForm")
	public String addBookToAuthorForm(Model model) 
	{
		List<Book> books = bookRepo.findAll();
		List<Author> authors = authorRepo.findAll();
		model.addAttribute("authors", authors);
		model.addAttribute("books", books);
		return "book/addBookToAuthorForm";
	}
	
	@PostMapping(value="/addBookToAuthor")
	public String addBookToAuthor(@RequestParam("authorID") Integer authorID, @RequestParam("bookID") Integer bookID ) 
	{
		Book book = bookRepo.findByID(bookID).get();
		Author author = authorRepo.findByID(authorID);
		author.addBook(book);
		book.addAuthor(author);
		EntityManager manager = dbService.openDBConnection();
		manager.merge(author);
		manager.merge(book);
		dbService.closeDBConnection(manager);
		return "redirect:/author/allAuthors";
	}
	
	@GetMapping(value = "/addBookToPublisherForm")
	public String addBookToPublisherForm(Model model) 
	{
		List<Book> books = bookRepo.findAll();
		List<Publisher> publishers = publisherRepo.findAll();
		model.addAttribute("publishers", publishers);
		model.addAttribute("books", books);
		return "book/addBookToPublisherForm";
	}
	
	@PostMapping(value="/addBookToPublisher")
	public String addBookToPublisher(@RequestParam("publisherID") Integer publisherID, @RequestParam("bookID") Integer bookID ) 
	{
		Book book = bookRepo.findByID(bookID).get();
		Publisher publisher = publisherRepo.findByID(publisherID);
		publisher.addBook(book);
		book.addPublisher(publisher);
		EntityManager manager = dbService.openDBConnection();
		manager.merge(publisher);
		manager.merge(book);
		dbService.closeDBConnection(manager);
		return "redirect:/author/allAuthors";
	}
	

}
