
package InventoryWarehouse.service;

import InventoryWarehouse.dto.ProductForm;
import InventoryWarehouse.model.Category;
import InventoryWarehouse.model.Product;
import InventoryWarehouse.repository.CategoryRepository;
import InventoryWarehouse.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllWithCategory();
    }

    @Transactional
    public void addProduct(ProductForm form) {

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Selected category does not exist."));

        Product product = new Product();

        product.setSku(form.getSku().trim());
        product.setProductName(form.getProductName().trim());
        product.setCategory(category);
        product.setUnitOfMeasure(form.getUnitOfMeasure().trim());
        product.setUnitCost(form.getUnitCost());
        product.setReorderLevel(form.getReorderLevel());
        product.setActive(form.getActive() != null ? form.getActive() : false);
        product.setBarcode(
                form.getBarcode() == null || form.getBarcode().isBlank()
                        ? null
                        : form.getBarcode().trim());

        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Integer productId, ProductForm form) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Selected category does not exist."));

        product.setSku(form.getSku().trim());
        product.setProductName(form.getProductName().trim());
        product.setCategory(category);
        product.setUnitOfMeasure(form.getUnitOfMeasure().trim());
        product.setUnitCost(form.getUnitCost());
        product.setReorderLevel(form.getReorderLevel());
        product.setActive(form.getActive() != null ? form.getActive() : false);
        product.setBarcode(
                form.getBarcode() == null || form.getBarcode().isBlank()
                        ? null
                        : form.getBarcode().trim());

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        productRepository.delete(product);

        // Execute the delete immediately so database relationship
        // restrictions are detected before this method returns.
        productRepository.flush();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
