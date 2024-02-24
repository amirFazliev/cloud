package diploma.cloud.repository;

import diploma.cloud.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    // Дополнительные методы для работы с файлами
    File findByFilename(String filename);
    void deleteFile(Long id);
}
