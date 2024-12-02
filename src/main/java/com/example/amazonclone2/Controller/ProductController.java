package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.Product;
import com.example.amazonclone2.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity getProducts() {

        List<Product> products = productService.getProducts();

        if (products.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No products found."));
        }
        return ResponseEntity.status(200).body(products);
    }

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        String response = productService.addProduct(product);
        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Product successfully added."));
        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        String response = productService.updatedProduct(id, product);
        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Product successfully updated."));
        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {

        Boolean isDeleted = productService.deleteProduct(id);

        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Product successfully deleted."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Product not found."));
    }



    @GetMapping("/merchant-products/{merchantId}")
    public ResponseEntity getMerchantProducts(@PathVariable Integer merchantId) {

        List<Product> products = productService.getMerchantProducts(merchantId);
        if (products == null){
            return  ResponseEntity.status(400).body(new ApiResponse("Merchant not found in the system"));

        }
        return  ResponseEntity.status(200).body(products);

    }

}
