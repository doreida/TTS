package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.model.TaskStatus;

@Component
public class TaskStatusToTaskStatusDTO implements Converter<TaskStatus, TaskStatusDTO> {

    @Override
    public TaskStatusDTO convert(TaskStatus source) {
        if(source !=null){
            TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
            if (source.getId()!=null) {
                taskStatusDTO.setId(source.getId());
            }
            taskStatusDTO.setDescription(source.getDescription());
            taskStatusDTO.setColor(source.getColor());
            return  taskStatusDTO;

        }
        return null;
    }
}
