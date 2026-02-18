package com.niteshgiri.AthleteArena.config;

import com.niteshgiri.AthleteArena.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class AuthUtil {
    @Value("${jwt.secretKey}")
    private String  secretKey;

    private SecretKey getSecretkey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId",user.getId().toString())
                .claim("roles", user.getRoles().toString())
                .signWith(getSecretkey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*15))
                .compact();
    }

    public String getUsernameFromToken(String token){
      return     Jwts.parserBuilder()
                .setSigningKey(getSecretkey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
