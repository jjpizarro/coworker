package edu.unimagdalena.coworker.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationSeconds;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(UserDetails principal, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        try {
            var claims = parseAllClaims(token);
            return user.getUsername().equalsIgnoreCase(claims.getSubject())
                    && claims.getExpiration().after(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}