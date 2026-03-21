package ttan.local.ttan.database.seeder;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ttan.local.ttan.modules.users.entities.User;
import ttan.local.ttan.modules.users.repositories.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (isTableEmpty()) {
            // Thêm mục danh mục user trước để thoả mãn khoá ngoại (id = 1)
            entityManager.createNativeQuery("INSERT IGNORE INTO user_catalogues (id, name) VALUES (1, 'Admin')").executeUpdate();

            String encodedPassword = passwordEncoder.encode("123456");

            // entityManager.createNativeQuery( "INSERT INTO users( name, email, password,
            // user_catalogue_id, phone ) VALUES ( ?, ?, ?, ?, ? )" )
            // .setParameter( 1, "Tân Tuấn Tú" )
            // .setParameter( 2, "tantuantu@gmail.com" )
            // .setParameter( 3, encodedPassword )
            // .setParameter( 4, 1 )
            // .setParameter( 5, "0388034748" )
            // .executeUpdate();

            // User user = new User();
            // user.setName("Tân Tuấn Tú");
            // user.setEmail("tantuantu@gmail.com");
            // user.setPassword(encodedPassword);
            // user.setUserCatalogueId(1L);
            // user.setPhone("0388034748");
            // userRepository.save(user);

            User user = new User();
            user.setName("Tân Tuấn Tú");
            user.setEmail("tantuantu@gmail.com");
            user.setPassword(encodedPassword);
            user.setUserCatalogueId(1L);
            user.setPhone("0388034748");
            userRepository.save(user);
            logger.info("Seeding data...");
        }
    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        return count == 0;
    }  
}
