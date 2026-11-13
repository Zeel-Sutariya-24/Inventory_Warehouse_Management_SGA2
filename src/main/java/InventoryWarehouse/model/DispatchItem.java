
package InventoryWarehouse.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dispatchitems")
public class DispatchItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispatch_item_id")
    private Integer dispatchItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_order_id", nullable = false)
    private DispatchOrder dispatchOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_sale_price",
            nullable = false, precision = 12, scale = 2)
    private BigDecimal unitSalePrice = BigDecimal.ZERO;

    // Getters and Setters

    public Integer getDispatchItemId() {
        return dispatchItemId;
    }

    public void setDispatchItemId(Integer dispatchItemId) {
        this.dispatchItemId = dispatchItemId;
    }

    public DispatchOrder getDispatchOrder() {
        return dispatchOrder;
    }

    public void setDispatchOrder(DispatchOrder dispatchOrder) {
        this.dispatchOrder = dispatchOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitSalePrice() {
        return unitSalePrice;
    }

    public void setUnitSalePrice(BigDecimal unitSalePrice) {
        this.unitSalePrice = unitSalePrice;
    }
}
