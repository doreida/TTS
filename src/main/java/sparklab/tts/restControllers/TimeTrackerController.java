package sparklab.tts.restControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.TimeTrackerDTO;
import sparklab.tts.services.TimeTrackerService;

import java.util.List;

@RestController
@RequestMapping("tts/time-tracking")
public class TimeTrackerController {

    private final TimeTrackerService timeTrackerService;

    public TimeTrackerController(TimeTrackerService timeTrackerService) {
        this.timeTrackerService = timeTrackerService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<TimeTrackerDTO>> getAllTimeTrackers() {
        List<TimeTrackerDTO> timeTrackers = timeTrackerService.getAllTimeTrackers();
        return new ResponseEntity<>(timeTrackers, HttpStatus.OK);
    }
    @PostMapping("/start")
    public ResponseEntity<String> startTimeTracking(@RequestParam Long taskId, @RequestParam Long userId) {
       return timeTrackerService.startTimeTracking(taskId, userId);
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopTimeTracking(@RequestParam Long taskId, @RequestParam Long userId) {
       return timeTrackerService.stopTimeTracking(taskId, userId);
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseTimeTracking(@RequestParam Long taskId, @RequestParam Long userId) {
        return timeTrackerService.pauseTimeTracking(taskId, userId);
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resumeTimeTracking(@RequestParam Long taskId, @RequestParam Long userId) {
        return timeTrackerService.resumeTimeTracking(taskId, userId);
    }
}
