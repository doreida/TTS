package sparklab.tts.converters;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparklab.tts.dto.TaskDocumentDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.exceptions.NotValidFileException;
import sparklab.tts.model.Task;
import sparklab.tts.model.TaskDocument;
import sparklab.tts.repositories.TaskDocumentRepository;
import sparklab.tts.repositories.TaskRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TaskDocumentDTOtoTaskDocument implements Converter<TaskDocumentDTO, TaskDocument> {

    public final TaskRepository taskRepository;
    public final TaskDocumentRepository taskDocumentRepository;

    @Override
    public TaskDocument convert(TaskDocumentDTO source) {
        if (source ==null) {
            return null;
        }

        if(source.getFile() == null) {
            throw new NotValidFileException("You should add a document to save the record");
        }
            TaskDocument taskDocument = new TaskDocument();
        try {
            if (source.getId() != null) {
                TaskDocument existingDocument = taskDocumentRepository.findById(source.getId())
                        .orElseThrow(() -> new NotFoundException("Task Document with id: " + source.getId() + " does not exist!"));

                taskDocument.setId(existingDocument.getId());
                taskDocument.setFile_upload(existingDocument.getFile_upload());
                taskDocument.setFileName(existingDocument.getFileName());
                taskDocument.setFileType(existingDocument.getFileType());
                taskDocument.setTask(getTaskById(source.getTaskId()));
            }else
            {
                taskDocument.setFile_upload(source.getFile().getBytes());
                taskDocument.setFileName(StringUtils.cleanPath(source.getFile().getOriginalFilename()));
                taskDocument.setFileType(source.getFile().getContentType());
                taskDocument.setTask(getTaskById(source.getTaskId()));
            }

            } catch(IOException e){
                throw new RuntimeException("Error while processing the file", e);
            }

        return taskDocument;
    }
    private Task getTaskById(Long taskId){
        return (taskId == null) ? null : taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: "+ taskId+ " does not exist"));
    }
}
