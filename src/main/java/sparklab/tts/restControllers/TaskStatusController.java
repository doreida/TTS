package sparklab.tts.restControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.services.TaskStatusService;

import java.util.List;

@RestController
@RequestMapping("tts/taskStatus")
public class TaskStatusController {

    public final TaskStatusService taskStatusService;

    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping
    public List<TaskStatusDTO> findAll(){
        return taskStatusService.findAll();
    }

    @GetMapping("/{id}")
    public TaskStatusDTO findById(@PathVariable String id){
        return taskStatusService.findById(id);
    }

    @PostMapping
    public ResponseEntity saveOrUpdate(@RequestBody TaskStatusDTO taskStatusDTO){
        return taskStatusService.saveOrUpdate(taskStatusDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStatus(@PathVariable String id){
        return taskStatusService.deleteStatus(id);
    }
}
