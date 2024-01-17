package sparklab.tts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserManagement extends  BaseEntity{
    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;

    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    private Long estimatedTime;
    private Long workedTime;
    private Long startTime;

    @Enumerated(EnumType.STRING)
    private Status status;
}
