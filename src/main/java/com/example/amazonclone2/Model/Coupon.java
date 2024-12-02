package com.example.amazonclone2.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Coupon {

    @Id
    @Column(columnDefinition = "varchar(15)")
    private String couponCode;

    @NotNull(message = "Discount percent cannot be null")
    @Positive(message = "Discount percent accept positive only")
    @Column(columnDefinition = "int not null")
    private Integer discount;



    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime start;

    @NotNull(message = "End time must be not null")
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime end;



}
