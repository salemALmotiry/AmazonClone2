package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.MerchantStock;
import com.example.amazonclone2.Service.MerchantStockService;
import com.example.amazonclone2.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/merchant-stock")
public class MerchantStockController {

    private final MerchantStockService merchantStockService;
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity getMerchantStocks() {

        List<MerchantStock> merchantStocks = merchantStockService.getMerchantStocks();

        if (merchantStocks.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No merchant stocks found."));
        }
        return ResponseEntity.status(200).body(merchantStocks);
    }

    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        String response = merchantStockService.addMerchantStock(merchantStock);
        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock successfully added."));
        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable Integer id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        String response = merchantStockService.updatedMerchantStock(id, merchantStock);
        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock successfully updated."));
        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable Integer id) {

        Boolean isDeleted = merchantStockService.deleteMerchantStock(id);

        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock successfully deleted."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found."));
    }

    @PutMapping("/restock/{productId}/{merchantId}/{amount}")
    public ResponseEntity reStock(@PathVariable Integer productId,@PathVariable Integer merchantId, @PathVariable int amount) {

        String response = merchantStockService.reStock(productId, merchantId, amount,true);

        if (response == null) {
            return ResponseEntity.status(200).body(new ApiResponse("Product successfully restocked."));
        }

        return ResponseEntity.status(400).body(new ApiResponse(response));
    }


    @GetMapping("/get-stocks/{merchantId}")
    public ResponseEntity getStocks(@PathVariable Integer merchantId){

        String response = productService.getStocks(merchantId);
        if (response.startsWith("Product")) {
            return ResponseEntity.status(400).body(new ApiResponse(response));
        }
        return ResponseEntity.status(200).body(new ApiResponse("This product stocks: "+response));

    }
}
