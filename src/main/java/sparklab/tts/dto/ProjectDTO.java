package sparklab.tts.dto;

import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String  description;
    private List<UserSlimDTO> members;
    private List<ProjectDocumentDTO> documents;
    private UserSlimDTO createdBy;
    private LocalDateTime created_date;
}
