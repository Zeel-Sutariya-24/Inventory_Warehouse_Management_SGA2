
package InventoryWarehouse.controller;

import InventoryWarehouse.dto.SupplierForm;
import InventoryWarehouse.model.Supplier;
import InventoryWarehouse.service.SupplierService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SupplierPageController {

    private final SupplierService supplierService;

    public SupplierPageController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/suppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "suppliers/index";
    }

    @GetMapping("/suppliers/new")
    public String showCreateForm(Model model) {
        model.addAttribute("supplierForm", new SupplierForm());
        return "suppliers/create";
    }

    @PostMapping("/suppliers/save")
    public String saveSupplier(
            @Valid @ModelAttribute("supplierForm") SupplierForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "suppliers/create";
        }

        try {
            supplierService.addSupplier(form);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Supplier added successfully.");
            return "redirect:/suppliers";

        } catch (DataIntegrityViolationException e) {
            result.reject("supplier.duplicate",
                    "Supplier phone or email may already exist.");
            return "suppliers/create";
        }
    }

    @GetMapping("/suppliers/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        Supplier supplier = supplierService.getSupplierById(id);

        SupplierForm form = new SupplierForm();
        form.setSupplierName(supplier.getSupplierName());
        form.setContactName(supplier.getContactName());
        form.setPhone(supplier.getPhone());
        form.setEmail(supplier.getEmail());
        form.setAddress(supplier.getAddress());
        form.setActive(supplier.getActive());

        model.addAttribute("supplierForm", form);
        model.addAttribute("supplierId", id);

        return "suppliers/edit";
    }

    @PostMapping("/suppliers/update/{id}")
    public String updateSupplier(
            @PathVariable Integer id,
            @Valid @ModelAttribute("supplierForm") SupplierForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("supplierId", id);
            return "suppliers/edit";
        }

        try {
            supplierService.updateSupplier(id, form);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Supplier updated successfully.");
            return "redirect:/suppliers";

        } catch (IllegalArgumentException e) {
            result.reject("supplier.error", e.getMessage());
            model.addAttribute("supplierId", id);
            return "suppliers/edit";

        } catch (DataIntegrityViolationException e) {
            result.reject("supplier.duplicate",
                    "Supplier phone or email may already exist.");
            model.addAttribute("supplierId", id);
            return "suppliers/edit";
        }
    }

    @PostMapping("/suppliers/delete/{id}")
    public String deleteSupplier(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Supplier deleted successfully.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This supplier cannot be deleted because existing purchase orders reference it.");
        }

        return "redirect:/suppliers";
    }
}
