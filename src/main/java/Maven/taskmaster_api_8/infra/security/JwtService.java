package Maven.taskmaster_api_8.infra.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  private static final String SECRET_KEY = "123456789123456789";

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  public String generateToken(String username) {

    long expirationMs = 1000 * 60 * 60;

    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  public String extractUsername(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey()).build()
        .parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  public boolean isTokenExpired(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey()).build()
        .parseSignedClaims(token).getPayload();
    return claims.getExpiration().before(new Date());
  }

  public boolean isTokenValid(String token, String username) {
    String extractedUsername = extractUsername(token);
    return extractedUsername.equals(username) && !isTokenExpired(token);
  }
}
