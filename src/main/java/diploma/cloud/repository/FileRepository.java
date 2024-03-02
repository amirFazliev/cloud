package diploma.cloud.repository;

import diploma.cloud.domain.File;
import diploma.cloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByFilename(String filename);

    void deleteFile(Long id);
}
