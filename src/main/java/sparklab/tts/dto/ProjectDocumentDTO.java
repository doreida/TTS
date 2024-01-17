package sparklab.tts.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProjectDocumentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private MultipartFile file;
    private Long projectId;
    private String downloadUrl;
    private byte[] document;
}
