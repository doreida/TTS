package sparklab.tts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus extends BaseEntity {
    private String description;
    private String color;

    @OneToMany(mappedBy = "taskStatus")
    private Set<Task> tasks = new HashSet<>();
}
