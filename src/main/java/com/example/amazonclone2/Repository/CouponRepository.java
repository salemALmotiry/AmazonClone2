package com.example.amazonclone2.Repository;

import com.example.amazonclone2.Model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Integer> {
}
