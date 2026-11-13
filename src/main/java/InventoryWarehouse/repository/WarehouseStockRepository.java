
package InventoryWarehouse.repository;

import InventoryWarehouse.dto.WarehouseStockSummaryResponse;
import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.model.WarehouseStockId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface WarehouseStockRepository
        extends JpaRepository<WarehouseStock, WarehouseStockId> {

    @Query("""
                SELECT new InventoryWarehouse.dto.WarehouseStockSummaryResponse(
                    COUNT(DISTINCT ws.product.productId),
                    COALESCE(SUM(ws.quantityOnHand), 0L),
                    SUM(
                        CASE
                            WHEN ws.quantityOnHand <= ws.product.reorderLevel
                            THEN 1L
                            ELSE 0L
                        END
                    )
                )
                FROM WarehouseStock ws
                WHERE ws.warehouse.warehouseId = :warehouseId
            """)
    WarehouseStockSummaryResponse getWarehouseSummary(
            @Param("warehouseId") Integer warehouseId);

    @Query("""
                SELECT ws
                FROM WarehouseStock ws
                JOIN FETCH ws.product
                JOIN FETCH ws.warehouse
                WHERE ws.quantityOnHand > 0
                ORDER BY ws.warehouse.warehouseName, ws.product.productName
            """)
    List<WarehouseStock> findAllStockForValuation();

    @Query("""
                SELECT ws
                FROM WarehouseStock ws
                JOIN FETCH ws.product
                JOIN FETCH ws.warehouse
                WHERE ws.quantityOnHand <= ws.product.reorderLevel
                ORDER BY ws.warehouse.warehouseName, ws.product.productName
            """)
    List<WarehouseStock> findLowStockRecords();

    @Query("""
                SELECT ws
                FROM WarehouseStock ws
                JOIN FETCH ws.product
                JOIN FETCH ws.warehouse
                WHERE ws.warehouse.warehouseId = :warehouseId
                ORDER BY ws.product.productName
            """)
    List<WarehouseStock> findStockByWarehouseId(
            @Param("warehouseId") Integer warehouseId);

    @Query("""
                SELECT ws FROM WarehouseStock ws
                JOIN FETCH ws.warehouse
                JOIN FETCH ws.product
                LEFT JOIN FETCH ws.location
            """)
    List<WarehouseStock> findAllWithDetails();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT ws
                FROM WarehouseStock ws
                WHERE ws.id = :id
            """)
    Optional<WarehouseStock> findByIdForUpdate(
            WarehouseStockId id);
}
