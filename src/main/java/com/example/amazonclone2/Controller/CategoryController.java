package com.example.amazonclone2.Controller;

import com.example.amazonclone2.ApiResponse.ApiResponse;
import com.example.amazonclone2.Model.Category;
import com.example.amazonclone2.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amazon-clone/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity getCategories() {

        List<Category> categories = categoryService.getCategories();

        if (categories.isEmpty()) {
            return ResponseEntity.status(200).body(new ApiResponse("No categories found."));
        }
        return ResponseEntity.status(200).body(categories);
    }

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category successfully added."));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable Integer id, @RequestBody @Valid Category category, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        Boolean isUpdated = categoryService.updateCategory(id, category);

        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Category successfully updated."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Category not found."));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {

        Boolean isDeleted = categoryService.deleteCategory(id);

        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Category successfully deleted."));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Category not found."));
    }
}
