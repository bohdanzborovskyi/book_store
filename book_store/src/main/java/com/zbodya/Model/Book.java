package com.zbodya.Model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
//	@Column(name = "publisher")
//	private ArrayList<Publisher> publishers;
//	
//	@Column(name = "author")
//	private Author author;
//	
//	@Column(name = "image")
//	private File image;
	
	public Book() {}

	public Book(LocalDate publishDate, String title, String describtion
//			, ArrayList<Publisher> publishers,
//			Author author, File image
			) {
		super();
		this.publishDate = publishDate;
		this.title = title;
		this.describtion = describtion;
//		this.publishers = publishers;
//		this.author = author;
//		this.image = image;
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

//	public ArrayList<Publisher> getPublishers() {
//		return publishers;
//	}
//
//	public void setPublishers(ArrayList<Publisher> publishers) {
//		this.publishers = publishers;
//	}
//
//	public Author getAuthor() {
//		return author;
//	}
//
//	public void setAuthor(Author author) {
//		this.author = author;
//	}
//
//	public File getImage() {
//		return image;
//	}
//
//	public void setImage(File image) {
//		this.image = image;
//	}
//	
	
}
