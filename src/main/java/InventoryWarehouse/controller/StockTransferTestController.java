
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.StockTransferRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StockTransferTestController {

    private final StockTransferRepository transferRepository;

    public StockTransferTestController(
            StockTransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @GetMapping("/stock-transfers-test")
    public List<Map<String, Object>> getStockTransfers() {

        return transferRepository.findAll()
                .stream()
                .map(transfer -> Map.<String, Object>of(
                        "transferId",
                        transfer.getTransferId(),

                        "sourceWarehouse",
                        transfer.getSourceWarehouse().getWarehouseName(),

                        "destinationWarehouse",
                        transfer.getDestinationWarehouse().getWarehouseName(),

                        "requestedAt",
                        transfer.getRequestedAt() == null
                                ? "" : transfer.getRequestedAt().toString(),

                        "transferredAt",
                        transfer.getTransferredAt() == null
                                ? "" : transfer.getTransferredAt().toString(),

                        "status",
                        transfer.getStatus().name(),

                        "requestedBy",
                        transfer.getRequestedBy()
                ))
                .toList();
    }
}
