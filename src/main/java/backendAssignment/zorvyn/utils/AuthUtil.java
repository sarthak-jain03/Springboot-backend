package backendAssignment.zorvyn.utils;

import backendAssignment.zorvyn.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserPrincipal userPrincipal){
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("userId", userPrincipal.getId())
                .claim("roles", userPrincipal.getUser().getRoles()
                        .stream()
                        .map(role -> role.name())
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}