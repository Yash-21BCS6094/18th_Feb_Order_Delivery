package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(UUID productId, ProductDTO productDTO);
    ProductDTO getProductById(UUID productId);
    Page<ProductDTO> getAllProduct(Pageable pageable);
    Page<ProductDTO> getProductSortedByPrice(Pageable pageable);
    List<ProductDTO> getProductByPriceRange(Double minPrice, Double maxPrice);
    void deleteProduct(UUID productId);
}