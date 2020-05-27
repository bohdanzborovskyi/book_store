package com.zbodya.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zbodya.Model.*;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>
{

}
