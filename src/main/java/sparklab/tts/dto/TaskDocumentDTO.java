package sparklab.tts.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskDocumentDTO {
    Long id;
    Long taskId;
    String downloadURL;
    String fileName;
    String fileType;
    Long fileSize;

    byte[] fileByteArray;

    MultipartFile file;
}
