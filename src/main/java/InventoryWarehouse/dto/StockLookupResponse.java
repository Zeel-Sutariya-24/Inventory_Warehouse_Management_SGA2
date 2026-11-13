package InventoryWarehouse.dto;

import java.math.BigDecimal;

public class StockLookupResponse {

    private String sku;
    private String productName;
    private Integer quantityOnHand;
    private Integer reorderLevel;
    private String status;

    public StockLookupResponse(
            String sku,
            String productName,
            Integer quantityOnHand,
            Integer reorderLevel) {

        this.sku = sku;
        this.productName = productName;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;

        if (quantityOnHand == 0) {
            this.status = "OUT OF STOCK";
        } else if (quantityOnHand <= reorderLevel) {
            this.status = "LOW STOCK";
        } else {
            this.status = "IN STOCK";
        }
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

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public String getStatus() {
        return status;
    }
}