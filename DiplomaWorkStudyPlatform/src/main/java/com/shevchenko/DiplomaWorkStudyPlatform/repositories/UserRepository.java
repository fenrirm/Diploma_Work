package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

    User findUserById(int userId);

}
