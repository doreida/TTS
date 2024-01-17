package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.TaskStatusDTOToTaskStatus;
import sparklab.tts.converters.TaskStatusToTaskStatusDTO;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.repositories.TaskStatusRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    public final TaskStatusRepository taskStatusRepository;
    public final TaskStatusToTaskStatusDTO toTaskStatusDTO;
    public final TaskStatusDTOToTaskStatus toTaskStatus;

    public List<TaskStatusDTO> findAll(){
        return taskStatusRepository.findAll().stream().map(taskStatus -> toTaskStatusDTO.convert(taskStatus)).collect(Collectors.toList());
    }

    public ResponseEntity saveOrUpdate(TaskStatusDTO taskStatusDTO){
        taskStatusRepository.save(toTaskStatus.convert(taskStatusDTO));
        ResponseEntity response = new ResponseEntity("Status created", HttpStatus.OK);
        return response;
    }

    public TaskStatusDTO findById(String id){
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Id: " +id+ " cannot be parsed");
        }
        return toTaskStatusDTO.convert(taskStatusRepository.findById(parseId).orElseThrow(()->
                new NotFoundException("Status with id: " + parseId + " does not exist")));
    }

    public ResponseEntity deleteStatus(String id){
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Id: " +id+ " cannot be parsed");
        }
        taskStatusRepository.deleteById(parseId);

        return new ResponseEntity("Task status deleted",HttpStatus.OK);
    }
}
