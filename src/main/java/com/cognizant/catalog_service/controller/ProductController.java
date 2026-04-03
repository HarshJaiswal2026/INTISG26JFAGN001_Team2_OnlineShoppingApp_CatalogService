package com.cognizant.catalog_service.controller;

import com.cognizant.catalog_service.dto.request.ProductRequest;
import com.cognizant.catalog_service.dto.response.ProductResponse;
import com.cognizant.catalog_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart("product") ProductRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("X-User-Id") Long sellerId // Assuming Gateway passes the Auth User ID
    ) {
        ProductResponse response = productService.createProduct(request, images, sellerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getSellerProducts(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role) {

        return ResponseEntity.ok(productService.updateProduct(id, request, userId, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-USER-ROLE") String role) {

        productService.deleteProduct(id, userId, role);
        return ResponseEntity.noContent().build();
    }
}