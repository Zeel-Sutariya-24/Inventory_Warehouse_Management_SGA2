package InventoryWarehouse.service;

import InventoryWarehouse.model.WarehouseStock;
import InventoryWarehouse.repository.WarehouseStockRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReorderAlertService {

    private final WarehouseStockRepository stockRepository;

    public ReorderAlertService(WarehouseStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<WarehouseStock> getReorderAlerts() {
        return stockRepository.findLowStockRecords();
    }
}