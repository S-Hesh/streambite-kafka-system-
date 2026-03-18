package com.streambite.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "stock_items")
public class StockItem {

    @Id
    private String itemName;

    private int quantity;

    // @Version enables optimistic locking —
    // if two threads try to update the same row simultaneously,
    // one will get an OptimisticLockException and retry.
    // This replaces the 'synchronized' keyword from the old HashMap approach.
    @Version
    private Long version;

    public StockItem() {}

    public StockItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() { return itemName; }
    public int    getQuantity() { return quantity; }
    public Long   getVersion()  { return version; }

    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setQuantity(int quantity)    { this.quantity = quantity; }
    public void setVersion(Long version)     { this.version = version; }
}
