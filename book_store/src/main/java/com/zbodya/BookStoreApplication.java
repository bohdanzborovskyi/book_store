package com.zbodya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@SpringBootApplication
public class BookStoreApplication {	
	

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
		
		
	}
	
	
	
	

}
