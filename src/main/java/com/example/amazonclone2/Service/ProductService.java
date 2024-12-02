package com.example.amazonclone2.Service;


import com.example.amazonclone2.Model.*;
import com.example.amazonclone2.Repository.CategoryRepository;
import com.example.amazonclone2.Repository.CouponRepository;
import com.example.amazonclone2.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {




    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;

    public List<Product> getProducts() {

        return productRepository.findAll();
    }


    public String addProduct(Product product){

        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getId().equals(product.getCategoryId())) {
                productRepository.save(product);
               return null;

            }
        }

        return "Category not found";


    }



    public String updatedProduct(Integer id , Product product){

        // Efficiently fetches all Product once and iterates to find and update the matching one.
        List<Product> products = productRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        boolean flag = false;
        for (Category category : categories) {
            if (category.getId().equals(product.getCategoryId())) {
                flag = true;
                break;

            }
        }

        if (!flag) {
            return "Category not found";

        }

        for (Product oldProduct : products) {
            if(oldProduct.getId().equals(id)){
                oldProduct.setName(product.getName());
                oldProduct.setPrice(product.getPrice());
                oldProduct.setCategoryId(product.getCategoryId());
                oldProduct.setCouponCode(product.getCouponCode());

                productRepository.save(oldProduct);
                return null;

            }
        }


        return "Product not found";


    }


    public Boolean deleteProduct(Integer id){


        // Efficiently fetches all Products once and iterates to find and delete the matching one.
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getId().equals(id)){
                productRepository.delete(product);
                return true;
            }
        }

        return false;

    }


    public Product getProduct(Integer id){
        return productRepository.findById(id).get();
    }

    // get merchant products
    public List<Product> getMerchantProducts(Integer merchantId) {

        for (Merchant merchant : merchantService.getMerchants()){

            if (merchant.getId().equals(merchantId)){

                ArrayList<Product> merchantProducts = new ArrayList<Product>();
                for (MerchantStock merchantStock : merchantStockService.getMerchantStocks()) {

                    if (merchantStock.getMerchantId().equals(merchantId)){

                        for (Product product : getProducts()){
                            if (product.getId().equals(merchantStock.getProductId())){
                                merchantProducts.add(product);
                            }
                        }
                    }

                }
                return merchantProducts;


            }
        }



        return null;

    }


    public String getStocks(Integer productId){

        int stock=0;
        for (MerchantStock merchantStock: merchantStockService.getMerchantStocks()){

            if (merchantStock.getProductId().equals(productId)){
                stock+= merchantStock.getStock();
            }
        }

        if (stock==0){
            return "Product not found";
        }


        return String.valueOf(stock);
    }
}
