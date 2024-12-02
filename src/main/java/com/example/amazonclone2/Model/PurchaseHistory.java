package com.example.amazonclone2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User id must be not null")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "Product id must be not null")
    @Column(columnDefinition = "int not null")
    private Integer productId;

    @NotNull(message = "Merchant id must be not null")
    @Column(columnDefinition = "int not null")
    private Integer merchantId;

    @NotNull(message = "price must be not null")
    @Column(columnDefinition = "double not null")
    private Double price;


    public PurchaseHistory(Integer userId, Integer productId, Integer merchantId, Double productPrice) {
        this.userId = userId;
        this.productId = productId;
        this.merchantId = merchantId;
        this.price = productPrice;

    }
}
