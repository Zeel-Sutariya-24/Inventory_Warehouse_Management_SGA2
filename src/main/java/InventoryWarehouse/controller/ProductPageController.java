
package InventoryWarehouse.controller;

import InventoryWarehouse.dto.ProductForm;
import InventoryWarehouse.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import InventoryWarehouse.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import java.math.BigDecimal;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductPageController {

    private final ProductService productService;

    public ProductPageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/index";
    }

    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", productService.getAllCategories());

        return "products/create";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            @Valid @ModelAttribute("productForm") ProductForm productForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            return "products/create";
        }

        try {
            productService.addProduct(productForm);
            return "redirect:/products";

        } catch (IllegalArgumentException e) {
            bindingResult.reject("product.error", e.getMessage());
            model.addAttribute("categories", productService.getAllCategories());
            return "products/create";
        }
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Integer id, Model model) {

        Product product = productService.getAllProducts().stream()
                .filter(p -> p.getProductId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        ProductForm form = new ProductForm();
        form.setSku(product.getSku());
        form.setProductName(product.getProductName());
        form.setCategoryId(product.getCategory().getCategoryId());
        form.setUnitOfMeasure(product.getUnitOfMeasure());
        form.setUnitCost(product.getUnitCost());
        form.setReorderLevel(product.getReorderLevel());
        form.setActive(product.getActive());
        form.setBarcode(product.getBarcode());

        model.addAttribute("productForm", form);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("productId", id);

        return "products/edit";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(
            @PathVariable Integer id,
            @Valid @ModelAttribute("productForm") ProductForm productForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("productId", id);
            return "products/edit";
        }

        try {
            productService.updateProduct(id, productForm);
            return "redirect:/products";

        } catch (IllegalArgumentException e) {
            bindingResult.reject("product.error", e.getMessage());
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("productId", id);
            return "products/edit";
        }
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Product deleted successfully.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This product cannot be deleted because it is referenced by existing inventory or transaction records.");
        }

        return "redirect:/products";
    }

}
