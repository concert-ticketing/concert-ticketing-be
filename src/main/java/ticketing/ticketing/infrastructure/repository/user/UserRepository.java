package ticketing.ticketing.infrastructure.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
