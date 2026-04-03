package com.cognizant.catalog_service.dto.response;

public record InventoryResponse(
        Long productId,
        Integer currentQuantity,
        String status,
        String message
) {}