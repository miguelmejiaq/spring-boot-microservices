package com.miguelmejiaq.microservices.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguelmejiaq.microservices.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory,Long>{
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
