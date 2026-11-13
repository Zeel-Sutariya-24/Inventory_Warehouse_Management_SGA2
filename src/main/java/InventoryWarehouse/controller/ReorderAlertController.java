package InventoryWarehouse.controller;

import InventoryWarehouse.service.ReorderAlertService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReorderAlertController {

    private final ReorderAlertService reorderAlertService;

    public ReorderAlertController(ReorderAlertService reorderAlertService) {
        this.reorderAlertService = reorderAlertService;
    }

    @GetMapping("/reorder-alerts")
    public String showReorderAlerts(Model model) {

        model.addAttribute(
                "alerts",
                reorderAlertService.getReorderAlerts()
        );

        return "reorder-alerts/index";
    }
}