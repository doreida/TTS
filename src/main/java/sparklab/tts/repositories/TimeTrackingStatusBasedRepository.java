package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.Task;
import sparklab.tts.model.TaskStatus;
import sparklab.tts.model.TimeTrackingStatusBased;

@Repository
public interface TimeTrackingStatusBasedRepository extends JpaRepository<TimeTrackingStatusBased,Long> {

    TimeTrackingStatusBased findByTaskStatus(TaskStatus taskStatus);

    Boolean existsByTaskStatusAndTask(TaskStatus taskStatus, Task task);
    TimeTrackingStatusBased findByTaskStatusAndTask(TaskStatus taskStatus, Task task);
}
