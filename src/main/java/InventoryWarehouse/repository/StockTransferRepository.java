
package InventoryWarehouse.repository;

import InventoryWarehouse.model.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface StockTransferRepository
        extends JpaRepository<StockTransfer, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT st
                FROM StockTransfer st
                WHERE st.transferId = :id
            """)
    Optional<StockTransfer> findByIdForUpdate(
            @Param("id") Integer id);
}
