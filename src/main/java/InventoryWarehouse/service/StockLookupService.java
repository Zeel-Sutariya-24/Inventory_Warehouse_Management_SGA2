
package InventoryWarehouse.service;

import InventoryWarehouse.dto.StockLookupResponse;
import InventoryWarehouse.dto.WarehouseStockSummaryResponse;
import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.repository.WarehouseStockRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockLookupService {

    private final WarehouseStockRepository stockRepository;

    public StockLookupService(WarehouseStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockLookupResponse> getStockByWarehouse(Integer warehouseId) {

        List<WarehouseStock> stockRecords =
                stockRepository.findStockByWarehouseId(warehouseId);

        return stockRecords.stream()
                .map(stock -> new StockLookupResponse(
                        stock.getProduct().getSku(),
                        stock.getProduct().getProductName(),
                        stock.getQuantityOnHand(),
                        stock.getProduct().getReorderLevel()))
                .toList();
    }

    @Transactional(readOnly = true)
    public WarehouseStockSummaryResponse getWarehouseSummary(Integer warehouseId) {

        return stockRepository.getWarehouseSummary(warehouseId);
    }
}
