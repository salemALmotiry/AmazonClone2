package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.Category;
import com.example.amazonclone2.Model.Merchant;
import com.example.amazonclone2.Model.MerchantStock;
import com.example.amazonclone2.Model.Product;
import com.example.amazonclone2.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MerchantStockService {




    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantStockRepository merchantStockRepository;

    public List<MerchantStock> getMerchantStocks() {

        return merchantStockRepository.findAll();
    }


    public String addMerchantStock(MerchantStock MerchantStock){

        List<Product> products = productRepository.findAll();
        List<Merchant> merchants = merchantRepository.findAll();

        for (Product product : products) {

            if (product.getId().equals(MerchantStock.getProductId())) {

                for (Merchant merchant : merchants) {
                    if (merchant.getId().equals(MerchantStock.getMerchantId())) {
                        merchantStockRepository.save(MerchantStock);
                        return null;
                    }
                }
                return "merchant not found";



            }
        }

            return "product not found";

    }



    public String updatedMerchantStock(Integer id , MerchantStock merchantStock){

        List<MerchantStock> merchantStocks = merchantStockRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Merchant> merchants = merchantRepository.findAll();

        for (MerchantStock oldMerchantStock : merchantStocks) {
            if (oldMerchantStock.getId().equals(id)) {

                for (Product product : products) {

                    if (product.getId().equals(oldMerchantStock.getProductId())) {
                        for (Merchant merchant : merchants) {
                            if (merchant.getId().equals(merchantStock.getMerchantId())) {
                                oldMerchantStock.setMerchantId(merchantStock.getMerchantId());
                                oldMerchantStock.setStock(merchantStock.getStock());
                                oldMerchantStock.setProductId(merchantStock.getProductId());
                                merchantStockRepository.save(oldMerchantStock);
                                return null;
                            }
                        }
                        return "Merchant not found";
                    }
                }
                return "Product not found";
            }
        }

        return "MerchantStock not found";
    }





    public Boolean deleteMerchantStock(Integer id){


        // Efficiently fetches all MerchantStocks once and iterates to find and delete the matching one.
        List<MerchantStock> MerchantStocks = merchantStockRepository.findAll();

        for (MerchantStock merchantStock : MerchantStocks) {
            if (merchantStock.getId().equals(id)){
                merchantStockRepository.delete(merchantStock);
                return true;
            }
        }

        return false;

    }

    public String reStock(Integer productId, Integer merchantId, int amount,boolean reStock) {

        if (amount < 0 && reStock) {
            return "Amount cannot be negative";
        }

        boolean productFound = false;
        boolean merchantFound = false;

        for (MerchantStock merchantStock : merchantStockRepository.findAll()) {

            if (merchantStock.getProductId().equals(productId)) {
                productFound = true;

                if (merchantStock.getMerchantId().equals(merchantId)) {
                    merchantFound = true;

                    if (merchantStock.getStock() + (amount) >= 0) {
                        merchantStock.setStock(merchantStock.getStock() + (amount));
                        updatedMerchantStock(merchantStock.getId(),merchantStock);
                        return null;
                    } else {
                        return "Product is out of stock";
                    }
                }
            }
        }

        if (!productFound && !merchantFound) {
            return "Product and Merchant not found";
        } else if (!productFound) {
            return "Product not found";
        } else if (!merchantFound) {
            return "Merchant not found";
        }

        return "Unexpected error occurred"; // Fallback message
    }

}
