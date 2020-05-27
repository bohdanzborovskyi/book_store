package com.zbodya.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name="publisher")
public class Publisher
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int ID;
	
	@Column(name = "established")
	@DateTimeFormat(iso = ISO.DATE)
	LocalDate established;
	
	@NotBlank
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy = "publishers")
	private List<Book> books;
	
	@ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
	@JoinTable(name = "publisheer_author",
			   joinColumns = @JoinColumn(name = "publisher_id"),
			   inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> authors;
	
	public Publisher() {}

	public Publisher(String name, String description, LocalDate established) {
		super();
		this.name = name;
		this.description = description;
		this.established = established;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}

	public LocalDate getEstablished() {
		return established;
	}

	public void setEstablished(LocalDate established) {
		this.established = established;
	}
	
	public void addAuthor(Author author) 
	{
		if(authors == null) 
		{
			authors = new ArrayList<Author>();
		}
		authors.add(author);
	}
	
	public void deleteAuthor(Author author) 
	{
		authors.remove(author);
	}
	
	
	
}
