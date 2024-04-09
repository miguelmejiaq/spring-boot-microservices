package com.miguelmejiaq.microservices.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miguelmejiaq.microservices.inventoryservice.dto.InventoryResponse;
import com.miguelmejiaq.microservices.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> IsInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory -> 
                InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode()) 
                    .isInStock(inventory.getQuantity() > 0)
                    .build()
            ).toList();
    }
}
