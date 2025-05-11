package ru.bsu.cad.lab.service;

import jakarta.persistence.EntityManager;
import ru.bsu.cad.lab.entity.Product;
import ru.bsu.cad.lab.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(EntityManager entityManager) {
        this.productRepository = new ProductRepository(entityManager);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
