package ticketing.ticketing.infrastructure;


import org.springframework.data.repository.CrudRepository;
import ticketing.ticketing.domain.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {



}
