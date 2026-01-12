package ttan.local.ttan.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Jwtconfig 
{
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String getSecretKey() 
    {
        return secretKey;
    }

    public long getExpiration()
    {
        return expiration;
    }
    
}
