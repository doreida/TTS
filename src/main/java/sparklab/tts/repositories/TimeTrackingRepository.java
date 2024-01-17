package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.Task;
import sparklab.tts.model.TaskStatus;
import sparklab.tts.model.TimeTracker;
import sparklab.tts.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTrackingRepository extends JpaRepository<TimeTracker,Long> {

    List<TimeTracker> findAll();

    @Query("SELECT tt FROM TimeTracker tt WHERE tt.task = :task AND tt.user = :user AND tt.endDate IS NULL")
    TimeTracker findActiveTimeTracker(@Param("task") Task task, @Param("user") User user);

    List<TimeTracker> findByUser_Id(Long userId);

    List<TimeTracker> findByTask(Task task);
    List<TimeTracker> findByTaskStatusAndTask(TaskStatus taskStatus,Task task);
}
