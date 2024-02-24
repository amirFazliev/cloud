package diploma.cloud.controller;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Login;
import diploma.cloud.service.FileService;
import diploma.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) throws IOException {
        // Логика загрузки файла
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        fileService.uploadFile(file, filename);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String authToken) {
        // Логика получения списка файлов
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<File> fileList = fileService.getAllFiles();
        return ResponseEntity.ok(fileList);
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, @RequestHeader("auth-token") String authToken) {
        // Логика удаления файла
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        fileService.deleteFile(id);
        return ResponseEntity.ok("File deleted successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        if (userService.login(login)) {
            String authToken = userService.generateAuthToken();
            return ResponseEntity.ok(authToken);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String authToken) {
        userService.logout(authToken);
        return ResponseEntity.ok("Logout successful");
    }
}