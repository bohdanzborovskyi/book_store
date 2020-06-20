package com.zbodya.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.zbodya.Model.Repositories.AuthorRepository;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Service.AuthorService;
import com.zbodya.Service.DBService;

@Controller
@RequestMapping(value="/author")
public class AuthorController
{
	@Autowired
	DBService dbService;
	
	@Autowired 
	AuthorRepository authorRepo;
	
	@Autowired 
	BookRepository bookRepo;
	
	@Autowired
	AuthorService authorService;
	
	private String resources_authors = "C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\authors\\";

	
	@GetMapping(value = "/addAuthorForm")
	public String addAuthorForm(Author author) 
	{
		return "author/addAuthorForm";
	}
	
	@PostMapping(value = "/addAuthor")
	public String addAuthor(@ModelAttribute("author")@Valid Author author,BindingResult bindingResult,@RequestPart("file")MultipartFile mFile, Model model) 
	{		
		author.setImage(mFile.getOriginalFilename());		
		author = authorRepo.saveAndFlush(author);
		List<Author>authors = authorRepo.findAll();			
		model.addAttribute("authors", authors);
		System.out.println("Errors: " + bindingResult.toString());
		if(bindingResult.hasErrors()) 
		{
			return "author/addAuthorForm";
		}
		else 
		{	
			if(!mFile.isEmpty())
			{
			File image = new File(resources_authors + author.getID());			
			try {
				image.mkdirs();
				image = new File(resources_authors + author.getID() + "\\" + mFile.getOriginalFilename());				
				image.createNewFile();
				mFile.transferTo(image);
			} catch (IOException e) 
			{				
				e.printStackTrace();
			}
			}				
			return "redirect:/author/allAuthors";
		}
	}
	
	@GetMapping(value="/allAuthors")
	public String allAuthors(@RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "4") Integer size,
			@RequestParam(defaultValue = "name") String sort, @RequestParam(required = false) String findBy,@RequestParam (required = false) String findKey, Model model, HttpServletRequest request)
	{
		Page<Author>authors;
		if(request.getParameter("findReq")!=null)
		{	
			sort = "name";
			findBy=null;
			findKey=null;
		}
		System.out.println(findBy + " " + findKey);
		if((findBy!=null) && (findKey!=null)) 
		{
			authors = authorService.findAuthors(pageNo-1, size, sort, findBy, findKey);					
			model.addAttribute("findBy", findBy);
			model.addAttribute("findKey", findKey);			
		}
		else
		{
			authors = authorService.getAuthors(pageNo-1, size, sort);				
		}		
		model.addAttribute("sort", sort);
		model.addAttribute("authors", authors);
		if(authors.getTotalPages()>0) 
		{
			System.out.println("Pages: " + authors.getTotalPages() + " " + authors.getNumber());
			List<Integer> pageNumbers = IntStream.rangeClosed(1, authors.getTotalPages()).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}		
		return "author/allAuthors";
	}
	
	@GetMapping(value = "/editAuthorForm/{id}")
	public String editAuthorForm(@PathVariable("id")Integer id, Model model) 
	{				
		EntityManager manager = dbService.openDBConnection();		
		Author author = (Author) manager.createQuery("select a from Author a where id=" + id).getSingleResult();		
		System.out.println(author.getID());
		model.addAttribute("author", author);
		dbService.closeDBConnection(manager);
		return "author/editAuthorForm";
	}
	
	@PostMapping(value="/editAuthor/{id}")
	public String editAuthor(@PathVariable("id") Integer id, @Valid @ModelAttribute("author")Author author, BindingResult bindingResult, @RequestPart("file")MultipartFile mfile, Model model) 
	{			
		String fileName = authorRepo.findByID(id).getImage();
		author.setID(id);
		System.out.println("ID: " + author.getID());
		if(bindingResult.hasErrors()) 
		{
			return "author/editAuthorForm";
		}
		else 
		{				
				File file = new File(resources_authors + author.getID());				
				if(!file.exists()) file.mkdirs();
					if(!mfile.isEmpty())
					{
						System.out.println("Mfile: " + mfile.isEmpty() + "  " + mfile.getSize() + author.getImage());
						File delete = new File(resources_authors + author.getID() + "\\" + author.getImage());
						if(delete.exists()) delete.delete();
						author.setImage(mfile.getOriginalFilename());						
						file = new File(resources_authors + author.getID() + "\\" + mfile.getOriginalFilename());
						try
						{
							file.createNewFile();
							mfile.transferTo(file);
						} catch (IOException e) {					
							e.printStackTrace();
						}
					}					
			EntityManager manager = dbService.openDBConnection();				
			manager.merge(author);					
			dbService.closeDBConnection(manager);
			return "redirect:/author/allAuthors";
		}
	}
	
	@GetMapping(value="/deleteAuthor/{id}")
	public String deleteAuthor(@PathVariable Integer id) 
	{
		EntityManager manager = dbService.openDBConnection();
		Author author = (Author) manager.createQuery("select a from Author a where id=" + id).getSingleResult();
		File file = new File(resources_authors + author.getID());
		file.delete();
		manager.remove(author);			
		dbService.closeDBConnection(manager);
		return "redirect:/author/allAuthors";
	}
	
	@GetMapping(value = "/authorInfo/{id}")
	public String readAuthor(@PathVariable Integer id, Model model) 
	{
		EntityManager manager = dbService.openDBConnection();
		Author author = (Author) manager.createQuery("select a from Author a where id=" + id).getSingleResult();
		model.addAttribute("author", author);
		List<Book> books = bookRepo.findByAuthorsID(author.getID());
		model.addAttribute("books", books);
		dbService.closeDBConnection(manager);
		return "author/authorInfo";
	}
	
}
