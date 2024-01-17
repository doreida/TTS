package sparklab.tts.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskBasicDTO {
    private Long id;
    private String name;
    private TaskStatusDTO status;
    private List<UserSlimDTO> assignedTo;
    private String description;
}
