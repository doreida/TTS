package sparklab.tts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTrackingStatusBased extends BaseEntity{

    @ManyToOne
    @JoinColumn(name="taskId",referencedColumnName = "id")
    private Task task;
    @ManyToOne
    @JoinColumn(name="taskStatusId",referencedColumnName = "id")
    private TaskStatus taskStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime totalTimePerStatus;
}
