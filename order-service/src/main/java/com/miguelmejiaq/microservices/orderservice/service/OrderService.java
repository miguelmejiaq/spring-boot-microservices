package com.miguelmejiaq.microservices.orderservice.service;

import java.util.UUID;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.miguelmejiaq.microservices.orderservice.dto.InventoryResponse;
import com.miguelmejiaq.microservices.orderservice.dto.OrderRequest;
import com.miguelmejiaq.microservices.orderservice.event.OrderPlaceEvent;
import com.miguelmejiaq.microservices.orderservice.model.Order;
import com.miguelmejiaq.microservices.orderservice.model.OrderLineItems;
import com.miguelmejiaq.microservices.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String,OrderPlaceEvent> kafkaTemplate;

    public String PlaceOrder(OrderRequest orderRequest){
        Order order  = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(
            this.modelMapper.map(
                orderRequest.getOrderLineItemsDtoList(), 
                new TypeToken<List<OrderLineItems>>() {}.getType() 
                ));
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        InventoryResponse[] result = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean isInStock = Arrays.stream(result).allMatch(InventoryResponse::isInStock);

        if (isInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlaceEvent(order.getOrderNumber()));
            return "Order placed successfully";
        }
        else {
            throw new IllegalArgumentException("Product is not in the stock");
        }
    }
}
