package com.streambite.inventory.controller;

import com.streambite.inventory.model.StockItem;
import com.streambite.inventory.repository.StockItemRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockItemRepository repo;

    public StockController(StockItemRepository repo) {
        this.repo = repo;
    }

    // Returns real stock levels straight from the database.
    // Frontend calls this on load and after every order
    // to show accurate counts — no more hardcoded values.
    @GetMapping
    public List<StockItem> getAllStock() {
        return repo.findAll();
    }
}