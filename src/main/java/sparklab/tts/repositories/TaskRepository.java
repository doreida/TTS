package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByProjectId(Long id);

    @Query(value="select * from task where id in(Select task_id from user_task  where user_id=:id)",nativeQuery = true)
    List<Task> findByAssignedTo(Long id);
}
