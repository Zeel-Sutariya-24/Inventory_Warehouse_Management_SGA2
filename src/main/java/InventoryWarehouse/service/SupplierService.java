
package InventoryWarehouse.service;

import InventoryWarehouse.dto.SupplierForm;
import InventoryWarehouse.model.Supplier;
import InventoryWarehouse.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Supplier not found."));
    }

    @Transactional
    public void addSupplier(SupplierForm form) {
        Supplier supplier = new Supplier();
        copyFormToSupplier(form, supplier);
        supplierRepository.saveAndFlush(supplier);
    }

    @Transactional
    public void updateSupplier(Integer id, SupplierForm form) {
        Supplier supplier = getSupplierById(id);
        copyFormToSupplier(form, supplier);
        supplierRepository.saveAndFlush(supplier);
    }

    @Transactional
    public void deleteSupplier(Integer id) {
        Supplier supplier = getSupplierById(id);
        supplierRepository.delete(supplier);
        supplierRepository.flush();
    }

    private void copyFormToSupplier(SupplierForm form, Supplier supplier) {
        supplier.setSupplierName(form.getSupplierName().trim());
        supplier.setContactName(
                form.getContactName() == null || form.getContactName().isBlank()
                        ? null : form.getContactName().trim()
        );
        supplier.setPhone(form.getPhone().trim());
        supplier.setEmail(form.getEmail().trim());
        supplier.setAddress(
                form.getAddress() == null || form.getAddress().isBlank()
                        ? null : form.getAddress().trim()
        );
        supplier.setActive(form.getActive() != null ? form.getActive() : false);
    }
}
