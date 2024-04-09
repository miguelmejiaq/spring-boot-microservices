package com.miguelmejiaq.microservices.productservice;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelmejiaq.microservices.productservice.dto.ProductRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static GenericContainer<?> mongoDBContainer = new GenericContainer<>("mongo:latest")
										.withExposedPorts(27017)
										.withEnv("MONGO_INITDB_ROOT_USERNAME", "mongoadmin")
										.withEnv("MONGO_INITDB_ROOT_PASSWORD", "secret")
										.withEnv("MONGO_INITDB_DATABASE", "product-service");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongob.host", mongoDBContainer::getHost);
		dynamicPropertyRegistry.add("spring.data.mongodb.port",mongoDBContainer::getFirstMappedPort);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest request = GetProductRequest();	
		String productRequestString = objectMapper.writeValueAsString(request);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
			.contentType(MediaType.APPLICATION_JSON)
			.content(productRequestString)
		).andExpect(MockMvcResultMatchers.status().isCreated());
	}

	private ProductRequest GetProductRequest(){
		return ProductRequest.builder()
			.name("Iphone test")
			.description("Iphone Test Description")
			.price(BigDecimal.valueOf(800))
			.build();
	}
}
