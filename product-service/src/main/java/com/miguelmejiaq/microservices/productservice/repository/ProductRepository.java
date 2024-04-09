package com.miguelmejiaq.microservices.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.miguelmejiaq.microservices.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
