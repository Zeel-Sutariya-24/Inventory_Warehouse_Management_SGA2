
package InventoryWarehouse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goodsreceipts")
public class GoodsReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_receipt_id")
    private Integer goodsReceiptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(name = "received_at",
            nullable = false,
            insertable = false,
            updatable = false)
    private LocalDateTime receivedAt;

    @Column(name = "received_by", nullable = false, length = 120)
    private String receivedBy;

    @Column(name = "receipt_notes", length = 500)
    private String receiptNotes;

    // Getters and Setters

    public Integer getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public void setGoodsReceiptId(Integer goodsReceiptId) {
        this.goodsReceiptId = goodsReceiptId;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getReceiptNotes() {
        return receiptNotes;
    }

    public void setReceiptNotes(String receiptNotes) {
        this.receiptNotes = receiptNotes;
    }
}
