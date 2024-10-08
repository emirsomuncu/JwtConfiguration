package com.somuncu.SecurityWithJWT.dao;

import com.somuncu.SecurityWithJWT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer> {

    public Optional<User> findUserByUsername(String username);

}
