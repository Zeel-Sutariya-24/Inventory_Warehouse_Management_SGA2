
package InventoryWarehouse.service;

import InventoryWarehouse.model.DispatchOrder;
import InventoryWarehouse.model.DispatchOrderStatus;
import InventoryWarehouse.model.DispatchItem;
import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.model.WarehouseStockId;
import InventoryWarehouse.model.StockMovement;
import InventoryWarehouse.model.StockMovementType;

import InventoryWarehouse.repository.DispatchOrderRepository;
import InventoryWarehouse.repository.DispatchItemRepository;
import InventoryWarehouse.repository.WarehouseStockRepository;
import InventoryWarehouse.repository.StockMovementRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DispatchService {

    private final DispatchOrderRepository dispatchOrderRepository;
    private final DispatchItemRepository dispatchItemRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockMovementRepository stockMovementRepository;

    public DispatchService(
            DispatchOrderRepository dispatchOrderRepository,
            DispatchItemRepository dispatchItemRepository,
            WarehouseStockRepository warehouseStockRepository,
            StockMovementRepository stockMovementRepository) {

        this.dispatchOrderRepository = dispatchOrderRepository;
        this.dispatchItemRepository = dispatchItemRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public void dispatchOrder(Integer dispatchOrderId) {

        if (dispatchOrderId == null || dispatchOrderId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid dispatch order ID."
            );
        }

        // 1. Lock and retrieve dispatch order
        DispatchOrder order =
                dispatchOrderRepository.findByIdForUpdate(dispatchOrderId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Dispatch order not found."
                                )
                        );

        // 2. Only CONFIRMED orders can be dispatched
        if (order.getStatus() != DispatchOrderStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Dispatch order must be CONFIRMED before dispatch."
            );
        }

        Integer warehouseId =
                order.getWarehouse().getWarehouseId();

        // 3. Lock all dispatch items
        List<DispatchItem> items =
                dispatchItemRepository.findByDispatchOrderIdForUpdate(
                        dispatchOrderId
                );

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Dispatch order has no items."
            );
        }

        Set<Integer> processedProducts = new HashSet<>();

        // 4. Validate all items and stock before changing anything
        for (DispatchItem item : items) {

            Integer productId =
                    item.getProduct().getProductId();

            Integer quantity = item.getQuantity();

            if (productId == null || quantity == null || quantity <= 0) {
                throw new IllegalArgumentException(
                        "Every dispatch item must have a product and positive quantity."
                );
            }

            if (!processedProducts.add(productId)) {
                throw new IllegalArgumentException(
                        "A product appears more than once in this dispatch order."
                );
            }

            WarehouseStockId stockId =
                    new WarehouseStockId(warehouseId, productId);

            WarehouseStock stock =
                    warehouseStockRepository.findByIdForUpdate(stockId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Warehouse stock record not found for product ID "
                                                    + productId
                                    )
                            );

            if (stock.getQuantityOnHand() == null
                    || stock.getQuantityOnHand() < quantity) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product ID " + productId
                );
            }
        }

        // 5. Deduct stock and create stock movements
        for (DispatchItem item : items) {

            Integer productId =
                    item.getProduct().getProductId();

            Integer quantity = item.getQuantity();

            WarehouseStockId stockId =
                    new WarehouseStockId(warehouseId, productId);

            WarehouseStock stock =
                    warehouseStockRepository.findByIdForUpdate(stockId)
                            .orElseThrow();

            stock.setQuantityOnHand(
                    stock.getQuantityOnHand() - quantity
            );

            warehouseStockRepository.save(stock);

            StockMovement movement = new StockMovement();

            movement.setProduct(item.getProduct());
            movement.setWarehouse(order.getWarehouse());
            movement.setMovementType(StockMovementType.DISPATCH);
            movement.setQuantityChange(-quantity);
            movement.setReferenceType("DISPATCH_ORDER");
            movement.setReferenceId(dispatchOrderId.longValue());
            movement.setPerformedBy(order.getCreatedBy());
            movement.setNotes(
                    "Stock dispatched for customer reference: "
                            + order.getCustomerReference()
            );

            stockMovementRepository.save(movement);
        }

        // 6. Mark dispatch order as dispatched
        order.setStatus(DispatchOrderStatus.DISPATCHED);
        order.setDispatchedAt(LocalDateTime.now());

        dispatchOrderRepository.save(order);
    }
}
