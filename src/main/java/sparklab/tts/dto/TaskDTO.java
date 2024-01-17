package sparklab.tts.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {
    Long id;
    String name;
    String description;
    LocalDateTime createdDate;
    List<UserSlimDTO> assignedTo;
    Long createdBy_id;
    UserSlimDTO createdBy;
    Long project_id;
    ProjectSlimDTO project;
    Long taskStatus_id;
    TaskStatusDTO taskStatus;
    String taskIdName;
    List<TaskDocumentDTO> attachments;

}
