package com.example.amazonclone2.Repository;

import com.example.amazonclone2.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
