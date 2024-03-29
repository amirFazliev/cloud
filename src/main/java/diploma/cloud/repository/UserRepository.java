package diploma.cloud.repository;

import diploma.cloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAuthToken(String authToken);

    User findByUsernameAndPassword(String username, String password);

    User findByAuthToken(String authToken);
}
