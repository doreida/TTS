package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.Task;
import sparklab.tts.model.TimeTrackingStatusBased;
import sparklab.tts.repositories.ProjectRepository;
import sparklab.tts.repositories.TaskRepository;
import sparklab.tts.repositories.TaskStatusRepository;
import sparklab.tts.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskDTOToTask implements Converter<TaskDTO, Task> {

    public final TaskStatusRepository statusRepository;
    public final UserRepository userRepository;
    public final ProjectRepository projectRepository;
    public final TaskRepository taskRepository;
    public final UserSlimDTOtoUser toUser;
    public final TaskDocumentDTOtoTaskDocument toTaskDocument;


    @Override
    public Task convert(TaskDTO source) {

        if(source!=null){
            Task task = new Task();
            if(source.getId() !=null){
                task.setId(source.getId());
                task.setCreatedDate(taskRepository.findById(source.getId()).get().getCreatedDate());
            }
            else{
                task.setCreatedDate(LocalDateTime.now());

            }

            task.setProject(source.getProject_id() == null ? null : projectRepository.findById(source.getProject_id()).orElseThrow(()->
                    new NotFoundException("Project with id: "+ source.getProject_id() + " does not exist!")));

            task.setName(source.getName());
            task.setDescription(source.getDescription());


            if (source.getAssignedTo()!=null)
                task.setAssignedTo(source.getAssignedTo().stream().map(userDTO -> toUser.convert(userDTO)).collect(Collectors.toSet()));

            task.setTaskStatus(source.getTaskStatus_id() == null ? null : statusRepository .findById(source.getTaskStatus_id()).orElseThrow(()->
                    new NotFoundException("Status with id " + source.getTaskStatus_id() + " does not exist!")));

            task.setCreatedBy(source.getCreatedBy_id()==null ? null : userRepository.findById(source.getCreatedBy_id()).orElseThrow(()->
                    new NotFoundException("User with id: " + source.getCreatedBy_id() + " does not exist!")));

            if (source.getAttachments()!=null && source.getId()==null) {
                task.setAttachments(source.getAttachments().stream().map(taskDocumentDTO -> toTaskDocument.convert(taskDocumentDTO)).collect(Collectors.toSet()));
            }else if (source.getId()!=null){
                task.setAttachments(taskRepository.findById(source.getId()).get().getAttachments());
            }

            return task;
        }
        return null;

    }
}
