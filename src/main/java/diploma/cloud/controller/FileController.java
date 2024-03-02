package diploma.cloud.controller;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Login;
import diploma.cloud.service.FileService;
import diploma.cloud.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> uploadFile(@Valid @RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) throws IOException {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        File uploadedFile = fileService.uploadFile(file, filename);
        return ResponseEntity.ok("File uploaded successfully with ID: " + uploadedFile.getId());
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String authToken) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<File> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, @RequestHeader("auth-token") String authToken) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        fileService.deleteFile(id);
        return ResponseEntity.ok("File with ID " + id + " deleted successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        if (userService.login(login)) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String authToken) {
        userService.logout(authToken);
        return ResponseEntity.ok("Logged out successfully");
    }
}