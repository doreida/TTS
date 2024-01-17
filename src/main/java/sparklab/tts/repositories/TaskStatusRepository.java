package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.TaskStatus;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus,Long> {
}
