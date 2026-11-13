
package InventoryWarehouse.controller;

import InventoryWarehouse.service.WarehouseService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final WarehouseService warehouseService;

    public PageController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        return "dashboard/index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        return "dashboard/index";
    }
}
