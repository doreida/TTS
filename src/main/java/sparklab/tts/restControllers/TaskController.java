package sparklab.tts.restControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.TaskDTO;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.dto.TimeTrackerDTO;
import sparklab.tts.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("tts/task")
public class TaskController {

    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskDTO saveOrUpdate(@ModelAttribute TaskDTO taskDTO){
        return taskService.saveOrUpdate(taskDTO);
    }


    @PutMapping("/updateStatus")
    public TaskDTO updateTaskStatus(@RequestBody TaskDTO taskDTO){
        return taskService.updateTaskStatus(taskDTO);
    }


    @GetMapping
    public List<TaskDTO> findAll(){
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskDTO findById(@PathVariable String id){
        return taskService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable String id){
        return taskService.deleteById(id);
    }


    @GetMapping("/{taskId}/time-trackers")
    public ResponseEntity<List<TimeTrackerDTO>> getAllTimeTrackersForUser(@PathVariable Long taskId) {
        List<TimeTrackerDTO> timeTrackers = taskService.getAllTimeTrackersForTask(taskId);
        return new ResponseEntity<>(timeTrackers, HttpStatus.OK);
    }


}
