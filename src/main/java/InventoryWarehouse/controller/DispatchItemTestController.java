
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.DispatchItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DispatchItemTestController {

    private final DispatchItemRepository itemRepository;

    public DispatchItemTestController(
            DispatchItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/dispatch-items-test")
    public List<Map<String, Object>> getDispatchItems() {

        return itemRepository.findAll()
                .stream()
                .map(item -> Map.<String, Object>of(
                        "dispatchItemId",
                        item.getDispatchItemId(),

                        "dispatchOrderId",
                        item.getDispatchOrder().getDispatchOrderId(),

                        "customerReference",
                        item.getDispatchOrder().getCustomerReference(),

                        "productName",
                        item.getProduct().getProductName(),

                        "sku",
                        item.getProduct().getSku(),

                        "quantity",
                        item.getQuantity(),

                        "unitSalePrice",
                        item.getUnitSalePrice()
                ))
                .toList();
    }
}
