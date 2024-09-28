package com.example.BeFETest.JWT;

import com.example.BeFETest.DTO.coinDTO.StrategyCommonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private long jwtExpirationInMs;

    @Value("${jwt.refresh-expiration-time}")
    private long refreshJwtExpirationInMs;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(Long userId, String email, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .claim("email", email)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshJwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String addCommonStrategyDataToken(String token, StrategyCommonDTO strategyCommonDTO){
        try {
            Claims claims = getClaimsFromToken(token);

            String strategyJSON = objectMapper.writeValueAsString(strategyCommonDTO);
            claims.put("strategyCommon", strategyJSON);

            // 새로운 토큰 생성 후 로그 추가
            String newToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(claims.getExpiration())
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            System.out.println("New token generated with strategyCommon: " + newToken); // 로그 추가

            return newToken;

        } catch (JsonProcessingException e){
            throw new RuntimeException("Error Adding strategy to token", e);
        }
    }

    public StrategyCommonDTO extractCommonStrategyDataFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String strategyJson = (String) claims.get("strategyCommon");

            if (strategyJson == null) {
                System.err.println("No strategyCommon found in the token.");
                return null;
            }

            // JSON 문자열을 StrategyCommonDTO로 변환
            return objectMapper.readValue(strategyJson, StrategyCommonDTO.class);
        } catch (Exception e) {
            System.err.println("Error extracting strategy from token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public Long getUserIdFromToken(String token) {
        try {
            System.out.println("Token received: " + token); // Add this line
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            System.err.println("Error extracting user ID from token: " + e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }


    public Long getRefreshExpirationInMs() {
        return refreshJwtExpirationInMs;
    }

    public Long getExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getExpiration().getTime();
    }


    public Claims getClaimsFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}

