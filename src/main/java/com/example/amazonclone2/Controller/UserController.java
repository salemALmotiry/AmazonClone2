package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.Product;
import com.example.amazonclone2.Model.User;
import com.example.amazonclone2.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/get")
    public ResponseEntity getUsers(){

        if (userService.getUsers().isEmpty()){
            return ResponseEntity.status(200).body(new ApiResponse("No users found."));
        }
        return ResponseEntity.status(200).body(userService.getUsers());
    }


    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        }

        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("New user successfully added."));

        }



    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        }

        String response = userService.updateUser(id,user);

        if (response==null){
            return ResponseEntity.status(200).body(new ApiResponse("User details successfully updated."));

        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){

        boolean isDeleted = userService.deleteUser(id);

        if (isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("User successfully deleted."));

        }

        return ResponseEntity.status(400).body(new ApiResponse("User not found."));
    }

    @PutMapping("/direct-paying/{userId}/{merchantId}/{productId}")
    public ResponseEntity directPay(@PathVariable Integer userId, @PathVariable Integer merchantId, @PathVariable Integer productId){


        String response = userService.directPaying(productId,userId,merchantId);

        if (response == null){
            return ResponseEntity.status(200).body(new ApiResponse("Product has been purchased."));

        }
        return ResponseEntity.status(400).body(new ApiResponse(response));
    }

    @PutMapping("/refund/{userId}/{merchantId}/{productId}")
    public ResponseEntity refund(@PathVariable Integer userId, @PathVariable Integer merchantId, @PathVariable Integer productId){

        String response = userService.refund(productId,userId,merchantId);

        if (response == null){
            return ResponseEntity.status(200).body(new ApiResponse("Item has been successfully refund"));

        }
        return ResponseEntity.status(400).body(new ApiResponse(response));

    }

    @PutMapping("/gif-gift/{userId}/{giftedEmail}/{merchantId}/{productId}")
    public ResponseEntity gifGift(@PathVariable Integer userId, @PathVariable String giftedEmail, @PathVariable Integer merchantId, @PathVariable Integer productId){


        String response = userService.gifGift(userId,giftedEmail,productId,merchantId);

        if (response == null){
            return ResponseEntity.status(200).body(new ApiResponse("Gifted user has been successfully gifted"));

        }
        return ResponseEntity.status(400).body(new ApiResponse(response));


    }

    @GetMapping("/recommendation/{userId}")
    public ResponseEntity recommendation(@PathVariable Integer userId) {

        if (!userService.checkUser(userId)){
            return ResponseEntity.status(400).body(new ApiResponse("User not found in the system."));
        }

        List<Product> products = userService.recommendationProducts(userId);

        if (products != null){
            return ResponseEntity.status(200).body(products);

        }
        return ResponseEntity.status(400).body(new ApiResponse("User do not make ant perches"));
    }

    @GetMapping("/best-selling/{userId}/{limit}")
    public ResponseEntity bestSelling(@PathVariable Integer userId,@PathVariable int limit) {

        if (!userService.checkUser(userId)){
            return ResponseEntity.status(400).body(new ApiResponse("User not found in the system."));
        }
        ArrayList<Product> products = userService.bestSelling(limit);

        if (!products.isEmpty()){
            return ResponseEntity.status(200).body(products);

        }
        return ResponseEntity.status(400).body(new ApiResponse("There is no best products available at this time"));
    }

    @PutMapping("/pay-with-coupon/{userId}/{merchantId}/{productId}/{coupon}")
    public ResponseEntity payWithCoupon(@PathVariable Integer userId, @PathVariable Integer merchantId, @PathVariable Integer productId,@PathVariable String coupon) {


        String response = userService.payWithCoupon(productId,userId,merchantId,coupon);

        if (response == null){
            return ResponseEntity.status(200).body(new ApiResponse("Product has been purchased."));

        }
        return ResponseEntity.status(400).body(new ApiResponse(response));
    }
}
