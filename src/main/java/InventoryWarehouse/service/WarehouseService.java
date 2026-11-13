
package InventoryWarehouse.service;

import InventoryWarehouse.dto.WarehouseForm;
import InventoryWarehouse.model.Warehouse;
import InventoryWarehouse.repository.WarehouseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(Integer id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Warehouse not found."));
    }

    @Transactional
    public void addWarehouse(WarehouseForm form) {
        Warehouse warehouse = new Warehouse();
        copyFormToWarehouse(form, warehouse);
        warehouseRepository.saveAndFlush(warehouse);
    }

    @Transactional
    public void updateWarehouse(Integer id, WarehouseForm form) {
        Warehouse warehouse = getWarehouseById(id);
        copyFormToWarehouse(form, warehouse);
        warehouseRepository.saveAndFlush(warehouse);
    }

    @Transactional
    public void deleteWarehouse(Integer id) {
        Warehouse warehouse = getWarehouseById(id);
        warehouseRepository.delete(warehouse);
        warehouseRepository.flush();
    }

    private void copyFormToWarehouse(
            WarehouseForm form, Warehouse warehouse) {

        warehouse.setWarehouseName(form.getWarehouseName().trim());
        warehouse.setAddress(form.getAddress().trim());

        warehouse.setManagerName(
                form.getManagerName() == null
                        || form.getManagerName().isBlank()
                        ? null : form.getManagerName().trim()
        );

        warehouse.setActive(
                form.getActive() != null ? form.getActive() : false
        );
    }
}
