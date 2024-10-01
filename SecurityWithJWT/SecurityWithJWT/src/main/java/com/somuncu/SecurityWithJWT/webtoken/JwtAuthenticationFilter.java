package com.somuncu.SecurityWithJWT.webtoken;

import com.somuncu.SecurityWithJWT.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService ;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) { //headerda authorization kısmında bilgi yoksa direkt olarak erişim engellenir
            filterChain.doFilter(request , response); // isteği bir sonraki filtera yönlendirir . Bu filter ile işin bittiğini gösterir
            return; //alltaki kodların yürütülmesini engeller
        }

        String jwt = authHeader.substring(7); //Jwtnin başladığı stringi işaret ederek onu yakalar
        String username = jwtService.extractUsername(jwt); //metod ile jwt gövdesindeki isim bilgisini alır
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username); //isme sahip kullanıcının UserDetails halini alıyor (authentication)
            if(userDetails !=null && jwtService.isTokenValid(jwt)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                ); //user contexte setlenmek için tokena yükleniyor
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //kimin giriş yapmak istediğine dair bilgi almaya yarar
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); //context logged in olarak işaretleniyor
            }
        }
        filterChain.doFilter(request , response);
    }
}
