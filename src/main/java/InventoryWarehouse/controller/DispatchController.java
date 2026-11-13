
package InventoryWarehouse.controller;

import InventoryWarehouse.service.DispatchService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @PostMapping("/{dispatchOrderId}/complete")
    public ResponseEntity<Map<String, Object>> dispatchOrder(
            @PathVariable Integer dispatchOrderId) {

        Map<String, Object> response = new HashMap<>();

        try {
            dispatchService.dispatchOrder(dispatchOrderId);

            response.put("success", true);
            response.put("message", "Dispatch completed successfully.");
            response.put("dispatchOrderId", dispatchOrderId);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
