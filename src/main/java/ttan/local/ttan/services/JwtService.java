package ttan.local.ttan.services;

import org.springframework.stereotype.Service;
import ttan.local.ttan.config.Jwtconfig;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.function.Function;

@Service
public class JwtService {
    private final Jwtconfig jwtconfig;
    private final SecretKey key;

    public JwtService(Jwtconfig jwtconfig) {
        this.jwtconfig = jwtconfig;
        // Chuyển secretKey thành bytes để tạo Key
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtconfig.getSecretKey()));
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtconfig.getExpiration());

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // public String extractUsername(String token) {
    // return extractClaim(token, Claims::getSubject);
    // }

    // public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    // final Claims claims = extractAllClaims(token);
    // return claimsResolver.apply(claims);
    // }

    // private Claims extractAllClaims(String token) {
    // return Jwts.parser()
    // .verifyWith(key)
    // .build()
    // .parseSignedClaims(token)
    // .getPayload();
    // }
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key) // Dùng verifyWith thay cho setSigningKey
                .build()
                .parseSignedClaims(token) // Trả về Jws<Claims>
                .getPayload();
        return claims.get("userId").toString();
    }

}