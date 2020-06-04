package com.zbodya.Model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Entity
@Table(name="author")
public class Author {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int ID;
	
	@Column(name = "birthday")
	@DateTimeFormat(iso = ISO.DATE)
	LocalDate birthday;
	
	@NotBlank
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@NotNull
	@Column(name = "image")
	private String image;
	
	@ManyToMany(mappedBy = "authors")
	private List<Book> books;
	
	@ManyToMany(mappedBy = "authors")
	private List<Publisher> publishers;
	
	public Author() {}
	

	public Author(LocalDate birthday, @NotBlank String name, String description, String image) {
		super();
		this.birthday = birthday;
		this.name = name;
		this.description = description;
		this.image = image;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(ArrayList<Publisher> publishers) {
		this.publishers = publishers;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	
	
}
