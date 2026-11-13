
package InventoryWarehouse.repository;

import InventoryWarehouse.model.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StorageLocationRepository
        extends JpaRepository<StorageLocation, Integer> {

    @Query("""
        SELECT sl
        FROM StorageLocation sl
        JOIN FETCH sl.warehouse
        ORDER BY sl.warehouse.warehouseName, sl.locationCode
    """)
    List<StorageLocation> findAllWithWarehouse();
}
