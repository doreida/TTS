package sparklab.tts.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeTracker extends BaseEntity{
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime totalTimePerTask;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("timeTrackers")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("timeTrackers")
    private User user;

    @ManyToOne
    @JoinColumn(name = "taskStatusId", referencedColumnName = "id", nullable = false)

    private TaskStatus taskStatus;

}
