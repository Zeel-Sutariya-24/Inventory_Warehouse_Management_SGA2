
package InventoryWarehouse.service;

import InventoryWarehouse.model.StockTransfer;
import InventoryWarehouse.model.StockTransferStatus;
import InventoryWarehouse.model.TransferItem;
import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.model.WarehouseStockId;
import InventoryWarehouse.model.StockMovement;
import InventoryWarehouse.model.StockMovementType;

import InventoryWarehouse.repository.StockTransferRepository;
import InventoryWarehouse.repository.TransferItemRepository;
import InventoryWarehouse.repository.WarehouseStockRepository;
import InventoryWarehouse.repository.StockMovementRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final TransferItemRepository transferItemRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockTransferService(
            StockTransferRepository stockTransferRepository,
            TransferItemRepository transferItemRepository,
            WarehouseStockRepository warehouseStockRepository,
            StockMovementRepository stockMovementRepository) {

        this.stockTransferRepository = stockTransferRepository;
        this.transferItemRepository = transferItemRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public void completeTransfer(Integer transferId) {

        if (transferId == null || transferId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid transfer ID."
            );
        }

        // 1. Lock and retrieve the transfer
        StockTransfer transfer =
                stockTransferRepository.findByIdForUpdate(transferId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Stock transfer not found."
                                )
                        );

        // 2. Validate transfer status
        if (transfer.getStatus() != StockTransferStatus.REQUESTED
                && transfer.getStatus() != StockTransferStatus.IN_TRANSIT) {

            throw new IllegalArgumentException(
                    "Transfer must be REQUESTED or IN_TRANSIT."
            );
        }

        Integer sourceWarehouseId =
                transfer.getSourceWarehouse().getWarehouseId();

        Integer destinationWarehouseId =
                transfer.getDestinationWarehouse().getWarehouseId();

        // 3. Prevent transfers within the same warehouse
        if (sourceWarehouseId.equals(destinationWarehouseId)) {
            throw new IllegalArgumentException(
                    "Source and destination warehouses must be different."
            );
        }

        // 4. Lock all items belonging to this transfer
        List<TransferItem> items =
                transferItemRepository.findByTransferIdForUpdate(transferId);

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Transfer has no items."
            );
        }

        Set<Integer> processedProducts = new HashSet<>();

        // 5. Validate every item before changing stock
        for (TransferItem item : items) {

            Integer productId =
                    item.getProduct().getProductId();

            Integer quantity = item.getQuantity();

            if (productId == null || quantity == null || quantity <= 0) {
                throw new IllegalArgumentException(
                        "Every transfer item must have a product and positive quantity."
                );
            }

            if (!processedProducts.add(productId)) {
                throw new IllegalArgumentException(
                        "A product appears more than once in this transfer."
                );
            }

            WarehouseStockId sourceId =
                    new WarehouseStockId(sourceWarehouseId, productId);

            WarehouseStock sourceStock =
                    warehouseStockRepository.findByIdForUpdate(sourceId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Source warehouse stock record not found."
                                    )
                            );

            if (sourceStock.getQuantityOnHand() == null
                    || sourceStock.getQuantityOnHand() < quantity) {

                throw new IllegalArgumentException(
                        "Insufficient stock in source warehouse for product ID "
                                + productId
                );
            }

            WarehouseStockId destinationId =
                    new WarehouseStockId(destinationWarehouseId, productId);

            warehouseStockRepository.findByIdForUpdate(destinationId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Destination warehouse stock record not found."
                            )
                    );
        }

        // 6. Update stock and create movement records
        for (TransferItem item : items) {

            Integer productId =
                    item.getProduct().getProductId();

            Integer quantity = item.getQuantity();

            WarehouseStockId sourceId =
                    new WarehouseStockId(sourceWarehouseId, productId);

            WarehouseStock sourceStock =
                    warehouseStockRepository.findByIdForUpdate(sourceId)
                            .orElseThrow();

            WarehouseStockId destinationId =
                    new WarehouseStockId(destinationWarehouseId, productId);

            WarehouseStock destinationStock =
                    warehouseStockRepository.findByIdForUpdate(destinationId)
                            .orElseThrow();

            // Deduct from source
            sourceStock.setQuantityOnHand(
                    sourceStock.getQuantityOnHand() - quantity
            );

            // Add to destination
            destinationStock.setQuantityOnHand(
                    destinationStock.getQuantityOnHand() + quantity
            );

            warehouseStockRepository.save(sourceStock);
            warehouseStockRepository.save(destinationStock);

            // Record transfer out
            StockMovement outgoing = new StockMovement();
            outgoing.setProduct(item.getProduct());
            outgoing.setWarehouse(transfer.getSourceWarehouse());
            outgoing.setMovementType(StockMovementType.TRANSFER_OUT);
            outgoing.setQuantityChange(-quantity);
            outgoing.setReferenceType("STOCK_TRANSFER");
            outgoing.setReferenceId(transferId.longValue());
            outgoing.setPerformedBy("Inventory Supervisor");
            outgoing.setNotes("Stock transferred to warehouse ID "
                    + destinationWarehouseId);

            stockMovementRepository.save(outgoing);

            // Record transfer in
            StockMovement incoming = new StockMovement();
            incoming.setProduct(item.getProduct());
            incoming.setWarehouse(transfer.getDestinationWarehouse());
            incoming.setMovementType(StockMovementType.TRANSFER_IN);
            incoming.setQuantityChange(quantity);
            incoming.setReferenceType("STOCK_TRANSFER");
            incoming.setReferenceId(transferId.longValue());
            incoming.setPerformedBy("Inventory Supervisor");
            incoming.setNotes("Stock received from warehouse ID "
                    + sourceWarehouseId);

            stockMovementRepository.save(incoming);
        }

        // 7. Mark transfer as completed
        transfer.setStatus(StockTransferStatus.COMPLETED);
        transfer.setTransferredAt(LocalDateTime.now());

        stockTransferRepository.save(transfer);
    }
}
