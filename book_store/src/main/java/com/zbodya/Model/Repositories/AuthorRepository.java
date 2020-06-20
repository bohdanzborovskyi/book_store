package com.zbodya.Model.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zbodya.Model.*;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long>
{

	Page<Author> findByNameContainingIgnoreCase(Pageable pageable, String findKey);

	Page<Author> findByDescriptionContainingIgnoreCase(Pageable pageable, String findKey);
	
	Author findByID(Integer ID);

	List<Author> findByPublishersID(int id);
	
	
	
	

}
