package com.example.amazonclone2.Repository;

import com.example.amazonclone2.Model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory,Integer> {
}