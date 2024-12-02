package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.Coupon;
import com.example.amazonclone2.Service.CouponService;
import com.example.amazonclone2.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/coupon")
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity getCoupons() {

        List<Coupon> coupons = couponService.getCoupons();

        if (coupons.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No coupons found."));
        }
        return ResponseEntity.status(200).body(coupons);
    }

    @PostMapping("/add/{userId}/{productId}")
    public ResponseEntity addCoupon(@PathVariable Integer userId,@PathVariable Integer productId,@RequestBody @Valid Coupon coupon, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        String response = userService.addCoupon(coupon,userId,productId);
        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Coupon successfully added."));

        }else
            return ResponseEntity.status(400).body(response);
    }

    @PutMapping("/update/{couponCode}")
    public ResponseEntity updateCoupon(@PathVariable String couponCode, @RequestBody @Valid Coupon coupon, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        Boolean isUpdated = couponService.updateCoupon(couponCode, coupon);

        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Coupon successfully updated."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Coupon not found."));
    }

    @DeleteMapping("/delete/{couponCode}")
    public ResponseEntity deleteCoupon(@PathVariable String couponCode) {

        Boolean isDeleted = couponService.deleteCoupon(couponCode);

        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Coupon successfully deleted."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Coupon not found."));
    }
}
