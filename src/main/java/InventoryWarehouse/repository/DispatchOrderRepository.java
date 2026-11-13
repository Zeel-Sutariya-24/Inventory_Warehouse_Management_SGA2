
package InventoryWarehouse.repository;

import InventoryWarehouse.model.DispatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface DispatchOrderRepository
        extends JpaRepository<DispatchOrder, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT d
                FROM DispatchOrder d
                WHERE d.dispatchOrderId = :id
            """)
    Optional<DispatchOrder> findByIdForUpdate(
            @Param("id") Integer id);
}
