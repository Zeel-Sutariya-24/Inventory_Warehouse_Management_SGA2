
package InventoryWarehouse.dto;

import java.util.List;

public class GoodsReceiptRequest {

    private Integer purchaseOrderId;

    private String receivedBy;

    private String receiptNotes;

    private List<ReceivedItem> items;

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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

    public List<ReceivedItem> getItems() {
        return items;
    }

    public void setItems(List<ReceivedItem> items) {
        this.items = items;
    }

    public static class ReceivedItem {

        private Integer purchaseOrderItemId;

        private Integer quantityReceived;

        public Integer getPurchaseOrderItemId() {
            return purchaseOrderItemId;
        }

        public void setPurchaseOrderItemId(Integer purchaseOrderItemId) {
            this.purchaseOrderItemId = purchaseOrderItemId;
        }

        public Integer getQuantityReceived() {
            return quantityReceived;
        }

        public void setQuantityReceived(Integer quantityReceived) {
            this.quantityReceived = quantityReceived;
        }
    }
}
