
package InventoryWarehouse.controller;

import InventoryWarehouse.dto.StorageLocationForm;
import InventoryWarehouse.model.StorageLocation;
import InventoryWarehouse.service.StorageLocationService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/storage-locations")
public class StorageLocationPageController {

    private final StorageLocationService locationService;

    public StorageLocationPageController(
            StorageLocationService locationService) {
        this.locationService = locationService;
    }

    private void addFormOptions(Model model) {
        model.addAttribute("warehouses", locationService.getAllWarehouses());
        model.addAttribute("locationTypes", locationService.getLocationTypes());
    }

    @GetMapping
    public String listLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        return "storage-locations/index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("locationForm", new StorageLocationForm());
        addFormOptions(model);
        return "storage-locations/create";
    }

    @PostMapping("/save")
    public String saveLocation(
            @Valid @ModelAttribute("locationForm") StorageLocationForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            addFormOptions(model);
            return "storage-locations/create";
        }

        try {
            locationService.addLocation(form);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Storage location added successfully.");
            return "redirect:/storage-locations";

        } catch (DataIntegrityViolationException ex) {
            addFormOptions(model);
            model.addAttribute("errorMessage",
                    "This location code may already exist in the selected warehouse.");
            return "storage-locations/create";

        } catch (RuntimeException ex) {
            addFormOptions(model);
            model.addAttribute("errorMessage",
                    "Could not save the storage location. Please check the selected warehouse.");
            return "storage-locations/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            StorageLocation location = locationService.getLocationById(id);

            StorageLocationForm form = new StorageLocationForm();
            form.setWarehouseId(location.getWarehouse().getWarehouseId());
            form.setLocationCode(location.getLocationCode());
            form.setLocationType(location.getLocationType());
            form.setActive(location.getActive());

            model.addAttribute("locationForm", form);
            model.addAttribute("locationId", id);
            addFormOptions(model);

            return "storage-locations/edit";

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Storage location not found.");
            return "redirect:/storage-locations";
        }
    }

    @PostMapping("/update/{id}")
    public String updateLocation(
            @PathVariable Integer id,
            @Valid @ModelAttribute("locationForm") StorageLocationForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("locationId", id);
            addFormOptions(model);
            return "storage-locations/edit";
        }

        try {
            locationService.updateLocation(id, form);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Storage location updated successfully.");
            return "redirect:/storage-locations";

        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("locationId", id);
            model.addAttribute("errorMessage",
                    "This location code may already exist in the selected warehouse.");
            addFormOptions(model);
            return "storage-locations/edit";

        } catch (RuntimeException ex) {
            model.addAttribute("locationId", id);
            model.addAttribute("errorMessage",
                    "Could not update the storage location. Please check the selected warehouse.");
            addFormOptions(model);
            return "storage-locations/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteLocation(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            locationService.deleteLocation(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Storage location deleted successfully.");

        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This location cannot be deleted because it is being used by stock records.");

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Could not delete the storage location.");
        }

        return "redirect:/storage-locations";
    }
}
