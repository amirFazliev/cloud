package diploma.cloud.controller;

import diploma.cloud.domain.File;
import diploma.cloud.domain.Users;
import diploma.cloud.service.ServiceApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ControllerApp {

    @Autowired
    private ServiceApp service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        String token = service.authenticate(user.getLogin(), user.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        service.uploadFile(file, token);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename, @RequestHeader("Authorization") String authToken) {
        File file = service.downloadFile(filename, authToken);
        if (file != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("attachment", filename);
            responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(file.getHash().getBytes(), responseHeaders, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename, @RequestHeader("Authorization") String authToken) {
        boolean deletionStatus = service.deleteFile(filename, authToken);
        if (deletionStatus) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
        }
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestParam("filename") String currentFilename, @RequestHeader("Authorization") String authToken, @RequestBody String newFilename) {
        service.editFileName(currentFilename, newFilename);
        return ResponseEntity.ok("File name edited successfully");
    }

    @GetMapping("/list")
    public List<File> getAllFiles(@RequestParam("count") int count, @RequestHeader("Authorization") String authToken) {
        List<File> files = service.getAllFiles();

        if (count < files.size()) {
            return files.subList(0, count);
        }

        return files;
    }
}