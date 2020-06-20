package com.zbodya.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
import com.zbodya.Model.Publisher;
import com.zbodya.Model.Repositories.AuthorRepository;
import com.zbodya.Model.Repositories.BookRepository;
import com.zbodya.Model.Repositories.PublisherRepository;
import com.zbodya.Service.DBService;
import com.zbodya.Service.PublisherService;

@Controller()
@RequestMapping(value = "/publisher")
public class PublisherController 
{
	
	@Autowired
	PublisherRepository publisherRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	DBService dbService;
	
	private String resources_publishers = "C:\\Users\\tempadmin2\\git\\book_store_repository\\book_store\\src\\main\\resources\\static\\images\\publishers\\";


	@GetMapping(value = "/addPublisherForm")
	public String addAuthorForm(Publisher publisher) 
	{
		return "publisher/addPublisherForm";
	}
	
	@PostMapping(value = "/addPublisher")
	public String addPublisher(@ModelAttribute("publisher")@Valid Publisher publisher,BindingResult bindingResult,@RequestPart("file")MultipartFile mFile, Model model) 
	{		
		publisher.setImage(mFile.getOriginalFilename());		
		publisher = publisherRepo.saveAndFlush(publisher);
		List<Publisher>publishers = publisherRepo.findAll();			
		model.addAttribute("publishers", publishers);
		if(bindingResult.hasErrors()) 
		{
			return "publisher/addPublisherForm";
		}
		else 
		{	
			if(!mFile.isEmpty())
			{
			File image = new File(resources_publishers + publisher.getID());			
			try {
				image.mkdirs();
				image = new File(resources_publishers + publisher.getID() + "\\" + mFile.getOriginalFilename());				
				image.createNewFile();
				mFile.transferTo(image);
			} catch (IOException e) 
			{				
				e.printStackTrace();
			}
			}				
			return "redirect:/publisher/allPublishers";
		}
	}
	
	@GetMapping(value="/allPublishers")
	public String allPublishers(@RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "4") Integer size,
			@RequestParam(defaultValue = "name") String sort, @RequestParam(required = false) String findBy,@RequestParam (required = false) String findKey, Model model, HttpServletRequest request)
	{
		Page<Publisher>publishers;
		if(request.getParameter("findReq")!=null)
		{	
			sort = "name";
			findBy=null;
			findKey=null;
		}
		if((findBy!=null) && (findKey!=null)) 
		{
			publishers = publisherService.findPublishers(pageNo-1, size, sort, findBy, findKey);					
			model.addAttribute("findBy", findBy);
			model.addAttribute("findKey", findKey);			
		}
		else
		{
			publishers = publisherService.getPublishers(pageNo-1, size, sort);				
		}		
		model.addAttribute("sort", sort);
		model.addAttribute("publishers", publishers);
		if(publishers.getTotalPages()>0) 
		{
//			System.out.println("Pages: " + authors.getTotalPages() + " " + authors.getNumber());
			List<Integer> pageNumbers = IntStream.rangeClosed(1, publishers.getTotalPages()).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}		
		return "publisher/allPublishers";
	}
	
	@GetMapping(value = "/editPublisherForm/{id}")
	public String editPublisherForm(@PathVariable("id")Integer id, Model model) 
	{				
		EntityManager manager = dbService.openDBConnection();		
		Publisher publisher = (Publisher) manager.createQuery("select p from Publisher p where id=" + id).getSingleResult();		
		model.addAttribute("publisher", publisher);
		dbService.closeDBConnection(manager);
		return "publisher/editPublisherForm";
	}
	
	@PostMapping(value="/editPublisher/{id}")
	public String editAuthor(@PathVariable("id") Integer id, @Valid @ModelAttribute("publisher")Publisher publisher, BindingResult bindingResult, @RequestPart("file")MultipartFile mfile, Model model) 
	{			
		String fileName = publisherRepo.findByID(id).getImage();
		publisher.setID(id);
		System.out.println("ID: " + publisher.getID());
		if(bindingResult.hasErrors()) 
		{
			return "publisher/editPublisherForm";
		}
		else 
		{				
				File file = new File(resources_publishers + publisher.getID());				
				if(!file.exists()) file.mkdirs();
					if(!mfile.isEmpty())
					{
						System.out.println("Mfile: " + mfile.isEmpty() + "  " + mfile.getSize() + publisher.getImage());
						File delete = new File(resources_publishers + publisher.getID() + "\\" + publisher.getImage());
						if(delete.exists()) delete.delete();
						publisher.setImage(mfile.getOriginalFilename());						
						file = new File(resources_publishers + publisher.getID() + "\\" + mfile.getOriginalFilename());
						try
						{
							file.createNewFile();
							mfile.transferTo(file);
						} catch (IOException e) {					
							e.printStackTrace();
						}
					}					
			EntityManager manager = dbService.openDBConnection();				
			manager.merge(publisher);					
			dbService.closeDBConnection(manager);
			return "redirect:/publisher/allPublishers";
		}
	}
	
	@GetMapping(value="/deletePublisher/{id}")
	public String deleteAuthor(@PathVariable Integer id) 
	{
		EntityManager manager = dbService.openDBConnection();
		Publisher publisher = (Publisher) manager.createQuery("select p from Publisher p where id=" + id).getSingleResult();
		File file = new File(resources_publishers + publisher.getID());
		file.delete();
		manager.remove(publisher);			
		dbService.closeDBConnection(manager);
		return "redirect:/publisher/allPublishers";
	}
	
	@GetMapping(value = "/publisherInfo/{id}")
	public String readAuthor(@PathVariable Integer id, Model model) 
	{
		EntityManager manager = dbService.openDBConnection();
		Publisher publisher = (Publisher) manager.createQuery("select p from Publisher p where id=" + id).getSingleResult();
		model.addAttribute("publisher", publisher);
		List<Book> books = bookRepo.findByPublishersID(publisher.getID());
		model.addAttribute("books", books);
		List<Author> authors = authorRepo.findByPublishersID(publisher.getID());
		model.addAttribute("authors", authors);
		dbService.closeDBConnection(manager);
		return "publisher/publisherInfo";
	}
	
	@GetMapping(value = "/addAuthorToPublisherForm")
	public String addBookToPublisherForm(Model model) 
	{
		List<Author> authors = authorRepo.findAll();
		List<Publisher> publishers = publisherRepo.findAll();
		model.addAttribute("publishers", publishers);
		model.addAttribute("authors", authors);
		return "publisher/addAuthorToPublisherForm";
	}
	
	@PostMapping(value="/addAuthorToPublisher")
	public String addBookToPublisher(@RequestParam("publisherID") Integer publisherID, @RequestParam("authorID") Integer authorID ) 
	{
		Author author = authorRepo.findByID(authorID);
		Publisher publisher = publisherRepo.findByID(publisherID);
		publisher.addAuthor(author);
		author.addPublisher(publisher);
		EntityManager manager = dbService.openDBConnection();
		manager.merge(publisher);
		manager.merge(author);
		dbService.closeDBConnection(manager);
		return "redirect:/publisher/allPublishers";
	}
}
