package diploma.cloud.controller;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Login;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import diploma.cloud.service.FileService;
import diploma.cloud.service.UserService;

import java.io.IOException;
import java.util.List;


@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        if (userService.login(login)) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String authToken) {
        userService.logout(authToken);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PutMapping("/file/{id}")
    public ResponseEntity<String> editFileName(@PathVariable Long id, @RequestHeader("auth-token") String authToken, @RequestParam("name") String name) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (fileService.editFileName(id, name)) {
            return ResponseEntity.ok("File name updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating file name");
        }
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestParam("file") MultipartFile file) throws IOException {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        File uploadedFile = fileService.uploadFile(file, filename);
        return ResponseEntity.ok("File uploaded successfully with ID: " + uploadedFile.getId());
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        fileService.deleteFile(filename);
        return ResponseEntity.ok("File with name " + filename + " deleted successfully");
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        File file = fileService.getFileByName(filename);
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", filename);
        return new ResponseEntity<>(file.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileContent(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestParam("file") MultipartFile file) throws IOException {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        fileService.editFileContent(file, filename);
        return ResponseEntity.ok("File content updated successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String authToken) {
        if (!userService.isUserAuthenticated(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<File> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    
}