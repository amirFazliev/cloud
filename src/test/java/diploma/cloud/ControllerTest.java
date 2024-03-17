package diploma.cloud;

import diploma.cloud.controller.FileController;
import diploma.cloud.domain.File;
import diploma.cloud.domain.Login;
import diploma.cloud.service.FileService;
import diploma.cloud.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private FileService fileService;
    @InjectMocks
    private FileController controller;

    @Test
    public void testSuccessfulLogin() {
        Login login = new Login("username", "password");
        when(userService.login(login)).thenReturn(true);
        ResponseEntity<String> response = controller.login(login);
        assertEquals("Login successful", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUnsuccessfulLogin() {
        Login login = new Login("username", "password");
        when(userService.login(login)).thenReturn(false);
        ResponseEntity<String> response = controller.login(login);
        assertEquals("Invalid credentials", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLogout() {
        String authToken = "dummyAuthToken";
        ResponseEntity<String> response = controller.logout(authToken);
        verify(userService, times(1)).logout(authToken);
        assertEquals("Logged out successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditFileName(){
        Long id = 1L;
        String authToken = "auth-token";
        String name = "newName";
        ResponseEntity<String> response = controller.editFileName(id, authToken, name);
        assertEquals("File name updated successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditFileContent() throws IOException {
        String authToken = "auth-token";
        String filename = "filename";
        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<String> response = controller.editFileContent(authToken, filename, file);
        assertEquals("File content updated successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllFiles(){
        String authToken = "auth-token";
        ResponseEntity<List<File>> response = controller.getAllFiles(authToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUploadFile() throws IOException {
        String authToken = "auth-token";
        String filename = "filename";
        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<String> response = controller.uploadFile(authToken, filename, file);
        assertEquals("File uploaded successfully with ID: 1", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFile(){
        String authToken = "auth-token";
        String filename = "filename";
        ResponseEntity<String> response = controller.deleteFile(authToken, filename);
        assertEquals("File with name filename deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDownloadFile(){
        String authToken = "auth-token";
        String filename = "filename";
        ResponseEntity<byte[]> response = controller.downloadFile(authToken, filename);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditFileName_UserAuthenticated_FileNameUpdatedSuccessfully() {
        // Setup
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);

        when(fileService.editFileName(anyLong(), anyString())).thenReturn(true);;

        // Test
        ResponseEntity<String> response = controller.editFileName(1L, "token", "newFileName");

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File name updated successfully", response.getBody());
    }

    @Test
    public void testEditFileName_UserAuthenticated_ErrorUpdatingFileName() {
        // Setup
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);

        when(fileService.editFileName(anyLong(), anyString())).thenReturn(false);

        // Test
        ResponseEntity<String> response = controller.editFileName(1L, "token", "newFileName");

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error updating file name", response.getBody());
    }

    @Test
    public void testEditFileName_UserNotAuthenticated() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(false);

        // Test
        ResponseEntity<String> response = controller.editFileName(1L, "invalidToken", "newFileName");

        // Verify
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    void testUploadFileUserNotAuthenticated() throws IOException {
        String authToken = "mockAuthToken";

        ResponseEntity<String> response = controller.uploadFile(authToken, "test.txt", new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes()));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    void testUploadFileSuccess() throws IOException {
        String authToken = "mockAuthToken";

        ResponseEntity<String> response = controller.uploadFile(authToken, "test.txt", new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes()));

        // Assert that the response is OK and contains the file ID
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("File uploaded successfully with ID"));
    }

    @Test
    public void testDeleteFile_UserAuthenticated() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);

        ResponseEntity<String> response = controller.deleteFile("validToken", "testfile.txt");

        verify(userService, times(1)).isUserAuthenticated("validToken");
        verify(fileService, times(1)).deleteFile("testfile.txt");
        assertEquals("File with name testfile.txt deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFile_UserNotAuthenticated() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(false);

        ResponseEntity<String> response = controller.deleteFile("invalidToken", "testfile.txt");

        verify(userService, times(1)).isUserAuthenticated("invalidToken");
        verify(fileService, never()).deleteFile(anyString());
        assertEquals("Unauthorized", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testDownloadFileSuccess() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);
        when(fileService.getFileByName(anyString())).thenReturn(new File("testFile.txt", "Hello, World!".getBytes()));

        ResponseEntity<byte[]> response = controller.downloadFile("validToken", "testFile.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testFile.txt", response.getHeaders().getContentDisposition().getFilename());
    }

    @Test
    void testDownloadFileUnauthorized() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(false);

        ResponseEntity<byte[]> response = controller.downloadFile("invalidToken", "testFile.txt");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDownloadFileNotFound() {
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);
        when(fileService.getFileByName(anyString())).thenReturn(null);

        ResponseEntity<byte[]> response = controller.downloadFile("validToken", "nonExistentFile.txt");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testEditFileContent_UserAuthenticated() throws IOException {
        when(userService.isUserAuthenticated(anyString())).thenReturn(true);
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        ResponseEntity<String> response = controller.editFileContent("valid-token", "test.txt", mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File content updated successfully", response.getBody());

        verify(fileService, times(1)).editFileContent(mockFile, "test.txt");
    }

    @Test
    public void testEditFileContent_UserNotAuthenticated() throws IOException {
        when(userService.isUserAuthenticated(anyString())).thenReturn(false);
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        ResponseEntity<String> response = controller.editFileContent("invalid-token", "test.txt", mockFile);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());

        verify(fileService, never()).editFileContent(any(), anyString());
    }

    @Test
    public void testGetAllFiles_AuthenticatedUser() {

        String authToken = "validAuthToken";
        List<File> expectedFiles = fileService.getAllFiles();

        ResponseEntity<List<File>> response = controller.getAllFiles(authToken);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFiles, response.getBody());
    }

    @Test
    public void testGetAllFiles_UnauthenticatedUser() {

        String authToken = "invalidAuthToken";

        ResponseEntity<List<File>> response = controller.getAllFiles(authToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}