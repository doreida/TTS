package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskBasicDTO;
import sparklab.tts.model.Task;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskToTaskBasicDTO implements Converter<Task, TaskBasicDTO> {

    private final UserToUserSlimDTO toUserSlimDTO;
    private final TaskStatusToTaskStatusDTO toTaskStatusDTO;

    @Override
    public TaskBasicDTO convert(Task source) {
        if(source!=null) {
            TaskBasicDTO taskBasicDTO = new TaskBasicDTO();
            taskBasicDTO.setId(source.getId());
            taskBasicDTO.setName(source.getName());
            taskBasicDTO.setDescription(source.getDescription());
            if (source.getAssignedTo()!=null)
                taskBasicDTO.setAssignedTo(source.getAssignedTo().stream().map(user -> toUserSlimDTO.convert(user)).collect(Collectors.toList()));

            if (source.getTaskStatus() == null) {
                taskBasicDTO.setStatus(null);
            } else {
                taskBasicDTO.setStatus(toTaskStatusDTO.convert(source.getTaskStatus()));
            }

            return taskBasicDTO;
        }

        return null;
    }
}
