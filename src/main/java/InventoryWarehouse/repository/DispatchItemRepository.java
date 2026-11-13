
package InventoryWarehouse.repository;

import InventoryWarehouse.model.DispatchItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;

public interface DispatchItemRepository
        extends JpaRepository<DispatchItem, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT item
                FROM DispatchItem item
                WHERE item.dispatchOrder.dispatchOrderId = :dispatchOrderId
                ORDER BY item.dispatchItemId
            """)
    List<DispatchItem> findByDispatchOrderIdForUpdate(
            @Param("dispatchOrderId") Integer dispatchOrderId);
}
