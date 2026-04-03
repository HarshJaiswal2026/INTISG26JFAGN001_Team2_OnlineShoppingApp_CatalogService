package com.cognizant.catalog_service.controller;

import com.cognizant.catalog_service.dto.response.InventoryResponse;
import com.cognizant.catalog_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/catalog/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Anyone can check stock (Customer, Seller, Admin)
    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStockForProduct(productId));
    }

    // Only Owner or Admin can update stock
    @PatchMapping("/{productId}/update")
    public ResponseEntity<InventoryResponse> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer amount,
            @RequestHeader("X-USER-ID") Long userId,      // Injected by Gateway
            @RequestHeader("X-USER-ROLE") String role) {  // Injected by Gateway

        inventoryService.updateStock(productId, amount, userId, role);

        Integer newQty = inventoryService.getStockForProduct(productId);
        return ResponseEntity.ok(new InventoryResponse(
                productId,
                newQty,
                "SUCCESS",
                "Stock adjusted successfully"
        ));
    }
}