package com.streambite.notification.repository;

import com.streambite.notification.model.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRecordRepository extends JpaRepository<OrderRecord, String> {
}