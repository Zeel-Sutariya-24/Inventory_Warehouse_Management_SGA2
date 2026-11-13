
package InventoryWarehouse.controller;

import InventoryWarehouse.service.StockTransferService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-transfers")
public class StockTransferController {

    private final StockTransferService stockTransferService;

    public StockTransferController(
            StockTransferService stockTransferService) {
        this.stockTransferService = stockTransferService;
    }

    @PostMapping("/{transferId}/complete")
    public ResponseEntity<Map<String, Object>> completeTransfer(
            @PathVariable Integer transferId) {

        Map<String, Object> response = new HashMap<>();

        try {
            stockTransferService.completeTransfer(transferId);

            response.put("success", true);
            response.put("message", "Stock transfer completed successfully.");
            response.put("transferId", transferId);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
