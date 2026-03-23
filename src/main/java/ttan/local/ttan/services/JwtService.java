package ttan.local.ttan.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ttan.local.ttan.config.Jwtconfig;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtService {
    private final Jwtconfig jwtconfig;
    private final SecretKey key;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);


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
                .issuer("ttan.local.app") // Thêm Issuer để bảo vệ thêm
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
        // Hàm này sẽ tự động ném ra các Exception nếu token không đúng định dạng,
        // hết hạn, sai chữ ký, hoặc sai issuer. Các Exception này sẽ được bắt
        // và xử lý trong JwtAuthFilter.
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .requireIssuer("ttan.local.app") // Bỏ comment nếu muốn bẳt buộc mọi JWT phải có issuer này
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId").toString();
    }


    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer("ttan.local.app") // Bỏ comment nếu muốn bắt buộc phải có issuer này
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Nếu đến được đây thì token đã chuẩn định dạng, không hết hạn.
            // Nên ta chỉ cần kiểm tra xem subject có khớp với username không
            String email = claims.getSubject();
            if (!email.equals(userDetails.getUsername())) {
                logger.error("Token subject {} does not match UserDetails {}", email, userDetails.getUsername());
                return false;
            }

            // TODO: Logic kiểm tra Blacklist vào Cache/Redis tại đây
            // if (isTokenInBlacklist(token)) return false;

            return true;
        } catch (Exception e) {
            logger.error("Token is invalid: {}", e.getMessage());
            return false;
        }
    }
}