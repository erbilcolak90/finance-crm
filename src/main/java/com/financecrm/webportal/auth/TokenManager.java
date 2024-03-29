package com.financecrm.webportal.auth;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.services.CustomUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class TokenManager {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomUserService customUserService;

    private static final int validity = 30 * 60 * 10000;
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email) {
        User user = customUserService.findByEmail(email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(key)
                .compact();
    }

    public boolean logout(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        int tokenDate = (int) claims.get("exp");

        claims.setExpiration(new Date(tokenDate - validity));
        return true;
    }

    public void expireToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        int tokenDate = (int) claims.get("exp");

        claims.setExpiration(new Date(tokenDate - validity));
    }

    public boolean tokenValidate(String token) {
        if (parseUserIdFromToken(token) != null && isExpired(token)) {
            return true;
        } else {
            return false;
        }

    }

    public String parseUserIdFromToken(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return (String) body.get("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

}
