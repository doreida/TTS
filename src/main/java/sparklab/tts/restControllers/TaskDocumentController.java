package sparklab.tts.restControllers;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.TaskDocumentDTO;
import sparklab.tts.services.TaskDocumentService;

@RestController
@RequestMapping("tts/taskDocument")
public class TaskDocumentController {

    public final TaskDocumentService taskDocumentService;

    public TaskDocumentController(TaskDocumentService taskDocumentService) {
        this.taskDocumentService = taskDocumentService;
    }

    @PostMapping
    public ResponseEntity saveFile(@ModelAttribute TaskDocumentDTO taskDocumentDTO){
        return taskDocumentService.saveFile(taskDocumentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFile(@PathVariable String id){
        return taskDocumentService.deleteFile(id);
    }

    @GetMapping("/{id}")
    public TaskDocumentDTO findById(@PathVariable String id){
        try {
            return taskDocumentService.findById(id);
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id){
        return taskDocumentService.download(id);
    }
}
