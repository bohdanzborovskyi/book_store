package com.zbodya.Model;

import java.io.File;
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
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "book")
public class Book 
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	@Column(name = "id")
	private int ID;
	
	@Column(name = "publish_date")
	@Past
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate publishDate;
	
	@NotBlank
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String describtion;
	
	@ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
	@JoinTable(name="book_publisher",
			joinColumns = @JoinColumn(name="book_id"),
			inverseJoinColumns = @JoinColumn(name = "publisher_id"))		
	private List<Publisher> publishers;
	
	@ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
	@JoinTable(name = "book_author",
			   joinColumns = @JoinColumn(name="book_id"),
			   inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> authors;
	
	@Column(name = "image")
	private File image;
	
	public Book() {}

	public Book(LocalDate publishDate, String title, String describtion,File image) {
		super();
		this.publishDate = publishDate;
		this.title = title;
		this.describtion = describtion;
		this.image = image;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribtion() {
		return describtion;
	}

	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(ArrayList<Publisher> publishers) {
		this.publishers = publishers;
	}

	public List<Author> getAuthor() {
		return authors;
	}

	public void setAuthor(ArrayList<Author> authors) {
		this.authors = authors;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}
	
	public void addPublisher(Publisher publisher) 
	{
		if(publishers== null) 
		{
			publishers = new ArrayList<Publisher>();
		}
		publishers.add(publisher);
	}
	
	public void deletePublisher(Publisher publisher) 
	{
		publishers.remove(publisher);
	}
	
	public void addAuthor(Author author) 
	{
		if(authors== null) 
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
