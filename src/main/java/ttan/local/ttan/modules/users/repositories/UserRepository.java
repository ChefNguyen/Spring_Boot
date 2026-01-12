package ttan.local.ttan.modules.users.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ttan.local.ttan.modules.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{
    // Spring Data JPA tự generate query method
    User findByEmail(String email);
}
