package ttan.local.ttan.database.seeder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DatabaseSeeder implements CommandLineRunner
{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run( String... args ) throws Exception
    {
        System.out.println( "Database is seeding..." );
    }

    private boolean isTableEmpty()
    {
        Long count = (Long)entityManager.createQuery( "SELECT COUNT(id) FROM users" ).getSingleResult();
        return count == 0;
    }
}
