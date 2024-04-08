package diploma.cloud;

import diploma.cloud.controller.ControllerApp;
import diploma.cloud.domain.File;
import diploma.cloud.domain.Users;
import diploma.cloud.service.ServiceApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ControllerTest {

    private ServiceApp service = Mockito.mock(ServiceApp.class);
    private ControllerApp controller = new ControllerApp(service);


    @Test
    public void testLogin() {
        Users user = new Users("username", "password");
        when(service.authenticate(user.getLogin(), user.getPassword())).thenReturn("token");

        ResponseEntity<String> response = controller.login(user);

        assertEquals("token", response.getBody());
    }

    @Test
    public void testUploadFile() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("testFile");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        doNothing().when(service).uploadFile(any(MultipartFile.class), any(String.class));

        ResponseEntity<String> response = controller.uploadFile(file, "authToken");

        assertEquals("File uploaded successfully", response.getBody());
    }

    @Test
    public void testDownloadFile() {
        byte[] fileContent = "Test file content".getBytes();
        when(service.downloadFile(any(String.class))).thenReturn(fileContent);

        ResponseEntity<byte[]> response = controller.downloadFile("fileName", "authToken");

        assertEquals(fileContent, response.getBody());
    }

    @Test
    public void testDeleteFile() {
        doNothing().when(service).deleteFile(any(String.class), any(String.class));

        ResponseEntity<String> response = controller.deleteFile("fileName", "authToken");

        assertEquals("File deleted successfully", response.getBody());
    }

    @Test
    public void testEditFileName() {
        doNothing().when(service).editFileName(any(String.class), any(String.class), any(String.class));

        ResponseEntity<String> response = controller.editFileName("oldFileName", "newFileName", "authToken");

        assertEquals("File name edited successfully", response.getBody());
    }

    @Test
    public void testGetAllFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File("file1"));
        files.add(new File("file2"));
        when(service.getAllFiles(any(Integer.class))).thenReturn(files);

        ResponseEntity<List<File>> response = controller.getAllFiles(10);

        assertEquals(files, response.getBody());
    }
}
