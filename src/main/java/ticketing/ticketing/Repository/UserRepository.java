package ticketing.ticketing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
