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
public class TaskDocument extends  BaseEntity{

    private String fileName;

    private String fileType;

    @Lob
    @Column( length = 2147483647)
    private byte[] file_upload;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name="task_id")
    private Task task;
}
