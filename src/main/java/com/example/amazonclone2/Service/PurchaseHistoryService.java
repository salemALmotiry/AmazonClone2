package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.Merchant;
import com.example.amazonclone2.Model.Product;
import com.example.amazonclone2.Model.PurchaseHistory;
import com.example.amazonclone2.Model.User;
import com.example.amazonclone2.Repository.MerchantRepository;
import com.example.amazonclone2.Repository.ProductRepository;
import com.example.amazonclone2.Repository.PurchaseHistoryRepository;
import com.example.amazonclone2.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;


    public  List<PurchaseHistory>  getPurchaseHistories() {
        return purchaseHistoryRepository.findAll();
    }

    public String addPurchaseHistory(PurchaseHistory purchaseHistory) {

        List<Product> products = productRepository.findAll();
        List<Merchant> merchants = merchantRepository.findAll();
        List<User> users = userRepository.findAll();

        for (Product product : products) {

            if (product.getId().equals(purchaseHistory.getProductId())) {

                for (Merchant merchant : merchants) {
                    if (merchant.getId().equals(purchaseHistory.getMerchantId())) {

                        for (User user : users) {
                            if (user.getId().equals(purchaseHistory.getUserId())) {

                                purchaseHistoryRepository.save(purchaseHistory);

                                return null;
                            }
                        }
                        return "user not found";
                    }
                }
                return "merchant not found";
            }
        }

        return "product not found";
    }


    public String updatePurchaseHistory(Integer id, PurchaseHistory purchaseHistory) {
        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Merchant> merchants = merchantRepository.findAll();
        PurchaseHistory oldPurchaseHistory = null;

        boolean purchaseHistoryExists = false;
        for (PurchaseHistory history : purchaseHistoryRepository.findAll()) {
            if (history.getId().equals(id)) {
                purchaseHistoryExists = true;
                oldPurchaseHistory = history;
                break;
            }
        }

        if (!purchaseHistoryExists) {
            return "Purchase history not found";
        }

        for (Product product : products) {
            if (product.getId().equals(purchaseHistory.getProductId())) {

                for (Merchant merchant : merchants) {
                    if (merchant.getId().equals(purchaseHistory.getMerchantId())) {

                        for (User user : users) {
                                if (user.getId().equals(purchaseHistory.getUserId())) {
                                    oldPurchaseHistory.setUserId(purchaseHistory.getUserId());
                                    oldPurchaseHistory.setProductId(purchaseHistory.getProductId());
                                    oldPurchaseHistory.setMerchantId(purchaseHistory.getMerchantId());
                                purchaseHistoryRepository.save(purchaseHistory);
                                return null;
                            }
                        }
                        return "user not found";
                    }
                }
                return "merchant not found";
            }
        }

        return "product not found";
    }

    // Delete PurchaseHistory
    public String deletePurchaseHistory(Integer id) {
        List<PurchaseHistory> purchaseHistories = purchaseHistoryRepository.findAll();

        for (PurchaseHistory purchaseHistory : purchaseHistories) {
            if (purchaseHistory.getId().equals(id)) {
                purchaseHistoryRepository.delete(purchaseHistory);
                return "Purchase history deleted successfully";
            }
        }
        return "Purchase history not found";
    }


}
