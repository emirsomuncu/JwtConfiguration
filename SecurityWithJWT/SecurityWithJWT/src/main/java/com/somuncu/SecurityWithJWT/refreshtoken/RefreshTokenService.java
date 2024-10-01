package com.somuncu.SecurityWithJWT.refreshtoken;

import com.somuncu.SecurityWithJWT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${refresh.token.expires.in}")
    private Long expireSeconds;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository ;

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if (token == null) {
            token = new RefreshToken() ;
            token.setUser(user);
        } // bu sayede bir kullanıcının daha önceden refresh tokenı varsa ona ait alttaki veriler değişecekk yeni bir refresh token verisi oluşmaycacak
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public Boolean isRefreshTokenExpired(RefreshToken token ) {
        return token.getExpiryDate().before(new Date()); //Refresh tokenının geçerlilik süresi geçmiş mi diye bakıyoruz
    }

    public RefreshToken getByUser(int userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
