package com.zbodya.Model.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.zbodya.Model.*;

@Repository
public interface BookRepository extends CrudRepository<Book,Long>
{
	
}
