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

    public File uploadFile(MultipartFile file, String filename) throws IOException {
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

    public boolean editFileName(Long id, String name) {
        File file = fileRepository.findById(id).orElse(null);
        if (file != null) {
            file.setFilename(name);
            fileRepository.save(file);
            return true;
        }
        return false;
    }

    public File getFileByName(String filename) {
        return fileRepository.findByFilename(filename);
    }

    public void editFileContent(MultipartFile file, String filename) throws IOException {
        File fileToEdit = fileRepository.findByFilename(filename);
        fileToEdit.setContent(file.getBytes());
        fileRepository.save(fileToEdit);
    }

    public void deleteFile(String filename) {
        fileRepository.deleteFile(fileRepository.findByFilename(filename).getId());
    }
}
