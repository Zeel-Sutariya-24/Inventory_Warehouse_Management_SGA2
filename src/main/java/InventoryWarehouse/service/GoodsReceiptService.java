
package InventoryWarehouse.service;

import InventoryWarehouse.dto.GoodsReceiptRequest;
import InventoryWarehouse.model.*;
import InventoryWarehouse.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsReceiptService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockMovementRepository stockMovementRepository;

    public GoodsReceiptService(
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderItemRepository purchaseOrderItemRepository,
            GoodsReceiptRepository goodsReceiptRepository,
            WarehouseStockRepository warehouseStockRepository,
            StockMovementRepository stockMovementRepository) {

        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public Integer receiveGoods(GoodsReceiptRequest request) {

        // 1. Validate the request
        if (request == null
                || request.getPurchaseOrderId() == null) {
            throw new IllegalArgumentException(
                    "Purchase order ID is required.");
        }

        if (request.getReceivedBy() == null
                || request.getReceivedBy().isBlank()
                || request.getReceivedBy().length() > 120) {
            throw new IllegalArgumentException(
                    "A valid receiver name is required (maximum 120 characters).");
        }

        if (request.getReceiptNotes() != null
                && request.getReceiptNotes().length() > 500) {
            throw new IllegalArgumentException(
                    "Receipt notes cannot exceed 500 characters.");
        }

        if (request.getItems() == null
                || request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one received item is required.");
        }

        // 2. Lock and retrieve the purchase order
        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findByIdForUpdate(
                        request.getPurchaseOrderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Purchase order not found."));

        if (purchaseOrder.getStatus() != PurchaseOrderStatus.PLACED
                && purchaseOrder.getStatus()
                != PurchaseOrderStatus.PARTIALLY_RECEIVED) {

            throw new IllegalArgumentException(
                    "Only PLACED or PARTIALLY_RECEIVED orders "
                            + "can receive goods.");
        }

        // 3. Lock all items belonging to this purchase order
        List<PurchaseOrderItem> orderItems =
                purchaseOrderItemRepository
                        .findByPurchaseOrderIdForUpdate(
                                purchaseOrder.getPurchaseOrderId());

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "This purchase order has no items.");
        }

        Map<Integer, PurchaseOrderItem> itemMap =
                new HashMap<>();

        for (PurchaseOrderItem item : orderItems) {
            itemMap.put(
                    item.getPurchaseOrderItemId(),
                    item);
        }

        // 4. Validate submitted quantities
        Set<Integer> processedItemIds = new HashSet<>();

        for (GoodsReceiptRequest.ReceivedItem receivedItem
                : request.getItems()) {

            if (receivedItem == null
                    || receivedItem.getPurchaseOrderItemId() == null
                    || receivedItem.getQuantityReceived() == null) {

                throw new IllegalArgumentException(
                        "Every received item must have an item ID "
                                + "and quantity.");
            }

            Integer itemId =
                    receivedItem.getPurchaseOrderItemId();

            Integer quantity =
                    receivedItem.getQuantityReceived();

            if (!processedItemIds.add(itemId)) {
                throw new IllegalArgumentException(
                        "Duplicate purchase order item: " + itemId);
            }

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Received quantity must be positive.");
            }

            PurchaseOrderItem orderItem = itemMap.get(itemId);

            if (orderItem == null) {
                throw new IllegalArgumentException(
                        "Item " + itemId
                                + " does not belong to this purchase order.");
            }

            int ordered = orderItem.getQuantityOrdered();
            int alreadyReceived = orderItem.getQuantityReceived();

            if (alreadyReceived + quantity > ordered) {
                throw new IllegalArgumentException(
                        "Received quantity exceeds outstanding "
                                + "quantity for item " + itemId + ".");
            }
        }

        // 5. Create the goods receipt record
        GoodsReceipt receipt = new GoodsReceipt();

        receipt.setPurchaseOrder(purchaseOrder);
        receipt.setReceivedBy(request.getReceivedBy().trim());
        receipt.setReceiptNotes(request.getReceiptNotes());

        receipt = goodsReceiptRepository.saveAndFlush(receipt);

        // 6. Update stock, order items and movement ledger
        Warehouse warehouse = purchaseOrder.getWarehouse();

        for (GoodsReceiptRequest.ReceivedItem receivedItem
                : request.getItems()) {

            PurchaseOrderItem orderItem =
                    itemMap.get(
                            receivedItem.getPurchaseOrderItemId());

            Product product = orderItem.getProduct();

            Integer quantity =
                    receivedItem.getQuantityReceived();

            // Update received quantity on purchase order item
            orderItem.setQuantityReceived(
                    orderItem.getQuantityReceived() + quantity);

            // Find the stock record using composite primary key
            WarehouseStockId stockId =
                    new WarehouseStockId(
                            warehouse.getWarehouseId(),
                            product.getProductId());

            WarehouseStock stock =
                    warehouseStockRepository
                            .findByIdForUpdate(stockId)
                            .orElse(null);

            if (stock == null) {

                // Create a new stock record
                stock = new WarehouseStock();

                stock.setId(stockId);
                stock.setWarehouse(warehouse);
                stock.setProduct(product);
                stock.setQuantityOnHand(quantity);

            } else {

                // Increase existing stock
                stock.setQuantityOnHand(
                        stock.getQuantityOnHand() + quantity);
            }

            warehouseStockRepository.save(stock);

            // Create stock movement ledger entry
            StockMovement movement = new StockMovement();

            movement.setProduct(product);
            movement.setWarehouse(warehouse);
            movement.setMovementType(
                    StockMovementType.RECEIPT);
            movement.setQuantityChange(quantity);
            movement.setReferenceType("GOODS_RECEIPT");
            movement.setReferenceId(
                    receipt.getGoodsReceiptId().longValue());
            movement.setPerformedBy(
                    request.getReceivedBy().trim());

            movement.setNotes(
                    request.getReceiptNotes() == null
                            || request.getReceiptNotes().isBlank()
                            ? "Goods received against purchase order"
                            : request.getReceiptNotes());

            stockMovementRepository.save(movement);
        }

        // 7. Update purchase order status
        boolean fullyReceived = orderItems.stream()
                .allMatch(item ->
                        item.getQuantityReceived()
                                >= item.getQuantityOrdered());

        if (fullyReceived) {
            purchaseOrder.setStatus(
                    PurchaseOrderStatus.RECEIVED);
        } else {
            purchaseOrder.setStatus(
                    PurchaseOrderStatus.PARTIALLY_RECEIVED);
        }

        purchaseOrderRepository.save(purchaseOrder);

        // 8. Return the generated receipt ID
        return receipt.getGoodsReceiptId();
    }
}
