package diploma.cloud.repository;

import diploma.cloud.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFilename(String filename);
    void deleteFile(Long id);
    Optional<File> findById(Long id);

    List<File> findAll();
}
