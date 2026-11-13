package InventoryWarehouse.service;

import InventoryWarehouse.dto.StockValuationResponse;
import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.repository.WarehouseStockRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockValuationService {

    private final WarehouseStockRepository stockRepository;

    public StockValuationService(WarehouseStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockValuationResponse> getStockValuation() {

        List<WarehouseStock> records =
                stockRepository.findAllStockForValuation();

        return records.stream()
                .map(stock -> new StockValuationResponse(
                        stock.getWarehouse().getWarehouseName(),
                        stock.getProduct().getSku(),
                        stock.getProduct().getProductName(),
                        stock.getQuantityOnHand(),
                        stock.getProduct().getUnitCost()
                ))
                .toList();
    }

    public BigDecimal getTotalValuation(
            List<StockValuationResponse> records) {

        return records.stream()
                .map(StockValuationResponse::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}