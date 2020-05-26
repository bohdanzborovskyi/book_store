package com.zbodya.Model;

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
	
	@Column(name = "books")
	private ArrayList<Book> books;
	
	@Column(name = "authors")
	private ArrayList<Author> authors;
	
	public Publisher() {}

	public Publisher(String name, String description, ArrayList<Book> books, ArrayList<Author> authors, LocalDate established) {
		super();
		this.name = name;
		this.description = description;
		this.books = books;
		this.authors = authors;
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

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public ArrayList<Author> getAuthors() {
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
	
	
	
}
