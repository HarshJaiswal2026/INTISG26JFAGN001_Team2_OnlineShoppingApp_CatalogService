package com.cognizant.catalog_service.dto.request;

public record CategoryRequest(
        String name,
        String description,
        Long parentId // To support hierarchy
) {}
