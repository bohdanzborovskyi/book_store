package com.zbodya.Model.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.zbodya.Model.*;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{

	Optional<Book> findByID(Integer iD);
	Page<Book> findAll(Pageable pageable);
	Page<Book> findByTitle(Pageable pageable,String title);
	Page<Book> findByDescribtion(Pageable pageable,String describtion);

}
