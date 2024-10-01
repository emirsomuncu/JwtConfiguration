package com.somuncu.SecurityWithJWT;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretMakerTest {

    @Test
    public void generateSecretKey() {

        SecretKey key = Jwts.SIG.HS512.key().build(); //Secretkeyi belirtilen algoritmaya göre oluşturur .

        String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded()); //Key tipindeki değeri .getEncoded ile bytearray tipine dönüştürür .
        //Typeconverter ilede onu onaltılık sayı sistemine çevirir (sayı ve harflerden oluşan sistem)

        System.out.printf("\nSecret Key : [%s]\n" , encodedKey);
    }

}