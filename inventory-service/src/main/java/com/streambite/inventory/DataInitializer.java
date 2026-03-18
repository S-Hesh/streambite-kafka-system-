package com.streambite.inventory;

import com.streambite.inventory.model.StockItem;
import com.streambite.inventory.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final StockItemRepository stockRepo;

    public DataInitializer(StockItemRepository stockRepo) {
        this.stockRepo = stockRepo;
    }

    @Override
    public void run(String... args) {
        // Only seed if the table is empty.
        // On first startup: inserts all items.
        // On restart: table already has rows, skips seeding.
        // This means stock levels now SURVIVE restarts.
        if (stockRepo.count() == 0) {
            stockRepo.saveAll(List.of(
                    new StockItem("pizza",  10),
                    new StockItem("burger",  5),
                    new StockItem("sushi",   3),
                    new StockItem("pasta",   8),
                    new StockItem("tacos",   0)
            ));
            log.info("Stock table seeded with initial inventory");
        } else {
            log.info("Stock table already populated — skipping seed");
            stockRepo.findAll().forEach(s ->
                    log.info("  {} → {} units", s.getItemName(), s.getQuantity()));
        }
    }
}