package com.example.amazonclone2.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Merchant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Merchant name cannot be null")
    @Size(min = 3, max = 49, message = "Merchant name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9 _-]{2,49}$", message = "Merchant name RegEx rules:" +
            "- Length: between 3 to 50 characters.\n" +
            "- No leading or trailing spaces.\n" +
            "- Must start with a letter.")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;


    

}
