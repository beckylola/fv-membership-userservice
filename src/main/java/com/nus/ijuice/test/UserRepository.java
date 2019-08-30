package com.nus.ijuice.test;

import com.nus.ijuice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String emailid);
    User findUserByEmail(String email);

}