package com.example.amazonclone2.Repository;

import com.example.amazonclone2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
