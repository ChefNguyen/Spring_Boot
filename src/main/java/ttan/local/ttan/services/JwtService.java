package ttan.local.ttan.services;

import org.springframework.stereotype.Service;
import ttan.local.ttan.config.Jwtconfig;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final Jwtconfig jwtconfig;
    private final Key key;

    public JwtService(Jwtconfig jwtconfig) {
        this.jwtconfig = jwtconfig;
        // Chuyển secretKey thành bytes để tạo Key
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtconfig.getSecretKey()));
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtconfig.getExpiration());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

}