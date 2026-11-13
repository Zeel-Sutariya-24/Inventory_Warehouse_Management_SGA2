
package InventoryWarehouse.controller;

import InventoryWarehouse.model.Product;
import InventoryWarehouse.repository.ProductRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ProductTestController {

    private final ProductRepository productRepository;

    public ProductTestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products-test")
    public List<Map<String, Object>> getProducts() {

        return productRepository.findAllWithCategory()
                .stream()
                .map(product -> Map.<String, Object>of(
                        "productId", product.getProductId(),
                        "sku", product.getSku(),
                        "productName", product.getProductName(),
                        "categoryName", product.getCategory().getCategoryName()
                ))
                .toList();
    }
}
