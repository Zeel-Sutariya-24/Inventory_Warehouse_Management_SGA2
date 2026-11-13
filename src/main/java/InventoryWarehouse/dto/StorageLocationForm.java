
package InventoryWarehouse.dto;

import InventoryWarehouse.model.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class StorageLocationForm {

    @NotNull(message = "Please select a warehouse")
    @Positive(message = "Please select a valid warehouse")
    private Integer warehouseId;

    @NotBlank(message = "Location code is required")
    @Size(max = 30, message = "Location code cannot exceed 30 characters")
    private String locationCode;

    @NotNull(message = "Please select a location type")
    private LocationType locationType = LocationType.SHELF;

    private Boolean active = true;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
