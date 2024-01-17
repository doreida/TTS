package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.TaskDocument;

@Repository
public interface TaskDocumentRepository extends JpaRepository<TaskDocument,Long> {
}
