package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.Merchant;
import com.example.amazonclone2.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity getMerchants() {

        List<Merchant> merchants = merchantService.getMerchants();

        if (merchants.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No merchants found."));
        }
        return ResponseEntity.status(200).body(merchants);
    }

    @PostMapping("/add")
    public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant successfully added."));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant(@PathVariable Integer id, @RequestBody @Valid Merchant merchant, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        Boolean isUpdated = merchantService.updatedMerchant(id, merchant);

        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant successfully updated."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchant(@PathVariable Integer id) {

        Boolean isDeleted = merchantService.deleteMerchant(id);

        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant successfully deleted."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant not found."));
    }
}
