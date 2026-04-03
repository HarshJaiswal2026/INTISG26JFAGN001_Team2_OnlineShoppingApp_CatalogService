package com.cognizant.catalog_service.dto.response;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {}