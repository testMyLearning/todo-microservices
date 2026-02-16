package com.example.todo.security.jwt;


import com.example.todo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

private final JwtService jwtService;
private final UserService userService;

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);
        if(token==null){
            filterChain.doFilter(request,response);
             return;
        }
if(jwtService.validateToken(token)){
    String email = jwtService.getEmailFromToken(token);
    UserDetails userDetails = userService.loadUserByUsername(email);
    UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
    SecurityContextHolder.getContext().setAuthentication(auth);
}
        filterChain.doFilter(request, response);
}


    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && !header.isEmpty() && !header.isBlank()) {
            return header.substring(7);
        }
        return null;
    }
}