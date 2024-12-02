package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.*;
import com.example.amazonclone2.Repository.CouponRepository;
import com.example.amazonclone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final MerchantStockService merchantStockService;
    private final PurchaseHistoryService purchaseHistoryService;
    private final CouponService couponService;
    private final CouponRepository couponRepository;


    public List<User> getUsers(){

        return userRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }



    public String updateUser(Integer id , User user){

        for (User oldUser : userRepository.findAll() ) {
            if (oldUser.getId().equals(id)) {
                oldUser.setUserName(user.getUserName());
                oldUser.setPassword(user.getPassword());
                oldUser.setEmail(user.getEmail());

                oldUser.setRole(user.getRole());
                oldUser.setBalance(user.getBalance());

                userRepository.save(oldUser);
                return null;
            }
        }

        return "User not found";


    }


    public Boolean deleteUser(Integer id){

        User user = userRepository.findById(id).get();
        if (user == null){
            return false;
        }

        userRepository.delete(user);
        return true;
    }


    public String directPaying(Integer productId, Integer userId, Integer merchantId) {
        boolean userFound = false;
        boolean productFound = false;
        boolean merchantFound = false;

        for (User user : getUsers()) {
            if (user.getId().equals(userId)) {
                userFound = true;

                for (MerchantStock merchantStock : merchantStockService.getMerchantStocks()) {
                    if (merchantStock.getProductId().equals(productId)) {
                        productFound = true;

                        if (merchantStock.getMerchantId().equals(merchantId)) {
                            merchantFound = true;

                            Double productPrice = productService.getProduct(productId).getPrice();

                            if (productPrice > user.getBalance()) {
                                return "Product price exceeds the user's balance.";
                            } else {
                                String response = merchantStockService.reStock(productId, merchantId, -1, false);

                                if (response != null) {
                                    return response;
                                }

                                user.setBalance(user.getBalance() - productPrice);
                                updateUser(userId,user);
                                response = purchaseHistoryService.addPurchaseHistory(
                                        new PurchaseHistory(userId, productId, merchantId, productPrice)
                                );


                                return null;
                            }
                        }
                    }
                }
            }
        }

        // Return messages for missing entities
        if (!userFound) {
            return "User not found.";
        }
        if (!productFound) {
            return "Product not found.";
        }
        if (!merchantFound) {
            return "Merchant not found.";
        }

        return "Unexpected error occurred.";
    }


    public String refund(Integer productId, Integer userId, Integer merchantId) {
        boolean userFound = false;
        boolean purchaseFound = false;

        for (User user : getUsers()) {
            if (user.getId().equals(userId)) {
                userFound = true;

                for (PurchaseHistory purchaseHistory : purchaseHistoryService.getPurchaseHistories()) {

                    if (purchaseHistory.getUserId().equals(userId)) {

                        if (purchaseHistory.getProductId().equals(productId)) {

                            if (purchaseHistory.getMerchantId().equals(merchantId)) {

                                user.setBalance(user.getBalance() + purchaseHistory.getPrice());

                                updateUser(userId, user);
                                purchaseHistoryService.deletePurchaseHistory(purchaseHistory.getId());
                                return null;
                            } else {
                                return "Merchant mismatch for the given purchase history.";
                            }
                        } else {
                            purchaseFound = true;
                        }
                    }
                }
            }
        }

        if (!userFound) {
            return "User not found.";
        }
        if (!purchaseFound) {
            return "Purchase history not found for the given product.";
        }

        return "Unexpected error occurred.";
    }


    public String gifGift(Integer gifterid,String giftedEmail,Integer productId ,Integer merchantId) {

        Integer giftedId = null;
        for (User user : getUsers()) {
            if (user.getEmail().equals(giftedEmail) && !user.getId().equals(gifterid)) {
                giftedId = user.getId();
            }
        }
        if (giftedId == null) {
            return "You cannot gift to yourself.";
        }

        boolean userFound = false;
        boolean productFound = false;
        boolean merchantFound = false;

        for (User user : getUsers()) {
            if (user.getId().equals(gifterid)) {
                userFound = true;

                for (MerchantStock merchantStock : merchantStockService.getMerchantStocks()) {
                    if (merchantStock.getProductId().equals(productId)) {
                        productFound = true;

                        if (merchantStock.getMerchantId().equals(merchantId)) {
                            merchantFound = true;

                            Double productPrice = productService.getProduct(productId).getPrice();

                            if (productPrice > user.getBalance()) {
                                return "Product price exceeds the user's balance.";
                            } else {
                                String response = merchantStockService.reStock(productId, merchantId, -1, false);

                                if (response != null) {
                                    return response;
                                }

                                user.setBalance(user.getBalance() - productPrice);
                                updateUser(gifterid,user);
                                response = purchaseHistoryService.addPurchaseHistory(
                                        new PurchaseHistory(giftedId, productId, merchantId, productPrice)
                                );


                                return null;
                            }
                        }
                    }
                }
            }
        }

        // Return messages for missing entities
        if (!userFound) {
            return "User not found.";
        }
        if (!productFound) {
            return "Product not found.";
        }
        if (!merchantFound) {
            return "Merchant not found.";
        }

        return "Unexpected error occurred.";
    }


    public List<Product> recommendationProducts(Integer userId) {
        List<Product> recommendationProducts = new ArrayList<>();

        // Find the user by userId
        User user = null;
        for (User u : getUsers()) {
            if (u.getId().equals(userId)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            return recommendationProducts;
        }

        List<PurchaseHistory> userPurchaseHistory = new ArrayList<>();
        for (PurchaseHistory purchaseHistory : purchaseHistoryService.getPurchaseHistories()) {
            if (purchaseHistory.getUserId().equals(userId)) {
                userPurchaseHistory.add(purchaseHistory);
            }
        }

        if (userPurchaseHistory.isEmpty()) {
            return recommendationProducts;
        }

        Integer mostRepeatedCategoryId = getMostRepeatedCategory(userPurchaseHistory);

        for (Product product : productService.getProducts()) {
            if (product.getCategoryId().equals(mostRepeatedCategoryId)) {
                boolean alreadyPurchased = false;

                for (PurchaseHistory purchaseHistory : userPurchaseHistory) {
                    if (purchaseHistory.getProductId().equals(product.getId())) {
                        alreadyPurchased = true;
                        break;
                    }
                }

                if (!alreadyPurchased) {
                    recommendationProducts.add(product);
                }
            }
        }

        return recommendationProducts;
    }


    public ArrayList<Product> bestSelling(int limit) {
        ArrayList<Product> recommendationProducts = new ArrayList<>();

        Map<Product, Integer> productCountMap = new HashMap<>();

        List<PurchaseHistory> purchaseHistories = purchaseHistoryService.getPurchaseHistories();

        for (PurchaseHistory purchaseHistory : purchaseHistories) {
            Product purchasedProduct = productService.getProduct(purchaseHistory.getProductId());
            if (purchasedProduct != null) {
                productCountMap.put(purchasedProduct, productCountMap.getOrDefault(purchasedProduct, 0) + 1);
            }
        }

        List<Map.Entry<Product, Integer>> sortedList = sort(productCountMap);


        int count = 0;
        for (Map.Entry<Product, Integer> entry : sortedList) {
            if (count >= limit && entry.getKey() !=null)
                break;
            Product product = entry.getKey();
            int productCount = entry.getValue();
            count++;
            recommendationProducts.add(product);
        }

        return recommendationProducts;
    }

    private List<Map.Entry<Product, Integer>> sort(Map<Product, Integer> productCountMap) {
        List<Map.Entry<Product, Integer>> sortedList = new ArrayList<>(productCountMap.entrySet());

        for (int i = 0; i < sortedList.size(); i++) {
            int maxIndex = i;
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (sortedList.get(j).getValue() > sortedList.get(maxIndex).getValue()) {
                    maxIndex = j;
                }
            }

            if (maxIndex != i) {
                Map.Entry<Product, Integer> temp = sortedList.get(i);
                sortedList.set(i, sortedList.get(maxIndex));
                sortedList.set(maxIndex, temp);
            }
        }
        return sortedList;
    }

    public String payWithCoupon(Integer productId, Integer userId, Integer merchantId, String couponCode) {
        boolean userFound = false;
        boolean productFound = false;
        boolean merchantFound = false;
        boolean couponFound = false;
        String response = null;

        User user = null;
        Product product = null;
        Coupon coupon = null;

        for (User u : getUsers()) {
            if (u.getId().equals(userId)) {
                userFound = true;
                user = u; // Found the user
            }
        }

        for (Product p : productService.getProducts()) {
            if (p.getId().equals(productId)) {
                productFound = true;
                product = p; // Found the product
            }
        }


        for (MerchantStock ms : merchantStockService.getMerchantStocks()) {
            if (ms.getProductId().equals(productId) && ms.getMerchantId().equals(merchantId)) {
                merchantFound = true;
                break;
            }
        }

        for (Coupon coupon1 : couponService.getCoupons()){
            if (coupon1.getCouponCode().equals(couponCode)) {
                couponFound = true;
                coupon = coupon1;

            }

        }

        if (!userFound) return "User not found.";
        if (!productFound) return "Product not found.";
        if (!merchantFound) return "Merchant not found.";
        if (!couponFound) return "Coupon not found.";

        if (!couponCode.isEmpty()) {
            if (product.getCouponCode() == null || !product.getCouponCode().equals(couponCode)) {
                System.out.println(product.getCouponCode());
                System.out.println(couponCode);

                return "Coupon is invalid.";
            }

            if (coupon.getEnd().isBefore(LocalDateTime.now())) {
                return "Coupon has expired.";
            }

            if (product.getCouponCode() != null && product.getCouponCode().equals(couponCode)) {
                Double discountPrice =(product.getPrice() * coupon.getDiscount() / 100.0);
                System.out.println(discountPrice);
                System.out.println(user.getBalance());
                if (discountPrice > user.getBalance()) {
                    return "Product price after discount exceeds the user's balance.";
                }

                // Proceed with stock update, balance deduction, and purchase history
                String stockResponse = merchantStockService.reStock(productId, merchantId, -1, false);
                if (stockResponse != null) {
                    return stockResponse; // If stock is insufficient
                }

                // Deduct the amount after discount from user balance
                user.setBalance(user.getBalance() - discountPrice);
                updateUser(userId, user); // Save updated user

                // Add purchase history
                purchaseHistoryService.addPurchaseHistory(new PurchaseHistory(userId, productId, merchantId, discountPrice));
                System.out.println(discountPrice);

                return null; // Successful transaction
            }
        }


        return null; // Successful transaction
    }




    public String addCoupon(Coupon coupon,Integer userId,Integer productId) {

        for (User user : getUsers()) {
            if (user.getId().equals(userId)) {
                if (user.getRole().equals("Admin")) {

                    for (Product product : productService.getProducts()) {
                        if (product.getId().equals(productId)) {
                            product.setCouponCode(coupon.getCouponCode());
                            productService.updatedProduct(productId,product);
                            couponRepository.save(coupon);
                            return null;
                        }
                    }
                    return "product not found.";

                } else {
                    return "User does not admin ";
                }
            }
        }

        return "User not found";
    }






































    private Integer getMostRepeatedCategory(List<PurchaseHistory> userPurchaseHistory) {
        Integer mostRepeated = null;
        int maxCount = 0;

        for (Product currentProduct : productService.getProducts()) {
            int count = 0;

            for (PurchaseHistory purchaseHistory : userPurchaseHistory) {
                Product purchasedProduct = productService.getProduct(purchaseHistory.getProductId());
                if (currentProduct.getCategoryId().equals(purchasedProduct.getCategoryId())) {
                    count++;
                }
            }

            if (count > maxCount) {
                mostRepeated = currentProduct.getCategoryId();
                maxCount = count;
            }
        }

        return mostRepeated;
    }

    public boolean checkUser(Integer userId) {
        for (User user : getUsers()) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
