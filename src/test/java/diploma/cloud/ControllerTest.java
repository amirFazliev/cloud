package diploma.cloud;

import diploma.cloud.controller.ControllerApp;
import diploma.cloud.domain.File;
import diploma.cloud.service.ServiceApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ControllerTest {

    @Mock
    private ServiceApp service;

    @InjectMocks
    private ControllerApp controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File("hash1", "file1"));
        files.add(new File("hash2", "file2"));

        when(service.getAllFiles()).thenReturn(files);

        List<File> result = controller.getAllFiles(2, "authToken");

        assertEquals(2, result.size());
        assertEquals("file1", result.get(0).getFilename());
        assertEquals("file2", result.get(1).getFilename());
    }


}
