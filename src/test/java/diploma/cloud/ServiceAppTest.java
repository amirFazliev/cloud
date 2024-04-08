package diploma.cloud;

import diploma.cloud.domain.File;
import diploma.cloud.repository.RepositoryApp;
import diploma.cloud.service.ServiceApp;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceAppTest {
    private RepositoryApp repository = Mockito.mock(RepositoryApp.class);
    private ServiceApp serviceApp = new ServiceApp(repository);


    @Test
    public void testUploadFile() throws IOException {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getBytes()).thenReturn(new byte[0]);
        serviceApp.uploadFile(mockFile, "token");
        verify(repository).save(any(File.class));
    }

    @Test
    public void testDownloadFile() {
        when(repository.findByFilename("test.txt")).thenReturn(new File("test.txt", "hash"));
        File file = serviceApp.downloadFile("test.txt", "token");
        assertEquals("test.txt", file.getFilename());
    }

    @Test
    public void testDeleteFile() {
        when(repository.findByFilename("test.txt")).thenReturn(new File("test.txt", "hash"));
        serviceApp.deleteFile("test.txt", "token");
        verify(repository).deleteByFilename("test.txt");
    }

    @Test
    public void testEditFileName() {
        when(repository.findByFilename("test.txt")).thenReturn(new File("test.txt", "hash"));
        serviceApp.editFileName("test.txt", "new.txt");
        verify(repository).deleteByFilename("test.txt");
        verify(repository).save(any(File.class));
    }

    @Test
    public void testAuthenticate() {
        when(repository.checkCredentials("login", "password")).thenReturn(true);
        String token = serviceApp.authenticate("login", "password");
        assertEquals("token", token);
    }
}
