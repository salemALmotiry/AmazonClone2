package com.example.amazonclone2.Repository;

import com.example.amazonclone2.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
