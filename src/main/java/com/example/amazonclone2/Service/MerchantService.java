package com.example.amazonclone2.Service;

import com.example.amazonclone2.Model.Category;
import com.example.amazonclone2.Model.Merchant;
import com.example.amazonclone2.Repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MerchantService {



    private final MerchantRepository merchantRepository;

    public List<Merchant> getMerchants() {

        return merchantRepository.findAll();
    }


    public void addMerchant(Merchant merchant){

        merchantRepository.save(merchant);

    }



    public Boolean updatedMerchant(Integer id , Merchant merchant){

        // Efficiently fetches all Merchant once and iterates to find and update the matching one.
        List<Merchant> merchants = merchantRepository.findAll();

        for (Merchant oldMerchant : merchants) {
            if(oldMerchant.getId().equals(id)){

                oldMerchant.setName(oldMerchant.getName());

                merchantRepository.save(oldMerchant);
                return true;

            }
        }


        return false;


    }


    public Boolean deleteMerchant(Integer id){


        // Efficiently fetches all merchants once and iterates to find and delete the matching one.
        List<Merchant> merchants = merchantRepository.findAll();

        for (Merchant merchant : merchants) {
            if (merchant.getId().equals(id)){
                merchantRepository.delete(merchant);
                return true;
            }
        }

        return false;

    }
}
