package com.cognizant.catalog_service.dto.request;

import java.math.BigDecimal;
import java.util.Map;

public record ProductRequest(
        String sku,
        String title,
        String description,
        BigDecimal price,
        String currency,
        Long categoryId,
        Integer initialQuantity,
        Map<String, Object> attributes
) {}