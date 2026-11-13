package InventoryWarehouse.controller;

import InventoryWarehouse.service.StockLookupService;
import InventoryWarehouse.service.WarehouseService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StockLookupPageController {

    private final WarehouseService warehouseService;

    public StockLookupPageController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/stock-lookup")
    public String showStockLookup(Model model) {

        model.addAttribute("warehouses",
                warehouseService.getAllWarehouses());

        return "stock-lookup/index";
    }
}