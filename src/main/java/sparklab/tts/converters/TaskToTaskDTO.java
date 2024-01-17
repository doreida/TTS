package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskDTO;
import sparklab.tts.model.Task;
import sparklab.tts.repositories.UserRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskToTaskDTO implements Converter<Task, TaskDTO> {

    public final UserRepository userRepository;
    public final UserToUserSlimDTO toUserSlimDTO;
    public final TaskDocumentToTaskDocumentDTO toTaskDocumentDTO;
    public final TaskStatusToTaskStatusDTO toTaskStatusDTO;

    public final ProjectToProjectSlimDTO toProjectSlimDTO;

    @Override
    public TaskDTO convert(Task source) {

        if(source!=null){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(source.getId());
            taskDTO.setName(source.getName());
            taskDTO.setDescription(source.getDescription());
            taskDTO.setCreatedDate(source.getCreatedDate());
            taskDTO.setTaskIdName(source.getTaskIdName());

            if (source.getAssignedTo()!=null)
                taskDTO.setAssignedTo(source.getAssignedTo().stream().map(user -> toUserSlimDTO.convert(user)).collect(Collectors.toList()));

            if (source.getTaskStatus()==null){
                taskDTO.setTaskStatus_id(null);
            }else {
                taskDTO.setTaskStatus_id(source.getTaskStatus().getId());
                taskDTO.setTaskStatus(toTaskStatusDTO.convert(source.getTaskStatus()));
            }

            if(source.getProject() == null){
                taskDTO.setProject_id(null);
            }else {
                taskDTO.setProject_id(source.getProject().getId());
                taskDTO.setProject(toProjectSlimDTO.convert(source.getProject()));
            }

            if (source.getCreatedBy() == null){
                taskDTO.setCreatedBy_id(null);
            }else {
                taskDTO.setCreatedBy_id(source.getCreatedBy().getId());
                taskDTO.setCreatedBy(toUserSlimDTO.convert(source.getCreatedBy()));
            }

            if (source.getAttachments()!=null) {
                taskDTO.setAttachments(source.getAttachments().stream().map(taskDocument -> toTaskDocumentDTO.convert(taskDocument)).collect(Collectors.toList()));
            }


            return taskDTO;
        }
        return null;

    }
}
