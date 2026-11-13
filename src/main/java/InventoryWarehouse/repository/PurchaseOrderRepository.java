
package InventoryWarehouse.repository;

import InventoryWarehouse.model.PurchaseOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT po
        FROM PurchaseOrder po
        WHERE po.purchaseOrderId = :id
    """)
    Optional<PurchaseOrder> findByIdForUpdate(
            @Param("id") Integer id);
}
