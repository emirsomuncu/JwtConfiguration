package com.somuncu.SecurityWithJWT.controller;

import com.somuncu.SecurityWithJWT.dao.UserDao;
import com.somuncu.SecurityWithJWT.entity.User;
import com.somuncu.SecurityWithJWT.refreshtoken.AuthResponse;
import com.somuncu.SecurityWithJWT.refreshtoken.RefreshRequest;
import com.somuncu.SecurityWithJWT.refreshtoken.RefreshToken;
import com.somuncu.SecurityWithJWT.refreshtoken.RefreshTokenService;
import com.somuncu.SecurityWithJWT.security.CustomUserDetailsService;
import com.somuncu.SecurityWithJWT.webtoken.FormLogin;
import com.somuncu.SecurityWithJWT.webtoken.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager ;

    @Autowired
    private JwtService jwtService ;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserDao userDao ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private RefreshTokenService refreshTokenService ;


    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        this.userDao.save(user);

        return "User created successfully";
    }

    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@RequestBody FormLogin formLogin) {

        //bu authenticationmanager yardımı ile formlogin classından gelen bilgileri basic auth konfumuza dahil edebildik conf classındaki bean sayesinde oldu bu.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.username(), formLogin.password()));

        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(customUserDetailsService.loadUserByUsername(formLogin.username()));
            //login olan kişinin hem access hemde refresh token bilgilerini döndürüyorum
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshTokenService.createRefreshToken(this.userDao.findUserByUsername(formLogin.username()).get()));
            authResponse.setMessage("User logged in");
            return authResponse ;
        }
        else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {

        AuthResponse authResponse = new AuthResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());

        //bodyden gelen refresh tokenı ile dbdeki refresh tokenı kıyaslanıyor ve referesh tokenın geçerlilik süresi geçmiş mi diye bakılıyor
        if(token.getToken().equals(refreshRequest.getRefreshToken()) && !refreshTokenService.isRefreshTokenExpired(token)) {

            int id = refreshRequest.getUserId();
            Optional<User> user = userDao.findById(id);
            String username = user.get().getUsername();
            String accessToken = jwtService.generateToken(customUserDetailsService.loadUserByUsername(username));

            authResponse.setMessage("Access token refreshed successfully");
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(token.getToken());

            return new ResponseEntity<>(authResponse , HttpStatus.OK);
        }
        else {
            authResponse.setMessage("Refresh token is not valid");
            return new ResponseEntity<>(authResponse,HttpStatus.UNAUTHORIZED);
        }
    }


}