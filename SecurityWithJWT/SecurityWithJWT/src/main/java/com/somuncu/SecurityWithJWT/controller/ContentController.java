package com.somuncu.SecurityWithJWT.controller;

import com.somuncu.SecurityWithJWT.security.CustomUserDetailsService;
import com.somuncu.SecurityWithJWT.webtoken.FormLogin;
import com.somuncu.SecurityWithJWT.webtoken.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {


    @GetMapping("/home")
    public String handleWelcome(){
        return "Welcome to home";
    }

    @GetMapping("/user/home")
    public String handleUserWelcome(){
        return "Welcome to USER home";
    }

    @GetMapping("/admin/home")
    public String handleAdminWelcome(){
        return "Welcome to ADMIN home";
    }


}


