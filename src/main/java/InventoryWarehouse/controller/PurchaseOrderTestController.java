
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.PurchaseOrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PurchaseOrderTestController {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderTestController(
            PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @GetMapping("/purchase-orders-test")
    public List<Map<String, Object>> getPurchaseOrders() {

        return purchaseOrderRepository.findAll()
                .stream()
                .map(order -> Map.<String, Object>of(
                        "purchaseOrderId", order.getPurchaseOrderId(),
                        "supplierName", order.getSupplier().getSupplierName(),
                        "warehouseName", order.getWarehouse().getWarehouseName(),
                        "orderDate", order.getOrderDate() == null
                                ? "" : order.getOrderDate().toString(),
                        "expectedDate", order.getExpectedDate() == null
                                ? "" : order.getExpectedDate().toString(),
                        "status", order.getStatus().name(),
                        "notes", order.getNotes() == null
                                ? "" : order.getNotes()
                ))
                .toList();
    }
}
