package com.cognizant.catalog_service.service;

import com.cognizant.catalog_service.model.Inventory;
import com.cognizant.catalog_service.model.Product;
import com.cognizant.catalog_service.repository.InventoryRepository;
import com.cognizant.catalog_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Integer getStockForProduct(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }

    @Transactional
    public void updateStock(Long productId, Integer quantityChange, Long currentUserId, String role) {
        // 1. Fetch the Inventory record (which includes the Product object)
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for Product ID: " + productId));

        // 2. SECURITY CHECK: Verify Ownership
        // If the user is NOT an Admin AND the Product's SellerID doesn't match the Current User
        if (!role.equalsIgnoreCase("ADMIN") &&
                !inventory.getProduct().getSellerId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized: You do not own the product for this inventory!");
        }

        // 3. Logic Check: Prevent Negative Stock
        int newTotal = inventory.getQuantity() + quantityChange;
        if (newTotal < 0) {
            throw new RuntimeException("Insufficient stock! Available: " + inventory.getQuantity());
        }

        // 4. Update
        inventory.setQuantity(newTotal);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void createInitialInventory(Product product, Integer initialQuantity) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(initialQuantity);
        inventory.setLocation("Default Warehouse"); // Optional default
        inventoryRepository.save(inventory);
    }
}