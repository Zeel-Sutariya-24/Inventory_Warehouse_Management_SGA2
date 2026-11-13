
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.StorageLocationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StorageLocationTestController {

    private final StorageLocationRepository storageLocationRepository;

    public StorageLocationTestController(
            StorageLocationRepository storageLocationRepository) {
        this.storageLocationRepository = storageLocationRepository;
    }

    @GetMapping("/locations-test")
    public List<Map<String, Object>> getLocations() {

        return storageLocationRepository.findAll()
                .stream()
                .map(location -> Map.<String, Object>of(
                        "locationId", location.getLocationId(),
                        "locationCode", location.getLocationCode(),
                        "locationType", location.getLocationType().name(),
                        "warehouseName",
                        location.getWarehouse().getWarehouseName(),
                        "active", location.getActive()
                ))
                .toList();
    }
}
