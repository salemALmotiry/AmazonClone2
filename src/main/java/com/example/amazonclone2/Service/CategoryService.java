package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.Category;
import com.example.amazonclone2.Model.User;
import com.example.amazonclone2.Repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {


    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(){

        return categoryRepository.findAll();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }



    public Boolean updateCategory(Integer id , Category category){


        // Efficiently fetches all categories once and iterates to find and update the matching one.
        List<Category> categories = categoryRepository.findAll();

        for (Category oldCategory : categories) {
            if(oldCategory.getId().equals(id)){
                oldCategory.setName(category.getName());
                categoryRepository.save(oldCategory);
                return true;

            }
        }


        return false;


    }


    public Boolean deleteCategory(Integer id){


        // Efficiently fetches all categories once and iterates to find and delete the matching one.
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getId().equals(id)){
                categoryRepository.delete(category);
                return true;
            }
        }

            return false;

    }

}
