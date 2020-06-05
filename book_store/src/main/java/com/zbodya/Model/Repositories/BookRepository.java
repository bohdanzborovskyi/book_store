package com.zbodya.Model.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.zbodya.Model.*;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{

	Optional<Book> findByID(Integer iD);
	
}
