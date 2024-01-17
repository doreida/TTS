package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.model.TaskStatus;

@Component
public class TaskStatusDTOToTaskStatus implements Converter<TaskStatusDTO, TaskStatus> {

    @Override
    public TaskStatus convert(TaskStatusDTO source) {
        if (source != null){
            TaskStatus taskStatus = new TaskStatus();
            if (source.getId()!=null){
                taskStatus.setId(source.getId());
            }
            taskStatus.setDescription(source.getDescription());
            taskStatus.setColor(source.getColor());
            return taskStatus;
        }
        return null;
    }
}
