package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.Coupon;
import com.example.amazonclone2.Model.User;
import com.example.amazonclone2.Repository.CouponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getCoupons() {

        return couponRepository.findAll();
    }



    public Boolean updateCoupon(String couponCode , Coupon coupon){


        // Efficiently fetches all coupons once and iterates to find and update the matching one.
        List<Coupon> coupons = couponRepository.findAll();

        for (Coupon oldCoupon : coupons) {
            if(oldCoupon.getCouponCode().equals(couponCode)){
                oldCoupon.setCouponCode(couponCode);
                oldCoupon.setDiscount(coupon.getDiscount());
                oldCoupon.setStart(coupon.getStart());
                oldCoupon.setEnd(coupon.getEnd());
                couponRepository.save(oldCoupon);
                return true;

            }
        }


        return false;


    }


    public Boolean deleteCoupon(String couponCode){


        // Efficiently fetches all coupons once and iterates to find and delete the matching one.
        List<Coupon> coupons = couponRepository.findAll();

        for (Coupon Coupon : coupons) {
            if (Coupon.getCouponCode().equals(couponCode)){
                couponRepository.delete(Coupon);
                return true;
            }
        }

        return false;

    }

}
