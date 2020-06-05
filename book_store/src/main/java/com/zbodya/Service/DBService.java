package com.zbodya.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Service;


@Service
public class DBService 
{

	public static EntityManagerFactory entityManagerFactory;
	public static EntityManager entityManager;
	
	public static EntityManager openDBConnection() 
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("com.zbodya");
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		return entityManager;
	}
	
	public static void closeDBConnection(EntityManager entityManager) 
	{
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
}
