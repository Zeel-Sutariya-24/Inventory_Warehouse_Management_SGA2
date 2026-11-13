
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.SupplierRepository;
import InventoryWarehouse.model.Supplier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SupplierTestController {

    private final SupplierRepository supplierRepository;

    public SupplierTestController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("/suppliers-test")
    public List<Map<String, Object>> getSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(supplier -> Map.<String, Object>of(
                        "supplierId", supplier.getSupplierId(),
                        "supplierName", supplier.getSupplierName(),
                        "phone", supplier.getPhone(),
                        "email", supplier.getEmail() == null ? "" : supplier.getEmail(),
                        "active", supplier.getActive()
                ))
                .toList();
    }
}
