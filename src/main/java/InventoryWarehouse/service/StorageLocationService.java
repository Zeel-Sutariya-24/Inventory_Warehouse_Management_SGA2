
package InventoryWarehouse.service;

import InventoryWarehouse.dto.StorageLocationForm;
import InventoryWarehouse.model.LocationType;
import InventoryWarehouse.model.StorageLocation;
import InventoryWarehouse.model.Warehouse;
import InventoryWarehouse.repository.StorageLocationRepository;
import InventoryWarehouse.repository.WarehouseRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StorageLocationService {

    private final StorageLocationRepository locationRepository;
    private final WarehouseRepository warehouseRepository;

    public StorageLocationService(
            StorageLocationRepository locationRepository,
            WarehouseRepository warehouseRepository) {
        this.locationRepository = locationRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<StorageLocation> getAllLocations() {
        return locationRepository.findAllWithWarehouse();
    }

    public StorageLocation getLocationById(Integer id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Storage location not found"));
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public LocationType[] getLocationTypes() {
        return LocationType.values();
    }

    @Transactional
    public void addLocation(StorageLocationForm form) {
        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Selected warehouse not found"));

        StorageLocation location = new StorageLocation();
        copyFormToLocation(form, location, warehouse);

        locationRepository.saveAndFlush(location);
    }

    @Transactional
    public void updateLocation(Integer id, StorageLocationForm form) {
        StorageLocation location = getLocationById(id);

        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Selected warehouse not found"));

        copyFormToLocation(form, location, warehouse);

        locationRepository.saveAndFlush(location);
    }

    @Transactional
    public void deleteLocation(Integer id) {
        StorageLocation location = getLocationById(id);

        locationRepository.delete(location);
        locationRepository.flush();
    }

    private void copyFormToLocation(
            StorageLocationForm form,
            StorageLocation location,
            Warehouse warehouse) {

        location.setWarehouse(warehouse);
        location.setLocationCode(form.getLocationCode().trim());
        location.setLocationType(form.getLocationType());
        location.setActive(form.getActive() != null && form.getActive());
    }
}
