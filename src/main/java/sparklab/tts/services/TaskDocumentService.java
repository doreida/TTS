package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.TaskDocumentDTOtoTaskDocument;
import sparklab.tts.converters.TaskDocumentToTaskDocumentDTO;
import sparklab.tts.dto.TaskDocumentDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.TaskDocument;
import sparklab.tts.repositories.TaskDocumentRepository;

@Service
@RequiredArgsConstructor
public class TaskDocumentService {

    private final TaskDocumentRepository taskDocumentRepository;
    private final TaskDocumentToTaskDocumentDTO toTaskDocumentDTO;
    private final TaskDocumentDTOtoTaskDocument toTaskDocument;

    public ResponseEntity saveFile(TaskDocumentDTO taskDocumentDTO){
        taskDocumentRepository.save(toTaskDocument.convert(taskDocumentDTO));
        return new ResponseEntity<>("Attachement added succesfully", HttpStatus.OK);
    }

    public ResponseEntity deleteFile(String id){

        try
        {
            Long parseId = Long.parseLong(id);
            taskDocumentRepository.deleteById(parseId);
            return new ResponseEntity<>("Attachment deleted",HttpStatus.OK);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Id: " +id+ " cannot be parsed");
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to delete the file", e);
        }

    }

    public ResponseEntity<Resource> download(String id){


        try {
            Long parseId = Long.parseLong(id);
            TaskDocument taskDocument = taskDocumentRepository.findById(parseId).orElseThrow(()->
                    new NotFoundException("Record with id: "+ id + "not found"));

            return  ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(taskDocument.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "taskdocumnet; filename=\"" + taskDocument.getFileName()
                                    + "\"")
                    .body(new ByteArrayResource(taskDocument.getFile_upload()));
        }catch (NumberFormatException e){
            throw new NumberFormatException("File id: " + id + " can't be parsed!");
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to download the file", e);
        }

    }

    public TaskDocumentDTO findById(String id) {

        try
        {
           Long parseId = Long.parseLong(id);
            TaskDocument taskDocument = taskDocumentRepository.findById(parseId).orElseThrow(()-> new NotFoundException("Record with id: " + id + " not found!"));
            return toTaskDocumentDTO.convert(taskDocument);
        }catch (NumberFormatException e){
            throw new NumberFormatException("File id: " + id + " can't be parsed!");
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to find task document by id", e);
        }

    }


}
