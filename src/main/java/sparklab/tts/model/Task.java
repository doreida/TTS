package sparklab.tts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task extends  BaseEntity{

    private  String taskIdName;
    private String name;

    private String description;

    private LocalDateTime createdDate;

    @ManyToMany
    @JoinTable(
            name ="user_task",
            joinColumns = @JoinColumn(name ="task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> assignedTo;

    @ManyToOne
    @JoinColumn(name = "createdBy_id", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "taskStatus_id", referencedColumnName = "id")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "task_id")
    Set<TaskDocument> attachments ;

    @OneToMany(mappedBy="task",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("task")
    private List<TimeTracker> timeTrackers;

    private LocalTime totalTime;


}
