
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.StockMovementRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StockMovementTestController {

    private final StockMovementRepository movementRepository;

    public StockMovementTestController(
            StockMovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @GetMapping("/stock-movements-test")
    public List<Map<String, Object>> getStockMovements() {

        List<Map<String, Object>> result = new ArrayList<>();

        movementRepository.findAll().forEach(movement -> {

            Map<String, Object> item = new HashMap<>();

            item.put("movementId", movement.getMovementId());
            item.put("productName",
                    movement.getProduct().getProductName());
            item.put("sku", movement.getProduct().getSku());
            item.put("warehouseName",
                    movement.getWarehouse().getWarehouseName());
            item.put("movementType",
                    movement.getMovementType().name());
            item.put("quantityChange", movement.getQuantityChange());

            item.put("referenceType",
                    movement.getReferenceType() == null
                            ? "" : movement.getReferenceType());

            item.put("referenceId",
                    movement.getReferenceId() == null
                            ? "" : movement.getReferenceId());

            item.put("movementTime",
                    movement.getMovementTime() == null
                            ? "" : movement.getMovementTime().toString());

            item.put("performedBy", movement.getPerformedBy());

            item.put("notes",
                    movement.getNotes() == null
                            ? "" : movement.getNotes());

            result.add(item);
        });

        return result;
    }
}
