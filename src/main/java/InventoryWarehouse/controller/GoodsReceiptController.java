
package InventoryWarehouse.controller;

import InventoryWarehouse.dto.GoodsReceiptRequest;
import InventoryWarehouse.service.GoodsReceiptService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/goods-receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    public GoodsReceiptController(
            GoodsReceiptService goodsReceiptService) {
        this.goodsReceiptService = goodsReceiptService;
    }

    @PostMapping
    public ResponseEntity<?> receiveGoods(
            @RequestBody GoodsReceiptRequest request) {

        try {
            Integer receiptId =
                    goodsReceiptService.receiveGoods(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "message", "Goods received successfully.",
                            "goodsReceiptId", receiptId
                    ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }
}
