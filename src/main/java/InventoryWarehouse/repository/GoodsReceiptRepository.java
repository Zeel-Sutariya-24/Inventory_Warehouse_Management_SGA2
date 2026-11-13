
package InventoryWarehouse.repository;

import InventoryWarehouse.model.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsReceiptRepository
        extends JpaRepository<GoodsReceipt, Integer> {
}
