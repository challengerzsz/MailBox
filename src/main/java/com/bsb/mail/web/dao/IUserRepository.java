package com.bsb.mail.web.dao;

import com.bsb.mail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: zeng
 * @Date: 2018/10/24 21:01
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user.password FROM User user WHERE user.username =:username")
    String getPassword(@Param("username") String username);

    @Query("FROM User user WHERE user.username =:username AND user.password =:password")
    User login(@Param("username") String username, @Param("password") String password);

    @Query("FROM User user WHERE user.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT user.imageUrl FROM User user WHERE user.username = :username")
    String getUserImage(@Param("username") String username);

}
