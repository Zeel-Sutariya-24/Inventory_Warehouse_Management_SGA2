package InventoryWarehouse.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StockValuationResponse {

    private String warehouseName;
    private String sku;
    private String productName;
    private Integer quantityOnHand;
    private BigDecimal unitCost;
    private BigDecimal totalValue;

    public StockValuationResponse(
            String warehouseName,
            String sku,
            String productName,
            Integer quantityOnHand,
            BigDecimal unitCost) {

        this.warehouseName = warehouseName;
        this.sku = sku;
        this.productName = productName;
        this.quantityOnHand = quantityOnHand;
        this.unitCost = unitCost;

        this.totalValue = unitCost
                .multiply(BigDecimal.valueOf(quantityOnHand))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getSku() {
        return sku;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
}