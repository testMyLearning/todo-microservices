package com.example.todo.security.jwt;

import com.example.todo.model.entity.User;
import com.example.todo.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    Logger log= LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    public long getJwtExpirationMs() {
        return jwtExpirationMs/(1000*60*60);
    }
    private SecretKey getSigningKey() {
        // Создаем SecretKey из секретной строки
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

public String generateJwtToken(User user){
    Claims claims = Jwts.claims()
            .subject(user.getEmail())
            .add("id",user.getId())
            .add("name",user.getName())
            .add("role", Role.USER)
            .build();

    return Jwts.builder()
            .claims(claims)
            .signWith(getSigningKey())
            .compact();
}
    public boolean validateToken(String token) {
        try {
            // Используем verifyWith с SecretKey - ЭТО ПРАВИЛЬНО
            Jwts.parser()
                    .verifyWith(getSigningKey())     // ← именно так, как в твоей сигнатуре
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    public Claims parseToken(String token) {
        return Jwts.parser()                 // ← вот он - метод из твоего класса!
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {
        return parseToken(token).getSubject();

    }
}