package com.miguelmejiaq.microservices.productservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.miguelmejiaq.microservices.productservice.dto.ProductRequest;
import com.miguelmejiaq.microservices.productservice.dto.ProductResponse;
import com.miguelmejiaq.microservices.productservice.model.Product;
import com.miguelmejiaq.microservices.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper; 

    public ProductResponse CreateProduct (ProductRequest productRequest){
        Product product = this.modelMapper.map(productRequest, Product.class);
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        return this.modelMapper.map(product, ProductResponse.class);
    }

    public List<ProductResponse> ListProducts (){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::MapToProduct).toList();
    }

    private ProductResponse MapToProduct(Product product){
        return this.modelMapper.map(product, ProductResponse.class);
    }
}
