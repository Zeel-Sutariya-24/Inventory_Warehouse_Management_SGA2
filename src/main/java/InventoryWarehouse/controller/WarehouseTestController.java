
package InventoryWarehouse.controller;

import InventoryWarehouse.model.Warehouse;
import InventoryWarehouse.repository.WarehouseRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WarehouseTestController {

    private final WarehouseRepository warehouseRepository;

    public WarehouseTestController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping("/warehouses-test")
    public List<Map<String, Object>> getWarehouses() {

        return warehouseRepository.findAll()
                .stream()
                .map(warehouse -> Map.<String, Object>of(
                        "warehouseId", warehouse.getWarehouseId(),
                        "warehouseName", warehouse.getWarehouseName(),
                        "address", warehouse.getAddress(),
                        "managerName", warehouse.getManagerName() == null
                                ? "" : warehouse.getManagerName(),
                        "active", warehouse.getActive()
                ))
                .toList();
    }
}
