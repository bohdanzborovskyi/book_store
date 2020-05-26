package com.zbodya.Model;

import java.io.File;
import java.time.LocalDateTime;

public class Book 
{

	private int ID;
	
	private LocalDateTime publishDate;
	
	private String title;
	
	private String describtion;
	
	private Publisher publisher;
	
	private Author author;
	
	private File image;
}
