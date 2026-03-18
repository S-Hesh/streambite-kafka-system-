package com.streambite.inventory.stock;

import com.streambite.inventory.model.StockItem;
import com.streambite.inventory.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StockRegistry {

    private static final Logger log = LoggerFactory.getLogger(StockRegistry.class);

    private final StockItemRepository repo;

    public StockRegistry(StockItemRepository repo) {
        this.repo = repo;
    }

    /**
     * Attempts to deduct the requested quantity from the DB row.
     *
     * Uses PESSIMISTIC_WRITE lock — the row is locked for the entire
     * transaction. Concurrent requests for the same item queue up and
     * execute one at a time. No synchronized keyword needed.
     *
     * Returns true if stock was available and deducted.
     * Returns false if stock was insufficient — row is NOT modified.
     */
    @Transactional
    public boolean deduct(String itemName, int quantity) {
        StockItem item = repo.findByItemNameWithLock(itemName)
                .orElse(null);

        if (item == null) {
            log.warn("Unknown item: {} — treating as out of stock", itemName);
            return false;
        }

        if (item.getQuantity() < quantity) {
            log.warn("Insufficient stock: {} | requested={} available={}",
                    itemName, quantity, item.getQuantity());
            return false;
        }

        int before = item.getQuantity();
        item.setQuantity(before - quantity);
        repo.save(item);

        log.info("Stock deducted: {} | was={} now={}",
                itemName, before, item.getQuantity());
        return true;
    }

    public int getStock(String itemName) {
        return repo.findById(itemName)
                .map(StockItem::getQuantity)
                .orElse(0);
    }
}