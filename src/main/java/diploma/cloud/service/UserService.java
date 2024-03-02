package diploma.cloud.service;

import diploma.cloud.domain.Login;
import diploma.cloud.domain.User;
import diploma.cloud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean isUserAuthenticated(String authToken) {
        return userRepository.existsByAuthToken(authToken);
    }

    public boolean login (@Valid @RequestBody Login login) {
        User user = userRepository.findByUsernameAndPassword(login.getLogin(), login.getPassword());
        if (user != null) {
            String authToken = generateAuthToken();
            user.setAuthToken(authToken);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void logout (String authToken) {
        User user = userRepository.findByAuthToken(authToken);
        if (user != null) {
            user.setAuthToken(null);
            userRepository.save(user);
        }
    }
}
