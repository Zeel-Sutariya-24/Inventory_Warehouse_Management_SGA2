package InventoryWarehouse.controller;

import InventoryWarehouse.dto.StockValuationResponse;
import InventoryWarehouse.service.StockValuationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StockValuationController {

    private final StockValuationService valuationService;

    public StockValuationController(
            StockValuationService valuationService) {
        this.valuationService = valuationService;
    }

    @GetMapping("/stock-valuation")
    public String showStockValuation(Model model) {

        List<StockValuationResponse> records =
                valuationService.getStockValuation();

        BigDecimal totalValuation =
                valuationService.getTotalValuation(records);

        Map<String, BigDecimal> warehouseTotals =
                records.stream().collect(
                        Collectors.groupingBy(
                                StockValuationResponse::getWarehouseName,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        StockValuationResponse::getTotalValue,
                                        BigDecimal::add
                                )
                        )
                );

        model.addAttribute("records", records);
        model.addAttribute("totalValuation", totalValuation);
        model.addAttribute("warehouseTotals", warehouseTotals);

        return "stock-valuation/index";
    }
}