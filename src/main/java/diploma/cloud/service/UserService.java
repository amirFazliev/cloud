package diploma.cloud.service;

import diploma.cloud.domain.Login;
import diploma.cloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Методы для работы с пользователями isUserAuthenticated, login, generateAuthToken и logout

    public boolean isUserAuthenticated(String authToken) {

    }

    public boolean login (Login login) {

    }

    public String generateAuthToken() {

    }

    public void logout (String authToken) {

    }
}
