
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.GoodsReceiptRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GoodsReceiptTestController {

    private final GoodsReceiptRepository receiptRepository;

    public GoodsReceiptTestController(
            GoodsReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @GetMapping("/goods-receipts-test")
    public List<Map<String, Object>> getGoodsReceipts() {

        return receiptRepository.findAll()
                .stream()
                .map(receipt -> Map.<String, Object>of(
                        "goodsReceiptId",
                        receipt.getGoodsReceiptId(),

                        "purchaseOrderId",
                        receipt.getPurchaseOrder().getPurchaseOrderId(),

                        "receivedAt",
                        receipt.getReceivedAt() == null
                                ? "" : receipt.getReceivedAt().toString(),

                        "receivedBy",
                        receipt.getReceivedBy(),

                        "receiptNotes",
                        receipt.getReceiptNotes() == null
                                ? "" : receipt.getReceiptNotes()
                ))
                .toList();
    }
}
