package diploma.cloud.service;

import diploma.cloud.domain.File;
import diploma.cloud.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    //uploadFile, getAllFiles и deleteFile

    public File uploadFile(MultipartFile file, String filename) throws IOException {
        // Логика загрузки файла
        File newFile = new File();
        newFile.setFilename(filename);
        newFile.setContent(file.getBytes());
        return fileRepository.save(newFile);
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public void deleteFile(Long id) {
        fileRepository.deleteFile(id);
    }
}
