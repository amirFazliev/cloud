package diploma.cloud.service;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Users;
import diploma.cloud.repository.RepositoryApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceApp {
    @Autowired
    private RepositoryApp repository;

    public String authenticate(String login, String password) {
        if (repository.checkCredentials(login, password)) {
            return generateToken();
        } else {
            return null;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }


    public void uploadFile(MultipartFile file, String authToken) throws IOException {
        Users user = repository.findByAuthToken(authToken);
        if (user != null) {
            File newFile = new File();
            newFile.setFilename(file.getOriginalFilename());
            newFile.setHash(Arrays.toString(file.getBytes()));
            repository.save(newFile);
        }
    }

    public File downloadFile(String filename, String authToken) {
        Users user = repository.findByAuthToken(authToken);
        if (user != null) {
            return repository.findByFilename(filename);
        }
        return null;
    }

    public boolean deleteFile(String filename, String authToken) {
        Users user = repository.findByAuthToken(authToken);
        if (user != null) {
            repository.deleteByFilename(filename);
            return true;
        }
        return false;
    }

    public void editFileName(String currentFilename, String newFilename) {
        File file = repository.findByFilename(currentFilename);
        if (file != null) {
            file.setFilename(newFilename);
            repository.save(file);
        }
    }

    public List<File> getAllFiles() {
        return repository.findAll();
    }
}
