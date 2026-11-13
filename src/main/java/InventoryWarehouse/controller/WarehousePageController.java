
package InventoryWarehouse.controller;

import InventoryWarehouse.dto.WarehouseForm;
import InventoryWarehouse.model.Warehouse;
import InventoryWarehouse.service.WarehouseService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WarehousePageController {

    private final WarehouseService warehouseService;

    public WarehousePageController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/warehouses")
    public String showWarehouses(Model model) {
        model.addAttribute(
                "warehouses", warehouseService.getAllWarehouses());
        return "warehouses/index";
    }

    @GetMapping("/warehouses/new")
    public String showCreateForm(Model model) {
        model.addAttribute("warehouseForm", new WarehouseForm());
        return "warehouses/create";
    }

    @PostMapping("/warehouses/save")
    public String saveWarehouse(
            @Valid @ModelAttribute("warehouseForm") WarehouseForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "warehouses/create";
        }

        try {
            warehouseService.addWarehouse(form);

            redirectAttributes.addFlashAttribute(
                    "successMessage", "Warehouse added successfully.");

            return "redirect:/warehouses";

        } catch (DataIntegrityViolationException e) {
            result.reject("warehouse.duplicate",
                    "A warehouse with this name may already exist.");
            return "warehouses/create";
        }
    }

    @GetMapping("/warehouses/edit/{id}")
    public String showEditForm(
            @PathVariable Integer id, Model model) {

        Warehouse warehouse = warehouseService.getWarehouseById(id);

        WarehouseForm form = new WarehouseForm();
        form.setWarehouseName(warehouse.getWarehouseName());
        form.setAddress(warehouse.getAddress());
        form.setManagerName(warehouse.getManagerName());
        form.setActive(warehouse.getActive());

        model.addAttribute("warehouseForm", form);
        model.addAttribute("warehouseId", id);

        return "warehouses/edit";
    }

    @PostMapping("/warehouses/update/{id}")
    public String updateWarehouse(
            @PathVariable Integer id,
            @Valid @ModelAttribute("warehouseForm") WarehouseForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("warehouseId", id);
            return "warehouses/edit";
        }

        try {
            warehouseService.updateWarehouse(id, form);

            redirectAttributes.addFlashAttribute(
                    "successMessage", "Warehouse updated successfully.");

            return "redirect:/warehouses";

        } catch (IllegalArgumentException e) {
            result.reject("warehouse.error", e.getMessage());
            model.addAttribute("warehouseId", id);
            return "warehouses/edit";

        } catch (DataIntegrityViolationException e) {
            result.reject("warehouse.duplicate",
                    "A warehouse with this name may already exist.");
            model.addAttribute("warehouseId", id);
            return "warehouses/edit";
        }
    }

    @PostMapping("/warehouses/delete/{id}")
    public String deleteWarehouse(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            warehouseService.deleteWarehouse(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage", "Warehouse deleted successfully.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This warehouse cannot be deleted because existing inventory, storage locations, transfers or dispatches reference it.");
        }

        return "redirect:/warehouses";
    }
}
