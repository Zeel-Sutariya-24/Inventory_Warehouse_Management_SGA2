
package InventoryWarehouse.repository;

import InventoryWarehouse.model.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;

public interface TransferItemRepository
                extends JpaRepository<TransferItem, Integer> {
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                            SELECT item
                            FROM TransferItem item
                            WHERE item.stockTransfer.transferId = :transferId
                            ORDER BY item.transferItemId
                        """)
        List<TransferItem> findByTransferIdForUpdate(
                        @Param("transferId") Integer transferId);
}
