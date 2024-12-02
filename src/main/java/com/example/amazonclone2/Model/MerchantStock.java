package com.example.amazonclone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Check(constraints = "stock >= 0")
public class MerchantStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Merchant id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer merchantId;


    @NotNull(message = "product id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer productId;

    @NotNull(message = "Stock quantity cannot be null")
//    @Min(value = 10, message = "Stock quantity must be at least 10")
    @Column(columnDefinition = "int not null")
    private Integer stock;
}
