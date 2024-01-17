package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sparklab.tts.dto.TaskDocumentDTO;
import sparklab.tts.model.TaskDocument;
import sparklab.tts.repositories.TaskDocumentRepository;
import sparklab.tts.repositories.TaskRepository;

@Component
@RequiredArgsConstructor
public class TaskDocumentToTaskDocumentDTO implements Converter<TaskDocument, TaskDocumentDTO> {

    public final TaskDocumentRepository taskDocumentRepository;
    public final TaskRepository taskRepository;

    @Override
    public TaskDocumentDTO convert(TaskDocument source) {
        if (source!=null){
            TaskDocumentDTO taskDocumentDTO = new TaskDocumentDTO();
            if (source.getId()!=null){
                taskDocumentDTO.setId(source.getId());
            }
            taskDocumentDTO.setFileName(source.getFileName());
            taskDocumentDTO.setFileType(source.getFileType());

            if (source.getFileType().contains("image/")){
                taskDocumentDTO.setFileByteArray(source.getFile_upload());
            }

            taskDocumentDTO.setDownloadURL(generateDownloadURL(source));
            if (source.getTask()!=null)
                taskDocumentDTO.setTaskId(source.getTask().getId());

            return taskDocumentDTO;
        }
        return null;
    }

    public String generateDownloadURL(TaskDocument taskDocument){

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("hopperHub/taskDocument/download/")
                .path(String.valueOf(taskDocument.getId()))
                .toUriString();

    }
}

