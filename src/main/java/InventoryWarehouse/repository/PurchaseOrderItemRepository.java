
package InventoryWarehouse.repository;

import InventoryWarehouse.model.PurchaseOrderItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderItemRepository
        extends JpaRepository<PurchaseOrderItem, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT item
        FROM PurchaseOrderItem item
        WHERE item.purchaseOrder.purchaseOrderId = :purchaseOrderId
        ORDER BY item.purchaseOrderItemId
    """)
    List<PurchaseOrderItem> findByPurchaseOrderIdForUpdate(
            @Param("purchaseOrderId") Integer purchaseOrderId);
}
