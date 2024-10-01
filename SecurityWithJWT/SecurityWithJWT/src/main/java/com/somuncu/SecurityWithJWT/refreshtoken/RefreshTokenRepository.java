package com.somuncu.SecurityWithJWT.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Integer> {

    public RefreshToken findByUserId(int userId);
}
