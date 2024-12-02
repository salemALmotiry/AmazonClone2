package com.example.amazonclone2.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Check(constraints = "price>0")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @NotEmpty(message = "Product name cannot be null")
    @Size(min = 5 , max = 50 , message = "Product name must be between 5 and 50")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 _-]{5,50}$", message = "RegEx rules:" +
            "- Length: between 5 to 50 characters.\n" +
            "- Allowed Characters: Letters (both uppercase and lowercase), numbers, spaces, hyphens (`-`), underscores (`_`), and possibly special characters (like `&`, `@`, etc.).\n" +
            "- Starting Character: It can start with a letter or a number.\n" +
            "- No leading or trailing spaces.")

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotNull(message = "Product price cannot be null ")
    @Positive(message = "Product price accept only positive")

    @Column(columnDefinition = "double not null")
    private Double price;

    @NotNull(message = "Category id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer categoryId;

    @JsonIgnore
    @Column(columnDefinition = "varchar(15)")
    private String couponCode;







}
