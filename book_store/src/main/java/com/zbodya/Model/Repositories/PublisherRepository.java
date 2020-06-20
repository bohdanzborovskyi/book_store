package com.zbodya.Model.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zbodya.Model.*;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>
{

	Page<Publisher> findByNameContainingIgnoreCase(Pageable pageable, String findKey);

	Page<Publisher> findByDescriptionContainingIgnoreCase(Pageable pageable, String findKey);
	
	Publisher findByID(Integer publisherID);

}
