
package InventoryWarehouse.dto;

public class WarehouseStockSummaryResponse {

    private Long totalProducts;
    private Long totalUnits;
    private Long lowStockProducts;

    public WarehouseStockSummaryResponse(
            Long totalProducts,
            Long totalUnits,
            Long lowStockProducts) {

        this.totalProducts = totalProducts;
        this.totalUnits = totalUnits;
        this.lowStockProducts = lowStockProducts;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public Long getTotalUnits() {
        return totalUnits;
    }

    public Long getLowStockProducts() {
        return lowStockProducts;
    }
}
