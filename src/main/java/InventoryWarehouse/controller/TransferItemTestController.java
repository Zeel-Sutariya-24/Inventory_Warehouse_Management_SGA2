
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.TransferItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TransferItemTestController {

    private final TransferItemRepository itemRepository;

    public TransferItemTestController(
            TransferItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/transfer-items-test")
    public List<Map<String, Object>> getTransferItems() {

        return itemRepository.findAll()
                .stream()
                .map(item -> Map.<String, Object>of(
                        "transferItemId",
                        item.getTransferItemId(),

                        "transferId",
                        item.getStockTransfer().getTransferId(),

                        "productName",
                        item.getProduct().getProductName(),

                        "sku",
                        item.getProduct().getSku(),

                        "quantity",
                        item.getQuantity()
                ))
                .toList();
    }
}
