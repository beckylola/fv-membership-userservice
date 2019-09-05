package com.nus.ijuice.repository;

import com.nus.ijuice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String emailid);
    User findUserByEmail(String email);
    User findByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "update users u set u.reset_password = ?1 where u.emailid = ?2",nativeQuery=true)
    int updateResetPassword(int val,String emailId);

}