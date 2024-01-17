package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.ProjectDocument;

import java.util.List;

@Repository
public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument,Long> {
    List<ProjectDocument> findProjectDocumentByProject_Id(Long id);
}
