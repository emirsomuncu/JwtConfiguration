package com.somuncu.SecurityWithJWT.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final String SECRET = "328D3BCDC57304E8287F817A75764A3E719BDC3E7B2F3BACB22E0E0877B30E4D16A2A9EEBD4890DD9EAE5CFC89A9222B0B677D804709B093B9F8A7D09FDFCFDF";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    // JWT tokenı gerekli olan ve istenilen bilgiler ile bu metod içinde oluşturuluyor . Sonuç olarak String değerde tutuluyor .
    public String generateToken(UserDetails userDetails) { //userDetails ile gelen kişi doğrulama safhasından gelen kişi önceki konfigürasyonda yapmıştım

        Map<String ,String> claims = new HashMap<>(); //Jwt payloadında geçmesini istediğim ekstra bilgileri geçirmek için oluşturdum
        claims.put("iss" , "https://secure.genuinecoder.com");
        claims.put("name" , "emir");

        return  Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                    .signWith(generateKey())
                    .compact(); //string hale getiriyor

    }

    // Test tarafından string olarak alıp bu classta tanımladıpım SecretKey bu metod ile önce byte array tipine sonra da SecretKey tipine geri döndürülüyor .
    // Döndürülme sebebi üstte jwt tokenını oluşturmak için SecretKey tipli veriye ihtiyaç duyulması
    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /*
    public String extractUsername(String jwt) {

        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claims.getSubject();

    } --> altta ortak alanı ayrı fonksiyona almazsak böyle gözükür bu metod
     */

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        Claims claims =
            Jwts.parser()
                    .verifyWith(generateKey()) //generate key ile metoda gelen jwtyi doğruluyorum
                    .build()
                    .parseSignedClaims(jwt) //jwt yi bölüyorum
                    .getPayload(); // payload kısmını alıyorum
        return claims;
    }

    public boolean isTokenValid(String jwt) {

         Claims claims = getClaims(jwt);
         return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
