
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.WarehouseStockRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WarehouseStockTestController {

    private final WarehouseStockRepository stockRepository;

    public WarehouseStockTestController(
            WarehouseStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping("/stock-test")
    public List<Map<String, Object>> getStock() {

        return stockRepository.findAllWithDetails()
                .stream()
                .map(stock -> Map.<String, Object>of(
                        "warehouseId", stock.getId().getWarehouseId(),
                        "productId", stock.getId().getProductId(),
                        "warehouseName",
                        stock.getWarehouse().getWarehouseName(),
                        "productName",
                        stock.getProduct().getProductName(),
                        "sku", stock.getProduct().getSku(),
                        "quantityOnHand", stock.getQuantityOnHand(),
                        "locationCode", stock.getLocation() == null
                                ? "" : stock.getLocation().getLocationCode()
                ))
                .toList();
    }
}
