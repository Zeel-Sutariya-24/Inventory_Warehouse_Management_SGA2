
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.PurchaseOrderItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PurchaseOrderItemTestController {

    private final PurchaseOrderItemRepository itemRepository;

    public PurchaseOrderItemTestController(
            PurchaseOrderItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/purchase-order-items-test")
    public List<Map<String, Object>> getPurchaseOrderItems() {

        return itemRepository.findAll()
                .stream()
                .map(item -> Map.<String, Object>of(
                        "purchaseOrderItemId",
                        item.getPurchaseOrderItemId(),

                        "purchaseOrderId",
                        item.getPurchaseOrder().getPurchaseOrderId(),

                        "productName",
                        item.getProduct().getProductName(),

                        "sku",
                        item.getProduct().getSku(),

                        "quantityOrdered",
                        item.getQuantityOrdered(),

                        "unitCost",
                        item.getUnitCost(),

                        "quantityReceived",
                        item.getQuantityReceived()
                ))
                .toList();
    }
}
