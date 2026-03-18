package com.streambite.inventory.repository;

import com.streambite.inventory.model.StockItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, String> {

    // PESSIMISTIC_WRITE locks the row for the duration of the transaction.
    // This prevents two simultaneous orders for the same item from both
    // reading quantity=3 and both deducting, resulting in quantity=-2.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM StockItem s WHERE s.itemName = :itemName")
    Optional<StockItem> findByItemNameWithLock(@Param("itemName") String itemName);
}