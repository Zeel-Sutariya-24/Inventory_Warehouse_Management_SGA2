package InventoryWarehouse.controller;

import InventoryWarehouse.dto.StockLookupResponse;
import InventoryWarehouse.dto.WarehouseStockSummaryResponse;
import InventoryWarehouse.service.StockLookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/api/stock-lookup")
public class StockLookupController {

    private final StockLookupService stockLookupService;

    public StockLookupController(StockLookupService stockLookupService) {
        this.stockLookupService = stockLookupService;
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<List<StockLookupResponse>> getStockByWarehouse(
            @PathVariable Integer warehouseId) {

        if (warehouseId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                stockLookupService.getStockByWarehouse(warehouseId));
    }

    @GetMapping("/{warehouseId}/summary")
    public ResponseEntity<WarehouseStockSummaryResponse> getWarehouseSummary(
            @PathVariable Integer warehouseId) {

        if (warehouseId == null || warehouseId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                stockLookupService.getWarehouseSummary(warehouseId));
    }
}