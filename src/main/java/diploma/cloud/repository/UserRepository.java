package diploma.cloud.repository;

import diploma.cloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAuthToken(String authToken);
    User findByUsernameAndPassword(String username, String password);
    User findByAuthToken(String authToken);

    Optional<User> findById(Long id);

    void deleteById(Long id);


}
