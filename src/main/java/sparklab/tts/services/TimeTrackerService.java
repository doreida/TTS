package sparklab.tts.services;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.TimeTrackerToTimeTrackerDTO;
import sparklab.tts.dto.TimeTrackerDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.Task;
import sparklab.tts.model.TimeTracker;
import sparklab.tts.model.User;
import sparklab.tts.repositories.TaskRepository;
import sparklab.tts.repositories.TimeTrackingRepository;
import sparklab.tts.repositories.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTrackerService {

    public final TimeTrackingRepository timeTrackerRepository;

    public final TaskRepository taskRepository;

    public final UserRepository userRepository;

    public final TimeTrackerToTimeTrackerDTO toTimeTrackerDTO;

    public List<TimeTrackerDTO> getAllTimeTrackers() {
        List<TimeTracker> timeTrackers = timeTrackerRepository.findAll();
        return timeTrackers.stream()
                .map(toTimeTrackerDTO::convert)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> startTimeTracking(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (timeTrackerRepository.findActiveTimeTracker(task, user) == null) {
            TimeTracker timeTracker = new TimeTracker();
            timeTracker.setStartDate(LocalDateTime.now());
            timeTracker.setTask(task);
            timeTracker.setUser(user);
            timeTracker.setTaskStatus(task.getTaskStatus());
            timeTrackerRepository.save(timeTracker);

            return  new ResponseEntity<>("Time tracking started successfully.", HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>("Time tracking is already active for the specified task and user.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> pauseTimeTracking(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        TimeTracker activeTimeTracker = timeTrackerRepository.findActiveTimeTracker(task, user);
        if (activeTimeTracker != null) {
            TimeTracker timeTracker = activeTimeTracker;
            if (timeTracker.getEndDate() == null) {
                timeTracker.setEndDate(LocalDateTime.now());
                Duration duration = Duration.between(timeTracker.getStartDate(), timeTracker.getEndDate());
                LocalTime totalTime=convertDurationToTime(duration);
                timeTracker.setTotalTimePerTask(totalTime);
                timeTrackerRepository.save(timeTracker);
                return new ResponseEntity<>("Time tracking paused successfully. Duration: " + totalTime, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The time tracking is already paused for the specified task and user.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No active time tracking found for the specified task and user.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> resumeTimeTracking(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        TimeTracker timeTracker = new TimeTracker();
        timeTracker.setStartDate(LocalDateTime.now());
        timeTracker.setTask(task);
        timeTracker.setUser(user);
        timeTracker.setTaskStatus(task.getTaskStatus());
        timeTrackerRepository.save(timeTracker);
        return ResponseEntity.ok("Time tracking resumed successfully.");
    }


    public ResponseEntity<String> stopTimeTracking(Long taskId, Long userId) {
        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

            TimeTracker activeTimeTracker = timeTrackerRepository.findActiveTimeTracker(task, user);
            if (activeTimeTracker != null) {
                TimeTracker timeTracker = activeTimeTracker;
                if (timeTracker.getEndDate() == null) {
                    timeTracker.setEndDate(LocalDateTime.now());
                    Duration duration = Duration.between(timeTracker.getStartDate(), timeTracker.getEndDate());
                    LocalTime totalTime = convertDurationToTime(duration);
                    timeTracker.setTotalTimePerTask(totalTime);
                    timeTrackerRepository.save(timeTracker);
                }
            }
            List<TimeTracker> allTimeTrackers = timeTrackerRepository.findByTask(task);
            LocalTime totalTimePerTask = LocalTime.of(0, 0, 0);
            for (TimeTracker timeTracker : allTimeTrackers) {
                totalTimePerTask = totalTimePerTask.plusHours(timeTracker.getTotalTimePerTask().getHour()).plusMinutes(timeTracker.getTotalTimePerTask().getMinute()).plusSeconds(timeTracker.getTotalTimePerTask().getSecond());
            }
            task.setTotalTime(totalTimePerTask);
            taskRepository.save(task);
            return new ResponseEntity<>("Time tracking stopped successfully. Duration: "+totalTimePerTask, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No active time tracking found for the specified task and user.", HttpStatus.NOT_FOUND);
        }
    }


    public static LocalTime convertDurationToTime(Duration duration) {
        long totalSeconds = duration.getSeconds();
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        int seconds = (int) (totalSeconds % 60);

        return LocalTime.of(hours, minutes, seconds);
    }

}


