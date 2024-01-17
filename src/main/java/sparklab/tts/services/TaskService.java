package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.TaskDTOToTask;
import sparklab.tts.converters.TaskToTaskDTO;
import sparklab.tts.converters.TimeTrackerToTimeTrackerDTO;
import sparklab.tts.dto.TaskDTO;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.dto.TimeTrackerDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.Project;
import sparklab.tts.model.Task;
import sparklab.tts.model.TimeTracker;
import sparklab.tts.model.TimeTrackingStatusBased;
import sparklab.tts.repositories.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    public final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    public final TaskToTaskDTO toTaskDTO;
    public final TaskDTOToTask toTask;
    public final ProjectRepository projectRepository;
    public final TimeTrackingRepository timeTrackingRepository;

    public final TimeTrackerToTimeTrackerDTO toTimeTrackerDTO;
    private final TimeTrackingStatusBasedRepository timeTrackingStatusBasedRepository;


    public List<TaskDTO> findAll() {
        return taskRepository.findAll().stream().map(task -> toTaskDTO.convert(task)).collect(Collectors.toList());
    }
    public TaskDTO findById(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id: " + id + " cannot be parsed");
        }
        return toTaskDTO.convert(taskRepository.findById(parseId).orElseThrow(() ->
                new NotFoundException("Record with id: " + id + " not found!")));
    }

    public ResponseEntity deleteById(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id: " + id + " cannot be parsed");
        }
        taskRepository.deleteById(parseId);
        ResponseEntity response = new ResponseEntity("Task deleted", HttpStatus.OK);
        return response;
    }

    public TaskDTO saveOrUpdate(TaskDTO taskDTO) {
        Task task = toTask.convert(taskDTO);
        Task savedTask = taskRepository.save(task);
        TimeTrackingStatusBased timeTrackingStatusBased = new TimeTrackingStatusBased();
        timeTrackingStatusBased.setTask(savedTask);
        timeTrackingStatusBased.setTaskStatus(savedTask.getTaskStatus());
        timeTrackingStatusBased.setStartDate(LocalDate.now());
        timeTrackingStatusBasedRepository.save(timeTrackingStatusBased);
        if (taskDTO.getId() == null) {
            Project project = projectRepository.findById(task.getProject().getId()).orElseThrow(() ->
                    new NotFoundException("Project with id: " + task.getProject().getId() + " not found"));
            projectRepository.save(project);
        }
        TaskDTO response = toTaskDTO.convert(task);
        return response;
    }


    public List<TimeTrackerDTO> getAllTimeTrackersForTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found"));

        List<TimeTracker> timeTrackings = timeTrackingRepository.findByTask(task);

        return timeTrackings.stream()
                .map(toTimeTrackerDTO::convert)
                .collect(Collectors.toList());
    }


    public TaskDTO updateTaskStatus(TaskDTO taskDTO) {
        try {
            Task savedTask = taskRepository.findById(taskDTO.getId()).get();
            List<TimeTracker> timeTrackersPerTask = timeTrackingRepository.findByTaskStatusAndTask(savedTask.getTaskStatus(), savedTask);
            LocalTime totalTimePerTask = LocalTime.of(0, 0, 0);
            for (TimeTracker timeTracker : timeTrackersPerTask) {
                totalTimePerTask = totalTimePerTask.plusHours(timeTracker.getTotalTimePerTask().getHour()).plusMinutes(timeTracker.getTotalTimePerTask().getMinute()).plusSeconds(timeTracker.getTotalTimePerTask().getSecond());
            }
            TimeTrackingStatusBased actualBased = timeTrackingStatusBasedRepository.findByTaskStatusAndTask(savedTask.getTaskStatus(),savedTask);
            actualBased.setEndDate(LocalDate.now());
            actualBased.setTotalTimePerStatus(totalTimePerTask);
            timeTrackingStatusBasedRepository.save(actualBased);
            if (timeTrackingStatusBasedRepository.existsByTaskStatusAndTask(taskStatusRepository.findById(taskDTO.getTaskStatus_id()).get(), savedTask)==false) {
                TimeTrackingStatusBased timeTrackingStatusBased = new TimeTrackingStatusBased();
                timeTrackingStatusBased.setTask(savedTask);
                timeTrackingStatusBased.setStartDate(LocalDate.now());
                savedTask.setTaskStatus(taskStatusRepository.findById(taskDTO.getTaskStatus_id()).get());
                timeTrackingStatusBased.setTaskStatus(savedTask.getTaskStatus());
                timeTrackingStatusBasedRepository.save(timeTrackingStatusBased);
            } else {
                TimeTrackingStatusBased timeTrackingStatusBased = timeTrackingStatusBasedRepository.findByTaskStatusAndTask(savedTask.getTaskStatus(), savedTask);
                timeTrackingStatusBased.setTotalTimePerStatus(totalTimePerTask);
                savedTask.setTaskStatus(taskStatusRepository.findById(taskDTO.getTaskStatus_id()).get());
                timeTrackingStatusBasedRepository.save(timeTrackingStatusBased);
            }

            return toTaskDTO.convert(taskRepository.save(savedTask));
        } catch (Exception e) {
            throw new NotFoundException("not updated");
        }
    }
}
